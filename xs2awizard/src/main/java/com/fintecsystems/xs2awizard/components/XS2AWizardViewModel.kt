package com.fintecsystems.xs2awizard.components

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.text.AnnotatedString
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.fintecsystems.xs2awizard.BuildConfig
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.helper.Crypto
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils
import com.fintecsystems.xs2awizard_networking.NetworkingInstance
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.lang.ref.WeakReference
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

    /**
     * Bank-Transport of this session.
     */
    private var provider: String? = null

    private var currentActivity: WeakReference<Activity?> = WeakReference(null)

    fun onStart(_config: XS2AWizardConfig, activity: Activity) {
        config = _config
        currentActivity = WeakReference(activity)

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
        currentStep = null
        currentActivity = WeakReference(null)
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
        config.onBack()

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

        if (Utils.isMarshmallow && Crypto.isDeviceSecure(context))
            storeCredentials()

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

        parseCallback(formResponse)


        if (Utils.isMarshmallow && Crypto.isDeviceSecure(context)) {
            form.value = parseFormForCredentials(formResponse.form)
            tryToAutoFillCredentials()
        } else {
            form.value = formResponse.form
        }

        loadingIndicatorLock.value = false
    }

    /**
     * Checks if specified provider is in store.
     *
     * @param provider - Provider to check.
     *
     * @return true if exists.
     */
    private fun isProviderInStore(provider: String?) =
        context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
            .getStringSet(storedProvidersKey, null)?.contains(provider) == true

    /**
     * Saves provider into store if it's not existing.
     *
     * @param provider - Provider to store.
     */
    private fun storeProvider(provider: String) {
        context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE).apply {
            getStringSet(storedProvidersKey, null).let {
                // Copy store providers
                val providers = mutableSetOf<String>()
                if (it != null) providers.addAll(providers)

                providers.add(provider)

                edit()
                    .putStringSet(storedProvidersKey, providers)
                    .apply()
            }
        }
    }

    /**
     * Encrypts and stores credentials if
     *  - [provider] exists.
     *  - Consent has been checked.
     *  - the phone is secure.
     * Aborts saving.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun storeCredentials() {
        if (form.value == null || provider.isNullOrEmpty()) return

        val consentCheckBoxLineData =
            form.value!!.firstOrNull { it is CheckBoxLineData && it.name == rememberLoginName } as CheckBoxLineData?

        if (consentCheckBoxLineData?.value?.jsonPrimitive?.boolean != true) return

        storeProvider(provider!!)

        Crypto.createEncryptedSharedPreferences(
            context,
            sharedPreferencesFileName,
            Crypto.createMasterKey(context, masterKeyAlias)
        ).edit().apply {
            form.value!!.forEach {
                if (it is CredentialFormLineData && it.isLoginCredential == true) {
                    if (it is CheckBoxLineData) putBoolean(
                        it.getProviderName(provider!!),
                        it.value?.jsonPrimitive?.boolean ?: false
                    )
                    else putString(
                        it.getProviderName(provider!!),
                        it.value?.jsonPrimitive?.content ?: ""
                    )
                }
            }

            apply()
        }
    }

    /**
     * Tries to AutoFill stored credentials if:
     *  - [provider] exists.
     *  - The Phone is secure.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun tryToAutoFillCredentials() {
        if (form.value == null || form.value?.none { it is CredentialFormLineData && it.isLoginCredential == true } == true) return

        if (provider.isNullOrEmpty() || !isProviderInStore(provider)) return

        Crypto.openBiometricPrompt(
            currentActivity.get() as FragmentActivity,
            "Unlock?",
            "Unlock device using fingerprint?",
            "no",
            BiometricManager.Authenticators.BIOMETRIC_STRONG,
            {
                Log.d("XS2AWizard", "onAuthenticationSucceeded: ${it.authenticationType}")

                autoFillCredentials()
            }, { errorCode, errString ->
                Log.d("XS2AWizard", "onAuthenticationError: $errorCode $errString")
            }
        )
    }

    /**
     * AutoFills credentials from the store.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun autoFillCredentials() {
        val sharedPreferences = Crypto.createEncryptedSharedPreferences(
            context,
            sharedPreferencesFileName,
            Crypto.createMasterKey(context, masterKeyAlias)
        )

        form.value!!.forEach {
            if (it is CredentialFormLineData && it.isLoginCredential == true) {
                val key = it.getProviderName(provider!!)
                if (it is CheckBoxLineData) {
                    if (sharedPreferences.contains(key)) it.value =
                        JsonPrimitive(sharedPreferences.getBoolean(key, false))
                } else {
                    if (sharedPreferences.contains(key)) it.value =
                        JsonPrimitive(sharedPreferences.getString(key, ""))
                }
            }
        }

        submitForm()
    }

    /**
     * Parses the Form for Login-Credentials.
     * If any exists an additional Checkbox will be added to ask for saving.
     *
     * @param form Form to parse.
     *
     * @return Parsed Form.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun parseFormForCredentials(form: List<FormLineData>?): List<FormLineData>? {
        if (Crypto.isDeviceSecure(context) && form?.any { it is CredentialFormLineData && it.isLoginCredential == true } == true) {
            val submitLineIndex = form.indexOfFirst { it is SubmitLineData }

            if (submitLineIndex > -1) {
                return form.toMutableList().also {
                    it.add(
                        submitLineIndex, CheckBoxLineData(
                            name = rememberLoginName,
                            label = context.getString(R.string.remember_login),
                            isLoginCredential = false,
                            value = JsonPrimitive(false),
                            disabled = false,
                        )
                    )
                }
            }
        }

        return form
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

        response.callbackParams?.first().let {
            if (it is JsonObject && it.containsKey("provider")) {
                provider = (it["provider"] as JsonPrimitive).content
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

    companion object {
        private const val rememberLoginName = "remember_login"
        private const val sharedPreferencesFileName = "xs2a_credentials"
        private const val storedProvidersKey = "providers"
        private const val masterKeyAlias = "xs2a_credentials_master_key"

        /**
         * Function to delete all saved credentials.
         *
         * @param context - Context to use.
         */
        fun clearCredentials(context: Context) {
            context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE).edit()
                .apply {
                    clear()
                    apply()
                }
        }
    }
}
