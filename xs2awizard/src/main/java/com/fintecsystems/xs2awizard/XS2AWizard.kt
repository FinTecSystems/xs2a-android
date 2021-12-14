package com.fintecsystems.xs2awizard

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig
import com.fintecsystems.xs2awizard.components.XS2AWizardError
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.webview.URLBarWebView
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.form.components.*
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard_networking.NetworkingInstance
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*

private const val TAG = "XS2AWizard"

class XS2AWizardViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var config: XS2AWizardConfig

    val form = MutableLiveData<List<FormLineData>>()

    val loadingIndicatorLock = MutableLiveData(false)

    val currentWebViewUrl = MutableLiveData<String?>(null)

    private val context: Context
        get() = getApplication<Application>().applicationContext

    fun onStart(_config: XS2AWizardConfig) {
        config = _config

        NetworkingInstance.getInstance(context).apply {
            sessionKey = config.sessionKey
            backendURL = config.backendURL
        }

        initForm()
    }

    fun onStop() {
        // Cleanup in case the viewModel gets reused for a future request.
        loadingIndicatorLock.value = false
        currentWebViewUrl.value = null
        form.value = emptyList()
    }

    /**
     * Initializes the Form
     *
     * This function does the very first API call to XS2A
     * and gets a form to render as response.
     */
    private fun initForm() = submitForm(
        buildJsonObject {
            put("version", JsonPrimitive(BuildConfig.VERSION))
            put("client", JsonPrimitive(context.getString(R.string.xs2a_client_tag)))
        },
        true
    )

    /**
     * Tells the server to go one step back
     */
    fun goBack() = submitForm(
        buildJsonObject {
            put("action", JsonPrimitive("back"))
        }
    )

    /**
     * Tells the server to abort the session
     */
    fun abort() {
        submitForm(
            buildJsonObject {
                put("action", JsonPrimitive("abort"))
            }
        )
    }

    /**
     * Tells the server to restarts the session
     */
    fun restart() = submitForm(
        buildJsonObject {
            put("action", JsonPrimitive("restart"))
        }
    )

    /**
     * Construct the JSON body for the form request to the backend.
     *
     * @param action [String] that describes the action of the form request.
     * @param values [JsonObject] used to override fields of request body.
     *
     * @return built [JsonObject]
     */
    fun constructJsonBody(action: String, values: JsonObject? = null) = buildJsonObject {
        form.value?.forEach { appendValueToJson(this, it) }

        put("action", JsonPrimitive(action))

        values?.entries?.forEach {
            put(it.key, it.value)
        }
    }

    /**
     * Append data of formLine to the resulting jsonObject.
     * Used for recursion purposes on MultiLine.
     *
     * @param jsonBuilder current [JsonBuilder] instance.
     * @param formLineData [FormLineData] element used for appending it's data to [JsonBuilder].
     */
    private fun appendValueToJson(jsonBuilder: JsonObjectBuilder, formLineData: FormLineData) {
        when (formLineData) {
            is ValueFormLineData -> jsonBuilder.put(
                formLineData.name,
                formLineData.value ?: JsonNull
            )
            is MultiLineData -> {
                jsonBuilder.put(formLineData.name, formLineData.selected)

                formLineData.forms
                    .find { it.value == formLineData.selected }
                    ?.form?.forEach { appendValueToJson(jsonBuilder, it) }
            }
        }
    }

    /**
     * Submits form using standard "submit" action.
     */
    fun submitForm(): Unit = submitForm("submit")

    /**
     * Constructs request body and submits form using the specified action.
     *
     * @param action action to use.
     */
    fun submitForm(action: String): Unit = submitForm(constructJsonBody(action), true)

    /**
     * Submits form using the specified request body.
     *
     * @param jsonBody request body.
     * @param showIndicator show loading indicator during request.
     */
    fun submitForm(jsonBody: JsonElement, showIndicator: Boolean = true) =
        submitForm(jsonBody.toString(), showIndicator)

    /**
     * Submits form using the specified request body.
     *
     * @param jsonBody stringified request body.
     * @param showIndicator show loading indicator during request.
     */
    private fun submitForm(jsonBody: String, showIndicator: Boolean) {
        if (showIndicator) {
            loadingIndicatorLock.value = true
        }

        return NetworkingInstance.getInstance(context)
            .encodeAndSendMessage(
                jsonBody,
                ::onFormReceived
            )
    }

    /**
     * Submits form with specified action and calls specified callback on success.
     *
     * @param action action to use.
     * @param onSuccess on success callback to use.
     */
    fun submitFormWithCallback(action: String, onSuccess: (String) -> Unit) =
        NetworkingInstance.getInstance(context)
            .encodeAndSendMessage(
                constructJsonBody(action).toString(),
                onSuccess
            )

    /**
     * Handles onClick of ClickableText's with string annotations.
     *
     * @param annotation clicked annotation
     */
    fun handleAnnotationClick(activity: Activity, annotation: AnnotatedString.Range<String>) {
        when (annotation.tag) {
            "autosubmit" -> {
                val jsonBody = MarkupParser.parseAutoSubmitPayloadAsJson(annotation.item)

                submitForm(constructJsonBody("autosubmit", jsonBody))
            }
            else -> CustomTabsIntent.Builder().build().launchUrl(
                activity, Uri.parse(annotation.item)
            )
        }
    }

    /**
     * Parses form response and possibly re-/builds the form.
     *
     * @param jsonString form response as a JSON [String].
     */
    private fun onFormReceived(jsonString: String) {
        try {
            Log.d(TAG, "onFormReceived: $jsonString")

            val formResponse = JSONFormatter.formatter.decodeFromString<FormResponse>(jsonString)

            /* TODO
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

                return
            }
             */

            form.value = formResponse.form

            loadingIndicatorLock.value = false

            parseCallback(formResponse)
        } catch (serializationException: SerializationException) {
            Log.e(TAG, "onFormReceived: $serializationException")
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
     * Opens specified url in a WebView and hides the form.
     *
     * @param url url to open.
     */
    fun openWebView(url: String) {
        Log.d(TAG, "openWebView $url")

        currentWebViewUrl.value = url
    }

    /**
     * Hides the WebView and shows the form again.
     */
    fun closeWebView() {
        currentWebViewUrl.value = null
    }

}

@Composable
fun XS2AWizardComponent(
    xS2AWizardConfig: XS2AWizardConfig,
    xs2aWizardViewModel: XS2AWizardViewModel = viewModel()
) {
    val form by xs2aWizardViewModel.form.observeAsState(null)
    val loadingIndicatorLock by xs2aWizardViewModel.loadingIndicatorLock.observeAsState(false)
    val currentWebViewUrl by xs2aWizardViewModel.currentWebViewUrl.observeAsState(null)

    DisposableEffect(xs2aWizardViewModel) {
        // Initialize ViewModel
        xs2aWizardViewModel.onStart(xS2AWizardConfig)

        // Cleanup
        onDispose {
            xs2aWizardViewModel.onStop()
        }
    }

    // Render
    XS2ATheme(xS2ATheme = xS2AWizardConfig.theme) {
        Box(
            modifier = Modifier
                .padding(10.dp, 5.dp)
        ) {
            form?.let {
                FormLinesContainer(it, xs2aWizardViewModel)
            }
        }

        if (loadingIndicatorLock) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(XS2ATheme.CURRENT.loadingIndicatorBackgroundColor),
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = XS2ATheme.CURRENT.tintColor
                )

            }
        }

        if (currentWebViewUrl != null)
            URLBarWebView(xs2aWizardViewModel)
    }
}

@Composable
fun FormLines(formData: List<FormLineData>, viewModel: XS2AWizardViewModel) {
    val formDataHashString = formData.hashCode().toString()

    formData.forEach { formLineData ->
        val formLineKey =
        // We have to prepend the formData hashCode, because if there is an validation error
        // and the same formData is received again, because the key would be the same, the
        // FormLines will be reused, which is bad, because it won't react like expected anymore.
            // If we have have FormLines with values e.g. TextLine, we can just use their name.
            if (formLineData is ValueFormLineData) formDataHashString + formLineData.name
            // Otherwise we can just use the hash of the data, because it's static anyway.
            else formLineData.hashCode()

        // Because the Composables are cached by their index we need to specify an custom key.
        key(formLineKey) {
            when (formLineData) {
                is AutoSubmitLineData -> AutoSubmitLine(formLineData, viewModel)
                is ParagraphLineData -> ParagraphLine(formLineData, viewModel)
                is DescriptionLineData -> DescriptionLine(formLineData, viewModel)
                is TextLineData -> TextLine(formLineData, viewModel)
                is PasswordLineData -> PasswordLine(formLineData)
                is CaptchaLineData -> CaptchaLine(formLineData)
                is SelectLineData -> SelectLine(formLineData)
                is CheckBoxLineData -> CheckBoxLine(formLineData, viewModel)
                is RadioLineData -> RadioLine(formLineData)
                is ImageLineData -> ImageLine(formLineData)
                is LogoLineData -> LogoLine(formLineData, viewModel)
                is FlickerLineData -> FlickerLine(formLineData)
                is SubmitLineData -> SubmitLine(formLineData, viewModel)
                is AbortLineData -> AbortLine(formLineData, viewModel)
                is RestartLineData -> RestartLine(formLineData, viewModel)
                is TabsLineData -> TabsLine(formLineData, viewModel)
                is RedirectLineData -> RedirectLine(formLineData, viewModel)
                is MultiLineData -> MultiLine(formLineData, viewModel)
                is HiddenLineData -> {
                    /* no-op */
                }
            }
        }
    }
}

@Composable
fun FormLinesContainer(formData: List<FormLineData>, viewModel: XS2AWizardViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        FormLines(formData, viewModel)
    }
}

/**
 * Wrapper for the XS2A-Wizard Compose Component
 */
class XS2AWizard(
    private val xS2AWizardConfig: XS2AWizardConfig
) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                XS2AWizardComponent(xS2AWizardConfig)
            }
        }
    }
}