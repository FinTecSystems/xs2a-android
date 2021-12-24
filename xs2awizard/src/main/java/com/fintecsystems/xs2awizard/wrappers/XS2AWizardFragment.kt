package com.fintecsystems.xs2awizard.wrappers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig

/**
 * Wrapper for the XS2A-Wizard Compose Component
 */
class XS2AWizardFragment(
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
                XS2AWizard(xS2AWizardConfig = xS2AWizardConfig)
            }
        }
    }
}