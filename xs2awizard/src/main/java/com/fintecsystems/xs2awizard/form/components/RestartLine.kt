package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.RestartLineData

/**
 * Subclass of [FormLine].
 * Displays restart button
 */
class RestartLine : FormLine() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_restart, container, false)
            .also { inflatedView ->
                inflatedView.findViewById<Button>(R.id.restart_button).also {
                    it.text = (getFormData() as RestartLineData).label
                    it.setOnClickListener { actionDelegate.restart() }
                }
            }
}
