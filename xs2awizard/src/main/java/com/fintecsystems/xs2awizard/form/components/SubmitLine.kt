package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.SubmitLineData

/**
 * Subclass of [FormLine].
 * Displays an submit button and if existing an abort button.
 */
class SubmitLine : FormLine() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_submit, container, false)
            .also { inflatedView ->
                val formData = getFormData() as SubmitLineData

                inflatedView.findViewById<Button>(R.id.submit_button).also {
                    it.text = formData.label
                    it.setOnClickListener { actionDelegate.submitForm() }
                }

                inflatedView.findViewById<Button>(R.id.back_button).also {
                    if (formData.backLabel == null) {
                        it.visibility = View.GONE
                    } else {
                        it.text = formData.backLabel
                        it.setOnClickListener { actionDelegate.goBack() }
                    }
                }
            }
}