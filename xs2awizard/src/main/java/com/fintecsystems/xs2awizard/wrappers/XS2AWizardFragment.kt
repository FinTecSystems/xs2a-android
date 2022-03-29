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
class XS2AWizardFragment() : Fragment() {
    private var mConfig: XS2AWizardConfig? = null

    constructor(_xS2AWizardConfig: XS2AWizardConfig) : this() {
        mConfig = _xS2AWizardConfig
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                if (mConfig != null)
                    XS2AWizard(xS2AWizardConfig = mConfig!!)
                else
                    XS2AWizard()
            }
        }
    }
}