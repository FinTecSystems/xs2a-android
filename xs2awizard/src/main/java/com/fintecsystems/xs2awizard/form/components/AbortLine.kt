package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.AbortLineData

/**
 * Subclass of [FormLine].
 * Displays abort button
 */
class AbortLine: FormLine() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_abort, container, false)
            .also { inflatedView ->
                inflatedView.findViewById<Button>(R.id.abort_button).also {
                    it.text = (getFormData() as AbortLineData).label
                    it.setOnClickListener { actionDelegate.abort() }
                }
            }
}