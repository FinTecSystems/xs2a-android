package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.DescriptionLineData

/**
 * Subclass of [FormLine].
 * Displays small description text
 */
class DescriptionLine : FormLine() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_description, container, false)
            .also { inflatedView ->
                actionDelegate.parseMarkupText(
                    inflatedView.findViewById(R.id.description_text),
                    (getFormData() as DescriptionLineData).text,
                )
            }
}