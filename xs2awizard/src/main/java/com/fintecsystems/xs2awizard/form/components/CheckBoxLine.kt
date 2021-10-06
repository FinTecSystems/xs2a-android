package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

/**
 * Subclass of [ValueFormLine].
 * Simple selectable box.
 */
class CheckBoxLine : ValueFormLine() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_checkbox, container, false)
        .also { view ->
            val formData = getFormData() as CheckBoxLineData

            view.findViewById<SwitchCompat>(R.id.checkbox_switch).let {
                it.isChecked = formData.value?.jsonPrimitive?.boolean ?: false

                it.isEnabled = !(formData.disabled ?: false)
                it.isClickable = !(formData.disabled ?: false)

                it.setOnClickListener { switchView ->
                    formData.value = JsonPrimitive((switchView as SwitchCompat).isChecked)
                }
            }

            actionDelegate.parseMarkupText(
                view.findViewById(R.id.checkbox_text),
                formData.label,
            )
        }
}
