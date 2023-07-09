package com.fintecsystems.xs2awizard.wrappers

import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.FragmentResultOwner
import androidx.lifecycle.LifecycleOwner
import com.fintecsystems.xs2awizard.components.XS2AWizardError
import com.fintecsystems.xs2awizard.components.XS2AWizardCallbackListener
import com.fintecsystems.xs2awizard.components.XS2AWizardStep

/**
 * Convenience method to register to all XS2AWizard events and bind them to a XS2AWizardEventListener instance.
 */
@Suppress("unused")
fun FragmentResultOwner.setXs2aCallbacks(
    lifecycleOwner: LifecycleOwner,
    xS2AWizardCallbackListener: XS2AWizardCallbackListener
) {
    val fragmentResultListener = FragmentResultListener { requestKey, result ->
        when (requestKey) {
            XS2AWizardFragment.onFinishKey -> xS2AWizardCallbackListener.onFinish(
                result.getString(
                    XS2AWizardFragment.onFinishArgumentKey
                )
            )
            XS2AWizardFragment.onAbortKey -> xS2AWizardCallbackListener.onAbort()
            XS2AWizardFragment.onErrorKey -> xS2AWizardCallbackListener.onError(
                result.getSerializable(
                    XS2AWizardFragment.onErrorArgumentKey
                ) as XS2AWizardError
            )
            XS2AWizardFragment.onNetworkErrorKey -> xS2AWizardCallbackListener.onNetworkError()
            XS2AWizardFragment.onStepKey -> xS2AWizardCallbackListener.onStep(
                result.getSerializable(
                    XS2AWizardFragment.onStepArgumentKey
                ) as XS2AWizardStep
            )
            XS2AWizardFragment.onBackKey -> xS2AWizardCallbackListener.onBack()
        }
    }

    setFragmentResultListener(
        XS2AWizardFragment.onFinishKey,
        lifecycleOwner,
        fragmentResultListener
    )
    setFragmentResultListener(XS2AWizardFragment.onAbortKey, lifecycleOwner, fragmentResultListener)
    setFragmentResultListener(XS2AWizardFragment.onErrorKey, lifecycleOwner, fragmentResultListener)
    setFragmentResultListener(
        XS2AWizardFragment.onNetworkErrorKey,
        lifecycleOwner,
        fragmentResultListener
    )
    setFragmentResultListener(XS2AWizardFragment.onStepKey, lifecycleOwner, fragmentResultListener)
    setFragmentResultListener(XS2AWizardFragment.onBackKey, lifecycleOwner, fragmentResultListener)
}

/**
 * Convenience method to clear all XS2AWizard events.
 */
@Suppress("unused")
fun FragmentResultOwner.clearXs2aCallbacks() {
    clearFragmentResultListener(XS2AWizardFragment.onFinishKey)
    clearFragmentResultListener(XS2AWizardFragment.onAbortKey)
    clearFragmentResultListener(XS2AWizardFragment.onErrorKey)
    clearFragmentResultListener(XS2AWizardFragment.onNetworkErrorKey)
    clearFragmentResultListener(XS2AWizardFragment.onStepKey)
    clearFragmentResultListener(XS2AWizardFragment.onBackKey)
}