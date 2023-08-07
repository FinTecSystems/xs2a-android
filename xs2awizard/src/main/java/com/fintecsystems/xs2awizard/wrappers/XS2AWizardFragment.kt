package com.fintecsystems.xs2awizard.wrappers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.*
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

/**
 * Wrapper for the XS2A-Wizard Compose Component
 */
class XS2AWizardFragment() : Fragment(), XS2AWizardCallbackListener {
    /**
     * Renders the XS2A-Wizard.
     *
     * @param sessionKey - Session key used by the wizard.
     * @param backendURL - Optional URL to target a different backend.
     * @param theme - Theme to be used.
     *                If null the default Light- or Dark-Theme, depending on the device settings, is used.
     * @param fontResId - Custom typography to be used by all form elements.
     * @param language - Specifies the wizard language.
     *                   Defaults to the device language if supported, [XS2AWizardLanguage.EN] otherwise.
     * @param enableScroll - If true the form container allows for automatic scrolling.
     *                       Disable if you need to implement nested scrolling.
     * @param enableBackButton - If true renders back button on some forms.
     *                           Disable this only if you implement your own back-handling-logic.
     *                           The user needs to have a way to "go-back".
     * @param enableAutomaticRetry - If true requests to the backend will be retried if the device is offline and goes online again.
     *                               This also means that the loading indicator will stay during that time.
     * @param redirectDeepLink - Deep Link of Host-App used for returning App2App redirection.
     *                           Must match your scheme and host declared in your AndroidManifest.
     *                           e.g "<scheme>://<host>".
     */
    @Suppress("unused")
    constructor(
        sessionKey: String,
        backendURL: String? = null,
        theme: XS2ATheme? = null,
        fontResId: Int? = null,
        language: XS2AWizardLanguage? = null,
        enableScroll: Boolean = true,
        enableBackButton: Boolean = true,
        enableAutomaticRetry: Boolean = true,
        redirectDeepLink: String? = null
    ) : this() {
        arguments = Bundle().apply {
            putString(XS2AWizardBundleKeys.sessionKey, sessionKey)
            putString(XS2AWizardBundleKeys.backendURL, backendURL)

            putParcelable(XS2AWizardBundleKeys.theme, theme)
            putSerializable(XS2AWizardBundleKeys.language, language)

            putBoolean(XS2AWizardBundleKeys.enableScroll, enableScroll)
            putBoolean(XS2AWizardBundleKeys.enableBackButton, enableBackButton)
            putBoolean(XS2AWizardBundleKeys.enableAutomaticRetry, enableAutomaticRetry)
            putString(XS2AWizardBundleKeys.redirectDeepLink, redirectDeepLink)

            if (fontResId != null) {
                putInt(XS2AWizardBundleKeys.fontResId, fontResId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                CompositionLocalProvider(
                    LocalViewModelStoreOwner provides (activity as ViewModelStoreOwner)
                ) {
                    val arguments = requireArguments()

                    assert(arguments.containsKey("sessionKey"))

                    val typography = if (arguments.containsKey(XS2AWizardBundleKeys.fontResId))
                        Typography(
                            defaultFontFamily = FontFamily(
                                Font(
                                    arguments.getInt(
                                        XS2AWizardBundleKeys.fontResId
                                    )
                                )
                            )
                        )
                    else
                        MaterialTheme.typography


                    XS2AWizard(
                        sessionKey = arguments.getString(XS2AWizardBundleKeys.sessionKey)!!,
                        backendURL = arguments.getString(XS2AWizardBundleKeys.backendURL),
                        theme = arguments.getParcelable(XS2AWizardBundleKeys.theme),
                        typography = typography,
                        language = arguments.getSerializable(XS2AWizardBundleKeys.language) as? XS2AWizardLanguage,
                        enableScroll = arguments.getBoolean(XS2AWizardBundleKeys.enableScroll),
                        enableBackButton = arguments.getBoolean(XS2AWizardBundleKeys.enableBackButton),
                        enableAutomaticRetry = arguments.getBoolean(XS2AWizardBundleKeys.enableAutomaticRetry),
                        callbackListener = this@XS2AWizardFragment,
                        redirectDeepLink = arguments.getString(XS2AWizardBundleKeys.redirectDeepLink)
                    )
                }
            }
        }
    }

    override fun onFinish(credentials: String?) {
        try {
            parentFragmentManager.setFragmentResult(onFinishKey, Bundle().apply {
                putString(onFinishArgumentKey, credentials)
            })
        } catch (_: IllegalStateException) {
            /* no-op */
        }
    }

    override fun onAbort() {
        try {
            parentFragmentManager.setFragmentResult(onAbortKey, Bundle.EMPTY)
        } catch (_: IllegalStateException) {
            /* no-op */
        }
    }

    override fun onError(xs2aWizardError: XS2AWizardError) {
        try {
            parentFragmentManager.setFragmentResult(onErrorKey, Bundle().apply {
                putSerializable(onErrorArgumentKey, xs2aWizardError)
            })
        } catch (_: IllegalStateException) {
            /* no-op */
        }
    }

    override fun onNetworkError() {
        try {
            parentFragmentManager.setFragmentResult(onNetworkErrorKey, Bundle.EMPTY)
        } catch (_: IllegalStateException) {
            /* no-op */
        }
    }

    override fun onStep(newStep: XS2AWizardStep) {
        try {
            parentFragmentManager.setFragmentResult(onStepKey, Bundle().apply {
                putSerializable(onStepArgumentKey, newStep)
            })
        } catch (_: IllegalStateException) {
            /* no-op */
        }
    }

    override fun onBack() {
        try {
            parentFragmentManager.setFragmentResult(onBackKey, Bundle.EMPTY)
        } catch (_: IllegalStateException) {
            /* no-op */
        }
    }

    companion object {
        const val onFinishKey = "onFinish"
        const val onFinishArgumentKey = "credentials"
        const val onAbortKey = "onAbort"
        const val onErrorKey = "onError"
        const val onErrorArgumentKey = "error"
        const val onNetworkErrorKey = "onNetworkError"
        const val onStepKey = "onStep"
        const val onStepArgumentKey = "step"
        const val onBackKey = "onBack"
    }
}