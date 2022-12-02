package com.fintecsystems.xs2awizard.wrappers

import android.os.Bundle
import android.util.Log
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
import com.fintecsystems.xs2awizard.components.theme.IXS2ATheme

/**
 * Wrapper for the XS2A-Wizard Compose Component
 */
class XS2AWizardFragment() : Fragment(), XS2AWizardEventListener {
    @Suppress("unused")
    constructor(
        sessionKey: String,
        backendURL: String? = null,
        theme: IXS2ATheme? = null,
        language: XS2AWizardLanguage? = null,
        enableScroll: Boolean = true,
        enableBackButton: Boolean = true,
        enableAutomaticRetry: Boolean = true
    ) : this() {
        arguments = Bundle().apply {
            putString(XS2AWizardBundleKeys.sessionKey, sessionKey)
            putString(XS2AWizardBundleKeys.backendURL, backendURL)

            putSerializable(XS2AWizardBundleKeys.theme, theme)
            putSerializable(XS2AWizardBundleKeys.language, language)

            putBoolean(XS2AWizardBundleKeys.enableScroll, enableScroll)
            putBoolean(XS2AWizardBundleKeys.enableBackButton, enableBackButton)
            putBoolean(XS2AWizardBundleKeys.enableAutomaticRetry, enableAutomaticRetry)
        }

        Log.d("XS2AWizard", "init: $arguments")
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
                    Log.d("XS2AWizard", "compose: $arguments")
                    Log.d("XS2AWizard", "compose: ${arguments?.getString("sessionKey")}")

                    val arguments = requireArguments()

                    assert(arguments.containsKey("sessionKey"))

                    XS2AWizard(
                        sessionKey = arguments.getString(XS2AWizardBundleKeys.sessionKey)!!,
                        backendURL = arguments.getString(XS2AWizardBundleKeys.backendURL),
                        theme = arguments.getSerializable(XS2AWizardBundleKeys.theme) as? IXS2ATheme,
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
        Log.d("XS2AWizard", "onFinish: $credentials")
    }

    override fun onAbort() {
        Log.d("XS2AWizard", "onAbort")
    }

    override fun onError(xs2aWizardError: XS2AWizardError) {
        Log.d("XS2AWizard", "onError: $xs2aWizardError")
    }

    override fun onNetworkError() {
        Log.d("XS2AWizard", "onNetworkError")
    }

    override fun onStep(newStep: XS2AWizardStep) {
        Log.d("XS2AWizard", "onStep: $newStep")
    }

    override fun onBack() {
        Log.d("XS2AWizard", "onBack")
    }
}