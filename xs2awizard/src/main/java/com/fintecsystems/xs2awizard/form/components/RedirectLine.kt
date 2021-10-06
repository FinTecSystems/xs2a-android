package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.RedirectLineData

/**
 * Subclass of [FormLine].
 * Displays an redirect button and an back button.
 */
class RedirectLine(
) : FormLine() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_redirect, container, false)
        .also { inflatedView ->
            val formData = getFormData() as RedirectLineData

            inflatedView.findViewById<Button>(R.id.redirect_button).also {
                it.text = formData.label
                it.setOnClickListener { actionDelegate.openWebView(formData.url!!) }
            }

            inflatedView.findViewById<Button>(R.id.back_button).also {
                it.text = formData.backLabel
                it.setOnClickListener { actionDelegate.goBack() }
            }
        }
}