package com.fintecsystems.xs2awizard

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.toSpannable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.fintecsystems.xs2awizard.components.CustomTabsURLSpan
import com.fintecsystems.xs2awizard.components.LoadingIndicator
import com.fintecsystems.xs2awizard.components.XS2AJavascriptInterface
import com.fintecsystems.xs2awizard.form.components.*
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.form.components.selectLine.SelectLine
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine
import com.fintecsystems.xs2awizard.helper.Utils
import com.fintecsystems.xs2awizard.components.GenericViewModel
import com.fintecsystems.xs2awizard.form.components.textLine.AutoCompleteResponse
import com.fintecsystems.xs2awizard_networking.NetworkingInstance
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import java.util.*

private const val TAG = "XS2AWizard"

/**
 * Action delegate to be used by the form elements of the wizard.
 */
interface XS2AWizardActionDelegate {
    fun goBack()
    fun abort()
    fun restart()
    fun submitForm()
    fun submitForm(action: String)
    fun submitForm(jsonBody: JsonElement, showIndicator: Boolean = true)
    fun submitFormWithCallback(action: String, onSuccess: (AutoCompleteResponse) -> Unit)
    fun triggerAutoSubmit(delay: Long)
    fun openWebView(url: String)
    fun parseMarkupText(tv: TextView, textToParse: String?)
    fun constructFragment(formElementData: FormLineData): FormLine?
    fun incrementLoadingIndicatorLock()
    fun decrementLoadingIndicatorLock()
}

/**
 * A [Fragment] subclass.
 * Implementation of the XS2A Wizard
 * Implements [XS2AWizardActionDelegate] for use in children.
 */
class XS2AWizard() : Fragment(), XS2AWizardActionDelegate {
    data class XS2AWizardViewModel(
        var config: XS2AWizardConfig? = null,
        var autoSubmitTask: Runnable? = null
    )

    // ViewModel used to save config data and autoSubmitTask reference during view rebuilds.
    private val viewModel: GenericViewModel<XS2AWizardViewModel> by viewModels()

    // activityViewModel is shared across the entire activity to be shared amongst components.
    private val styleIdModel: GenericViewModel<Int> by activityViewModels()

    // mConfig is used to temporarily store config data until viewModel is ready to be called.
    // ViewModels are only available during and after the onCreate lifecycle method.
    // That's why we cannot save the config data to the viewModel directly on instantiation.
    private var mConfig: XS2AWizardConfig? = null
    private val config: XS2AWizardConfig
        get() {
            return viewModel.liveData.value?.config!!
        }

    private val formElements = mutableListOf<FormLine>()

    private lateinit var loadingIndicator: LoadingIndicator
    private var loadingIndicatorLock = 0

    private var autoSubmitTask: Runnable?
        get() {
            return viewModel.liveData.value?.autoSubmitTask
        }
        set(value) {
            viewModel.liveData.value?.autoSubmitTask = value
        }

    // The following variables are to keep the App from crashing while being in the background
    // or during the transition to the background.
    // Used to keep track, if app is in background or foreground.
    private var isInBackground = false

    // If a autosubmit request tried to fire in the background.
    private var triedToAutoSubmit = false

    // onFormReceived body if it got captured while being in background.
    private var onFormReceivedDataFromBackground: String? = null

    // Secondary constructor used by the User to pass config.
    constructor(_config: XS2AWizardConfig) : this() {
        mConfig = _config
    }

    override fun onPause() {
        super.onPause()

        if (autoSubmitTask != null) {
            isInBackground = true
        }
    }

    override fun onResume() {
        super.onResume()

        isInBackground = false

        // If we received an form response while being in background, we captured it
        // and process it now instead.
        if (onFormReceivedDataFromBackground != null) {
            onFormReceived(onFormReceivedDataFromBackground!!)
            onFormReceivedDataFromBackground = null
        }

        // We tried to "autosubmit" while being in the background, but we cannot do that.
        // Just do that now instead.
        if (triedToAutoSubmit) {
            triedToAutoSubmit = false
            submitForm(constructJsonBody("autosubmit"), false)
        }
    }

    override fun onStart() {
        super.onStart()

        // If mConfig is set, that means a new instance of the wizard got created.
        // onStart is also called during rebuilds (e.g. screen orientation changed) or
        // coming back into foreground.
        // With this check we can ensure, that the form doesn't get initialized a second time.
        if (mConfig != null) {
            // Set mConfig back to null, so that on future onStart calls the wizard doesn't get
            // initialized again.
            mConfig = null

            // Configure NetworkingInstance
            NetworkingInstance.getInstance(requireContext()).apply {
                sessionKey = config.sessionKey
                backendURL = config.backendURL
            }

            initForm()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewModel if not existent.
        if (viewModel.liveData.value == null) {
            viewModel.liveData.value = XS2AWizardViewModel()
        }

        // Apply config
        // Unlike onStart we don't null mConfig after the check.
        // That's because onStart is called after onCreate and that would break the onStart logic.
        if (mConfig != null) {
            viewModel.liveData.value?.config = mConfig

            styleIdModel.liveData.value = mConfig?.styleResId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = Utils.getThemedInflater(
        inflater,
        requireContext(),
        styleIdModel.liveData.value
    ).inflate(R.layout.fragment_x_s2_a__wizard, container, false).also {
        loadingIndicator = it.findViewById(R.id.loading_indicator)

        // Apply custom style of the user to loadingIndicator, if it exists.
        if (styleIdModel.liveData.value != null) {
            loadingIndicator.context.theme.applyStyle(styleIdModel.liveData.value!!, true)
        }
    }


    /**
     * Initializes the Form
     *
     * This function does the very first API call to XS2A
     * and gets a form to render as response.
     */
    private fun initForm() = submitForm(
        buildJsonObject {
            put("version", JsonPrimitive(requireContext().getString(R.string.xs2a_version)))
            put("client", JsonPrimitive(requireContext().getString(R.string.xs2a_client_tag)))
        }
    )

    /**
     * Tells the server to go one step back
     */
    override fun goBack() = submitForm(
        buildJsonObject {
            put("action", JsonPrimitive("back"))
        }
    )

    /**
     * Tells the server to abort the session
     */
    override fun abort() {
        if (autoSubmitTask != null) {
            view?.removeCallbacks(autoSubmitTask!!)
        }

        submitForm(
            buildJsonObject {
                put("action", JsonPrimitive("abort"))
            }
        )
    }

    /**
     * Tells the server to restarts the session
     */
    override fun restart() = submitForm(
        buildJsonObject {
            put("action", JsonPrimitive("restart"))
        }
    )

    /**
     * Append data of formLine to the resulting jsonObject.
     * Used for recursion purposes on MultiLine.
     *
     * @param jsonBuilder current [JsonBuilder] instance.
     * @param formLine [FormLine] element used for appending it's data to [JsonBuilder].
     */
    private fun appendValueToJson(jsonBuilder: JsonObjectBuilder, formLine: FormLine) {
        when (val data = formLine.getFormData()) {
            is ValueFormLineData -> jsonBuilder.put(data.name, data.value ?: JsonNull)
            is MultiLineData -> {
                jsonBuilder.put(data.name, data.selected)

                (formLine as MultiLine).getCurrentForm()
                    ?.forEach { appendValueToJson(jsonBuilder, it) }
            }
        }
    }

    /**
     * Construct the JSON body for the form request to the backend.
     *
     * @param action [String] that describes the action of the form request.
     * @param values [JsonObject] used to override fields of request body.
     *
     * @return built [JsonObject]
     */
    private fun constructJsonBody(action: String, values: JsonObject? = null) = buildJsonObject {
        formElements.forEach { appendValueToJson(this, it) }

        put("action", JsonPrimitive(action))

        values?.entries?.forEach {
            put(it.key, it.value)
        }
    }.also { Log.d(TAG, "constructJsonBody: $it") }

    /**
     * Submits form using standard "submit" action.
     */
    override fun submitForm(): Unit = submitForm("submit")

    /**
     * Constructs request body and submits form using the specified action.
     *
     * @param action action to use.
     */
    override fun submitForm(action: String): Unit = submitForm(constructJsonBody(action), true)

    /**
     * Submits form using the specified request body.
     *
     * @param jsonBody request body.
     * @param showIndicator show loading indicator during request.
     */
    override fun submitForm(jsonBody: JsonElement, showIndicator: Boolean) {
        if (showIndicator) {
            incrementLoadingIndicatorLock()
        }

        Utils.hideSoftKeyboard(activity)

        return NetworkingInstance.getInstance(requireContext())
            .encodeAndSendMessage(
                jsonBody.toString(),
                ::onFormReceived
            )
    }

    /**
     * Submits form with specified action and calls specified callback on success.
     *
     * @param action action to use.
     * @param onSuccess on success callback to use.
     */
    override fun submitFormWithCallback(action: String, onSuccess: (AutoCompleteResponse) -> Unit) =
        NetworkingInstance.getInstance(requireContext())
            .encodeAndSendMessage(
                constructJsonBody(action).toString(),
                {
                    onSuccess(formatJson.decodeFromString(it))
                }
            )

    /**
     * Try to do an autoSubmit.
     * If app is in background, the request will not be sent but saved to fire when
     * the app is returning to foreground.
     */
    private fun tryToAutoSubmit() {
        if (!isInBackground) {
            submitForm(constructJsonBody("autosubmit"), false)
        } else {
            triedToAutoSubmit = true
        }
    }

    /**
     * Trigger autosubmit after a specified delay.
     *
     * @param delay delay until request is fired.
     */
    override fun triggerAutoSubmit(delay: Long) {
        if (autoSubmitTask != null) {
            view?.removeCallbacks(autoSubmitTask!!)
        }

        autoSubmitTask = Runnable {
            autoSubmitTask = null

            tryToAutoSubmit()
        }

        view?.postDelayed(autoSubmitTask!!, delay)
    }

    /**
     * Opens specified url in a WebView.
     *
     * @param url url to open.
     */
    override fun openWebView(url: String) {
        view?.findViewById<WebView>(R.id.webview)!!.apply {
            visibility = View.VISIBLE

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            addJavascriptInterface(XS2AJavascriptInterface(this@XS2AWizard), "App")

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    view.loadUrl(url)
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // Page has finished loading, we can hide the loadingIndicator now.
                    decrementLoadingIndicatorLock()
                }
            }

            loadUrl(url)
        }

        view?.findViewById<LinearLayout>(R.id.form_container).apply {
            this?.visibility = View.GONE
        }

        // Shows loadingIndicator until page is finished loading
        incrementLoadingIndicatorLock()
    }

    /**
     * Parses provided text and applies result to provided TextView
     *
     * @param tv [TextView], that's being applied to.
     * @param textToParse [String] to parse for markups.
     */
    override fun parseMarkupText(tv: TextView, textToParse: String?) {
        if (textToParse == null) {
            tv.text = null
            return
        }

        // Replace HTML linebreak and middot with Kotlin String counterparts.
        val text = lineBreakRegex
            .replace(
                middotRegex.replace(textToParse, "\u00B7"),
                "\n",
            )

        val spannableStringBuilder = SpannableStringBuilder()

        // Cursor used to determine startIndex of the current section.
        var cursor = 0

        do {
            // Search for markups beginning at the cursor.
            val regexResult = markupRegex.find(text, cursor)

            if (regexResult != null) {
                spannableStringBuilder.apply {
                    // Append all text between the cursor and beginning of markup.
                    append(text.slice(cursor until regexResult.range.first))

                    // Append textValue of markup.
                    append(regexResult.groupValues[1])

                    // Create Span
                    val span = when (regexResult.groupValues[2]) {
                        "autosubmit" -> object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                // Parse markup values into JSON.
                                val jsonBody = buildJsonObject {
                                    autoSubmitPayloadRegex.findAll(regexResult.groupValues[3])
                                        .forEach {
                                            it.groupValues.let { group ->
                                                put(group[1], JsonPrimitive(group[2]))
                                            }
                                        }
                                }

                                submitForm(constructJsonBody("autosubmit", jsonBody))
                            }
                        }
                        else -> CustomTabsURLSpan(regexResult.groupValues[3])
                    }

                    // Apply span to area of markup text.
                    setSpan(
                        span,
                        length - regexResult.groupValues[1].length,
                        length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }

                cursor = regexResult.range.last + 1
            }
        } while (regexResult != null)

        // If there is still text left, append the remaining section.
        if (cursor < text.length) {
            spannableStringBuilder.append(text.slice(cursor until text.length))
        }

        // Apply parsed text to TextView.
        tv.apply {
            setText(spannableStringBuilder.toSpannable())

            // Needed otherwise touch events won't get captured.
            movementMethod = LinkMovementMethod.getInstance()
        }
    }

    /**
     * Constructs a [FormLine] [Fragment] using the specified [FormLineData].
     *
     * @param formElementData the [FormLineData] for the form line.
     *
     * @return the [FormLine] or null, if we don't have an implementation yet.
     */
    override fun constructFragment(formElementData: FormLineData): FormLine? {
        val createdFragment: FormLine? = when (formElementData) {
            is AutoSubmitLineData -> AutoSubmitLine()
            is ParagraphLineData -> ParagraphLine()
            is DescriptionLineData -> DescriptionLine()
            is TextLineData -> TextLine()
            is PasswordLineData -> PasswordLine()
            is CaptchaLineData -> CaptchaLine()
            is SelectLineData -> SelectLine()
            is CheckBoxLineData -> CheckBoxLine()
            is HiddenLineData -> HiddenLine()
            is RadioLineData -> RadioLine()
            is ImageLineData -> ImageLine()
            is LogoLineData -> LogoLine()
            is FlickerLineData -> FlickerLine()
            is SubmitLineData -> SubmitLine()
            is AbortLineData -> AbortLine()
            is RestartLineData -> RestartLine()
            is TabsLineData -> TabsLine()
            is RedirectLineData -> RedirectLine()
            is MultiLineData -> MultiLine()
            else -> null
        }

        createdFragment?.setFormData(formElementData)

        return createdFragment
    }

    override fun incrementLoadingIndicatorLock() {
        if (++loadingIndicatorLock == 1)
            loadingIndicator.show()
    }

    override fun decrementLoadingIndicatorLock() {
        if (--loadingIndicatorLock <= 0) {
            loadingIndicatorLock = 0
            loadingIndicator.hide()
        }
    }

    /**
     * Parses [FormResponse] for possible callbacks
     *
     * @param response [FormResponse] to parse.
     */
    private fun parseCallback(response: FormResponse) {
        when (response.callback) {
            "finish" -> config.onFinish(response.callbackParams?.getOrNull(0)?.jsonPrimitive?.content)
            "abort" -> config.onAbort()
        }

        if (response.error != null && response.error == "tech_error") {
            config.onError(
                XS2AWizardError(
                    response.error,
                    emptyList(),
                    false
                )
            )
        }
    }

    /**
     * Checks if the form language matches the device language.
     * If the form languages don't match, the device language will be checked against our
     * supported languages.
     *
     * @param language language to check.
     *
     * @return true if language should be changed, false otherwise.
     */
    private fun checkIfLanguageNeedsToBeChanged(language: String?): Boolean {
        if (language != null) {
            val deviceLanguage = Locale.getDefault().language
            val formLanguage = Locale(language).language

            return languageWhitelist.contains(deviceLanguage) && !(deviceLanguage.equals(
                formLanguage
            ))
        }

        return false
    }


    @Serializable
    data class FormResponse(
        val form: List<FormLineData>? = null,
        val language: String? = null,
        val callback: String? = null,
        val callbackParams: List<JsonElement>? = null,
        val error: String? = null,
    )

    /**
     * Parses form response and possibly re-/builds the form.
     *
     * @param jsonString form response as a JSON [String].
     */
    private fun onFormReceived(jsonString: String) {
        try {
            Log.d(TAG, "onFormReceived: $jsonString")

            // Postpone until we're back into foreground.
            if (isInBackground) {
                onFormReceivedDataFromBackground = jsonString

                return
            }

            val formResponse = formatJson.decodeFromString<FormResponse>(jsonString)

            // Check if we're in the right language. If not change it.
            if (checkIfLanguageNeedsToBeChanged(formResponse.language)) {
                Log.d(
                    TAG,
                    "onFormReceived: Language is not the same! Form: ${formResponse.language} Device: ${Locale.getDefault().language}, Changing!"
                )

                submitForm(
                    buildJsonObject {
                        put("action", "change-language")
                        put("language", Locale.getDefault().language)
                    }
                )

                decrementLoadingIndicatorLock()

                return
            }

            // Begin transaction to modify children of this fragment.
            childFragmentManager.beginTransaction().apply {
                // Completely removed previous form elements.
                formElements.forEach { fragment -> remove(fragment) }
                formElements.clear()

                // Build the new form if we got the data.
                if (formResponse.form != null) {
                    formResponse.form.forEach { formElementData ->
                        // Construct the form line or entirely skip it, if we don't have an
                        // implementation for it yet.
                        val createdFragment = constructFragment(formElementData) ?: return@forEach

                        formElements.add(createdFragment)
                        add(R.id.form_container, createdFragment)
                    }

                }

                commit()
            }

            decrementLoadingIndicatorLock()

            parseCallback(formResponse)
        } catch (serializationException: SerializationException) {
            Log.e(TAG, "onFormReceived: $serializationException")
        }
    }

    companion object {
        private val markupRegex = Regex("\\[([\\w\\s\\-()]+)\\|(\\w+)::([&=\\w:/\\\\.]+)]")
        private val lineBreakRegex = Regex("<br>")
        private val middotRegex = Regex("&middot;")
        private val autoSubmitPayloadRegex = Regex("(\\w+)=(\\w+)")

        private val languageWhitelist = listOf("de", "en", "fr", "it", "es")

        // Serializer Module used to de-/serialize between json and our FormLineData classes.
        private val formLineDataModule = SerializersModule {
            polymorphic(FormLineData::class) {
                subclass(MultiLineData::class)
                subclass(TabsLineData::class)
                subclass(RestartLineData::class)
                subclass(AbortLineData::class)
                subclass(SubmitLineData::class)
                subclass(ImageLineData::class)
                subclass(LogoLineData::class)
                subclass(DescriptionLineData::class)
                subclass(ParagraphLineData::class)
                subclass(RedirectLineData::class)
                subclass(TextLineData::class)
                subclass(PasswordLineData::class)
                subclass(CaptchaLineData::class)
                subclass(FlickerLineData::class)
                subclass(HiddenLineData::class)
                subclass(CheckBoxLineData::class)
                subclass(RadioLineData::class)
                subclass(SelectLineData::class)
                subclass(AutoSubmitLineData::class)
            }
        }

        private val formatJson = Json {
            serializersModule = formLineDataModule; ignoreUnknownKeys = true; isLenient = true
        }
    }

    data class XS2AWizardError(
        val errorCode: String,
        val messages: List<String>,
        val recoverable: Boolean,
    )

    data class XS2AWizardConfig(
        val sessionKey: String,
        val backendURL: String? = null,
        val onFinish: (String?) -> Unit,
        val onAbort: () -> Unit,
        val onError: (XS2AWizardError) -> Unit = {},
        val styleResId: Int? = null,
    )
}
