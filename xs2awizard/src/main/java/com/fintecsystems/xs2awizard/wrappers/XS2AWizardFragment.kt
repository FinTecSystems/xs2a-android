package com.fintecsystems.xs2awizard.wrappers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.*
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

/**
 * Wrapper for the XS2A-Wizard Compose Component
 */
class XS2AWizardFragment() : Fragment(), XS2AWizardEventListener {
    @Suppress("unused")
    constructor(
        sessionKey: String,
        backendURL: String? = null,
        theme: XS2ATheme? = null,
        language: XS2AWizardLanguage? = null,
        enableScroll: Boolean = true,
        enableBackButton: Boolean = true,
        enableAutomaticRetry: Boolean = true
    ) : this() {
        arguments = Bundle().apply {
            putString(XS2AWizardBundleKeys.sessionKey, sessionKey)
            putString(XS2AWizardBundleKeys.backendURL, backendURL)

            putParcelable(XS2AWizardBundleKeys.theme, theme)
            putSerializable(XS2AWizardBundleKeys.language, language)

            putBoolean(XS2AWizardBundleKeys.enableScroll, enableScroll)
            putBoolean(XS2AWizardBundleKeys.enableBackButton, enableBackButton)
            putBoolean(XS2AWizardBundleKeys.enableAutomaticRetry, enableAutomaticRetry)
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

                    XS2AWizard(
                        sessionKey = arguments.getString(XS2AWizardBundleKeys.sessionKey)!!,
                        backendURL = arguments.getString(XS2AWizardBundleKeys.backendURL),
                        theme = arguments.getParcelable(XS2AWizardBundleKeys.theme),
                        language = arguments.getSerializable(XS2AWizardBundleKeys.language) as? XS2AWizardLanguage,
                        enableScroll = arguments.getBoolean(XS2AWizardBundleKeys.enableScroll),
                        enableBackButton = arguments.getBoolean(XS2AWizardBundleKeys.enableBackButton),
                        enableAutomaticRetry = arguments.getBoolean(XS2AWizardBundleKeys.enableAutomaticRetry),
                        eventListener = this@XS2AWizardFragment
                    )
                }
            }
        }
    }

    override fun onFinish(credentials: String?) {
        parentFragmentManager.setFragmentResult(onFinishKey, Bundle().apply {
            putString(onFinishArgumentKey, credentials)
        })
    }

    override fun onAbort() {
        parentFragmentManager.setFragmentResult(onAbortKey, Bundle.EMPTY)
    }

    override fun onError(xs2aWizardError: XS2AWizardError) {
        parentFragmentManager.setFragmentResult(onErrorKey, Bundle().apply {
            putSerializable(onErrorArgumentKey, xs2aWizardError)
        })
    }

    override fun onNetworkError() {
        parentFragmentManager.setFragmentResult(onNetworkErrorKey, Bundle.EMPTY)
    }

    override fun onStep(newStep: XS2AWizardStep) {
        parentFragmentManager.setFragmentResult(onStepKey, Bundle().apply {
            putSerializable(onStepArgumentKey, newStep)
        })
    }

    override fun onBack() {
        parentFragmentManager.setFragmentResult(onBackKey, Bundle.EMPTY)
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