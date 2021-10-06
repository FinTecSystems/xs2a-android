package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.ValueFormLineData

/**
 * Subclass of [FormLine].
 * Baseclass for all elements with values.
 */
abstract class ValueFormLine : FormLine() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = getThemedInflater(inflater).inflate(R.layout.fragment_form_line_value, container, false)
            .also { inflatedView ->
                inflatedView.findViewById<TextView>(R.id.form_label).text = (getFormData() as ValueFormLineData).label
            }
}