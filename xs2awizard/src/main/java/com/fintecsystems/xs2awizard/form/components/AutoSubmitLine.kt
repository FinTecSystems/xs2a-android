package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import com.fintecsystems.xs2awizard.form.AutoSubmitLineData

/**
 * Subclass of [FormLine].
 * Auto-submit holder
 */
class AutoSubmitLine : FormLine() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                actionDelegate.triggerAutoSubmit((getFormData() as AutoSubmitLineData).interval.toLong())
        }
}