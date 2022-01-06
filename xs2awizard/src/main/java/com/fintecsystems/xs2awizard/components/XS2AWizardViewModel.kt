package com.fintecsystems.xs2awizard.components

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.text.AnnotatedString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fintecsystems.xs2awizard.BuildConfig
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.FormLineData
import com.fintecsystems.xs2awizard.form.FormResponse
import com.fintecsystems.xs2awizard.form.MultiLineData
import com.fintecsystems.xs2awizard.form.ValueFormLineData
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils
import com.fintecsystems.xs2awizard_networking.NetworkingInstance
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.util.*

/**
 * Holds data of the Wizard-Instance and performs all Business-Logic.
 */
class XS2AWizardViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var config: XS2AWizardConfig

    val form = MutableLiveData<List<FormLineData>>()

    val loadingIndicatorLock = MutableLiveData(false)

    val currentWebViewUrl = MutableLiveData<String?>(null)

    private val context: Context
        get() = getApplication<Application>().applicationContext

    var currentStep: XS2AWizardStep? = null
        private set

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
     * Tells the server to go one step back and calls onBack if supplied.
     */
    fun goBack() {
        config.onBack(currentStep)

        submitForm(
            buildJsonObject
            {
                put("action", JsonPrimitive("back"))
            }
        )
    }


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
                onSuccess = ::onFormReceived,
                onError = { config.onNetworkError() }
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
                onSuccess = onSuccess,
                onError = { config.onNetworkError() }
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
        val formResponse = JSONFormatter.formatter.decodeFromString<FormResponse>(jsonString)

        // Check if we're in the right language. If not change it.
        if (Utils.checkIfLanguageNeedsToBeChanged(formResponse.language)) {
            submitForm(
                buildJsonObject {
                    put("action", "change-language")
                    put("language", Locale.getDefault().language)
                }
            )

            return
        }

        form.value = formResponse.form

        loadingIndicatorLock.value = false

        parseCallback(formResponse)
    }

    /**
     * Parses [FormResponse] for possible callbacks and calls onStep if necessary and provided.
     *
     * @param response [FormResponse] to parse.
     */
    private fun parseCallback(response: FormResponse) {
        when (response.callback) {
            "finish" -> config.onFinish(response.callbackParams?.getOrNull(0)?.jsonPrimitive?.content)
            "abort" -> config.onAbort()
            else -> {
                currentStep = XS2AWizardStep.getRelevantStep(response.callback)

                config.onStep(currentStep)
            }
        }

        if (response.error != null) {
            config.onError(
                XS2AWizardError.getRelevantError(
                    response.error,
                    response.isErrorRecoverable ?: false
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
        currentWebViewUrl.value = url
    }

    /**
     * Hides the WebView and shows the form again.
     */
    fun closeWebView() {
        currentWebViewUrl.value = null
    }
}
