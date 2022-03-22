package com.fintecsystems.xs2awizard.wrappers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig

class ConfigHolder() : ViewModel() {
    var xS2AWizardConfig: XS2AWizardConfig? = null
}

/**
 * Wrapper for the XS2A-Wizard Compose Component
 */
class XS2AWizardFragment() : Fragment() {
    private var mConfig: XS2AWizardConfig? = null

    private val configHolder: ConfigHolder by viewModels()

    constructor(_xS2AWizardConfig: XS2AWizardConfig) : this() {
        mConfig = _xS2AWizardConfig
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mConfig != null)
            configHolder.xS2AWizardConfig = mConfig
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            Log.d("XS2AWizard", "onCreateView: ${configHolder.xS2AWizardConfig}")

            setContent {
                if (configHolder.xS2AWizardConfig != null)
                    XS2AWizard(xS2AWizardConfig = configHolder.xS2AWizardConfig!!)
            }
        }
    }
}