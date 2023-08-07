package com.fintecsystems.xs2awizard.components

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.text.AnnotatedString
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.fintecsystems.xs2awizard.BuildConfig
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.networking.ConnectionState
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.helper.*
import com.fintecsystems.xs2awizard_networking.NetworkingInstance
import com.fintecsystems.xs2awizard_networking.registerNetworkCallback
import com.fintecsystems.xs2awizard_networking.unregisterNetworkCallback
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import java.lang.ref.WeakReference
import java.security.KeyStore
import java.util.*

/**
 * Holds data of the Wizard-Instance and performs all Business-Logic.
 */
class XS2AWizardViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {
    var callbackListener: XS2AWizardCallbackListener? = null

    /**
     * Wizard language. If null the device language will be used.
     */
    private var language: XS2AWizardLanguage? = null

    /**
     * If enabled, the form will add an vertical scroll component.
     * Disable this if the wizard is wrapped inside another scrollable view to avoid crashes.
     */
    internal var enableScroll: Boolean = true

    /**
     * If enabled, the back button of the form will be rendered.
     * Only disable this if you need to implement your own back button logic.
     * See [XS2AWizardViewModel.goBack] and [XS2AWizardViewModel.backButtonIsPresent] for your own implementation.
     */
    internal var enableBackButton: Boolean = true

    /**
     * If enabled, all network request will be automatically retried if the device is offline.
     * Otherwise the requests will be directly aborted and the loading indicator hides.
     */
    private var enableAutomaticRetry: Boolean = true

    /**
     * Used for App2App redirection.
     * URL of Host-App to return to.
     */
    private var redirectURL: String? = null

    internal val form = MutableLiveData<List<FormLineData>?>()

    internal val loadingIndicatorLock = MutableLiveData(false)

    internal val currentWebViewUrl = MutableLiveData<String?>(null)

    internal val connectionState = MutableLiveData(ConnectionState.UNKNOWN)

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            connectionState.postValue(ConnectionState.CONNECTED)
        }

        override fun onLost(network: Network) {
            connectionState.postValue(ConnectionState.DISCONNECTED)
        }
    }

    private val context: Context
        get() = getApplication<Application>().applicationContext

    private var currentStep: XS2AWizardStep? = null

    /**
     * Bank-Transport of this session.
     */
    private var provider: String? = null

    private var currentActivity: WeakReference<Activity?> = WeakReference(null)

    private var currentBiometricPromp: BiometricPrompt? = null

    private var currentState: String? = null

    init {
        val xs2aWizardBundle = savedStateHandle.get<Bundle>(XS2AWizardBundleKeys.bundleName)

        if (xs2aWizardBundle != null) {
            currentWebViewUrl.value =
                xs2aWizardBundle.getString(XS2AWizardBundleKeys.currentWebViewUrl)
        }

        savedStateHandle.setSavedStateProvider(XS2AWizardBundleKeys.bundleName) {
            Bundle().apply {
                putString(XS2AWizardBundleKeys.currentWebViewUrl, currentWebViewUrl.value)
            }
        }
    }

    internal fun onStart(
        sessionKey: String,
        backendURL: String?,
        language: XS2AWizardLanguage?,
        enableScroll: Boolean,
        enableBackButton: Boolean,
        enableAutomaticRetry: Boolean,
        redirectURL: String?,
        activity: Activity
    ) {
        this.language = language
        this.enableScroll = enableScroll
        this.enableBackButton = enableBackButton
        this.enableAutomaticRetry = enableAutomaticRetry
        this.redirectURL = redirectURL
        currentActivity = WeakReference(activity)

        NetworkingInstance.getInstance(context).apply {
            this.sessionKey = sessionKey
            this.backendURL = backendURL
        }

        context.registerNetworkCallback(networkCallback)

        initForm()
    }

    internal fun onStop() {
        // Cleanup in case the viewModel gets reused for a future request.
        loadingIndicatorLock.value = false
        currentWebViewUrl.value = null
        form.value = emptyList()
        language = null
        currentStep = null
        enableScroll = true
        enableBackButton = true
        enableAutomaticRetry = true
        redirectURL = null
        currentActivity = WeakReference(null)
        connectionState.value = ConnectionState.UNKNOWN
        context.unregisterNetworkCallback(networkCallback)
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

            if (redirectURL != null) {
                put("location", JsonPrimitive(redirectURL))
            }
        },
        true
    )

    /**
     *  Returns true if a back button is present on the current form.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun backButtonIsPresent() = form.value?.any {
        (it is SubmitLineData && !it.backLabel.isNullOrEmpty())
                || (it is RedirectLineData && !it.backLabel.isNullOrEmpty())
    } ?: false

    /**
     * Returns true, if network requests should abort.
     * This only may return true if [enableAutomaticRetry] is false.
     */
    private fun shouldAbortNetworkRequest() =
        !enableAutomaticRetry && connectionState.value == ConnectionState.DISCONNECTED

    /**
     * Tells the server to go one step back and calls onBack if supplied but only if a back button is present.
     */
    fun goBack() {
        if (!backButtonIsPresent()) return

        callbackListener?.onBack()

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
    internal fun constructJsonBody(action: String, values: JsonObject? = null) = buildJsonObject {
        form.value?.forEach {
            if (it is ValueFormLineData) {
                if (it is CheckBoxLineData && it.value?.jsonPrimitive?.booleanOrNull != true) {
                    return@forEach
                }

                put(
                    it.name,
                    it.value?.jsonPrimitive ?: JsonNull
                )
            }
        }

        put("action", JsonPrimitive(action))

        values?.entries?.forEach {
            put(it.key, it.value.jsonPrimitive.content)
        }
    }

    /**
     * Submits form using standard "submit" action.
     * Will not fire when [enableAutomaticRetry] is not set and device is offline.
     */
    internal fun submitForm(): Unit = submitForm("submit")

    /**
     * Constructs request body and submits form using the specified action.
     * Will not fire when [enableAutomaticRetry] is not set and device is offline.
     *
     * @param action action to use.
     */
    internal fun submitForm(action: String): Unit = submitForm(constructJsonBody(action), true)

    /**
     * Submits form using the specified request body.
     * Will not fire when [enableAutomaticRetry] is not set and device is offline.
     *
     * @param jsonBody request body.
     * @param showIndicator show loading indicator during request.
     */
    internal fun submitForm(jsonBody: JsonElement, showIndicator: Boolean = true) =
        submitForm(jsonBody.toString(), showIndicator)

    /**
     * Submits form using the specified request body.
     * Will not fire when [enableAutomaticRetry] is not set and device is offline.
     *
     * @param jsonBody stringified request body.
     * @param showIndicator show loading indicator during request.
     */
    private fun submitForm(jsonBody: String, showIndicator: Boolean) {
        if (shouldAbortNetworkRequest()) {
            return
        }

        if (showIndicator) {
            loadingIndicatorLock.value = true
        }

        // Cancel and close any open biometric prompt.
        currentBiometricPromp?.cancelAuthentication()

        if (Utils.isMarshmallow && Crypto.isDeviceSecure(context))
            tryToStoreCredentials()

        return NetworkingInstance.getInstance(context)
            .encodeAndSendMessage(
                jsonBody,
                onSuccess = ::onFormReceived,
                onError = {
                    callbackListener?.onNetworkError()
                    loadingIndicatorLock.value = false
                }
            )
    }

    /**
     * Submits form with specified action and calls specified callback on success.
     * Will not fire when [enableAutomaticRetry] is not set and device is offline.
     *
     * @param action action to use.
     * @param onSuccess on success callback to use.
     */
    internal fun submitFormWithCallback(action: String, onSuccess: (String) -> Unit) {
        if (shouldAbortNetworkRequest()) {
            return
        }

        NetworkingInstance.getInstance(context)
            .encodeAndSendMessage(
                constructJsonBody(action).toString(),
                onSuccess = onSuccess,
                onError = {
                    callbackListener?.onNetworkError()
                    loadingIndicatorLock.value = false
                }
            )
    }

    /**
     * Handles onClick of ClickableText's with string annotations.
     *
     * @param annotation clicked annotation
     */
    internal fun handleAnnotationClick(
        activity: Activity,
        annotation: AnnotatedString.Range<String>
    ) {
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
        if (Utils.checkIfLanguageNeedsToBeChanged(
                formResponse.language,
                language
            )
        ) {
            val languageToChangeTo = language ?: XS2AWizardLanguage.fromString(
                Locale.getDefault().language
            )

            submitForm(
                buildJsonObject {
                    put("action", "change-language")
                    put(
                        "language",
                        languageToChangeTo.toString().lowercase()
                    )
                }
            )

            return
        }

        parseCallback(formResponse)

        form.value = formResponse.form

        if (Utils.isMarshmallow && Crypto.isDeviceSecure(context)) {
            tryToAutoFillCredentials()
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
     * Tries to encrypt and store credentials if
     *  - [provider] exists.
     *  - Consent has been checked.
     *  - the phone is secure.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun tryToStoreCredentials() {
        if (form.value == null || provider.isNullOrEmpty()) return

        val consentCheckBoxLineData =
            form.value!!.firstOrNull { it is CheckBoxLineData && it.name == rememberLoginName } as CheckBoxLineData?

        if (consentCheckBoxLineData?.value?.jsonPrimitive?.boolean != true) return

        // We need to keep a reference to the current form, because the new form will be received by then.
        val formCopy = form.value!!

        currentBiometricPromp = Crypto.openBiometricPrompt(
            currentActivity.get() as FragmentActivity,
            context.getString(R.string.save_credentials_prompt_title),
            context.getString(R.string.save_credentials_prompt_description),
            context.getString(R.string.cancel),
            BiometricManager.Authenticators.BIOMETRIC_STRONG,
            {
                storeCredentials(formCopy)
            },
            { _, _ -> /* no-op */ }
        )
    }

    /**
     * Saves credentials to the store.
     *
     * @param form - Form to save.
     */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun storeCredentials(form: List<FormLineData>) {
        storeProvider(provider!!)

        Crypto.createEncryptedSharedPreferences(
            context,
            sharedPreferencesFileName,
            Crypto.createMasterKey(context, masterKeyAlias)
        ).edit().apply {
            form.forEach {
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

        currentBiometricPromp = Crypto.openBiometricPrompt(
            currentActivity.get() as FragmentActivity,
            context.getString(R.string.fill_credentials_prompt_title),
            context.getString(R.string.fill_credentials_prompt_description),
            context.getString(R.string.cancel),
            BiometricManager.Authenticators.BIOMETRIC_STRONG,
            {
                autoFillCredentials()
            },
            { _, _ -> /* no-op */ }
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
     * Parses [FormResponse] for possible callbacks and calls onStep if necessary and provided.
     *
     * @param response [FormResponse] to parse.
     */
    private fun parseCallback(response: FormResponse) {
        currentState = response.step

        when (response.callback) {
            "finish" -> callbackListener?.onFinish((response.callbackParams?.getOrNull(0)?.jsonPrimitive?.content))
            "abort" -> callbackListener?.onAbort()
            else -> {
                currentStep = XS2AWizardStep.getRelevantStep(response.callback)

                if (currentStep != null)
                    callbackListener?.onStep((currentStep!!))
            }
        }

        response.callbackParams?.first().let {
            if (it is JsonObject && it.containsKey("provider")) {
                provider = (it["provider"] as JsonPrimitive).content
            }
        }

        if (response.error != null) {
            callbackListener?.onError(
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
    internal fun openWebView(url: String) {
        currentWebViewUrl.value = url
    }

    /**
     * Hides the WebView and shows the form again.
     */
    internal fun closeWebView() {
        currentWebViewUrl.value = null
    }

    /**
     * Checks if the current form is the bank search.
     */
    @Suppress("unused")
    fun isBankSearch() = currentState == "bank"

    /**
     * Checks if the current form is the first login screen.
     */
    @Suppress("unused")
    fun isLogin() = currentState == "login"

    companion object {
        private const val rememberLoginName = "store_credentials"
        private const val sharedPreferencesFileName = "xs2a_credentials"
        private const val storedProvidersKey = "providers"
        private const val masterKeyAlias = "xs2a_credentials_master_key"

        /**
         * Delete all saved credentials.
         *
         * @param context - Context to use.
         */
        @Suppress("unused")
        fun clearCredentials(context: Context) {
            context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE).edit()
                .apply {
                    clear()
                    apply()
                }

            KeyStore.getInstance("AndroidKeyStore").apply {
                load(null)
                deleteEntry(masterKeyAlias)
            }
        }
    }
}
