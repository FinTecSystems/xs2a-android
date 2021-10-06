package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.PasswordLineData
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.json.JsonPrimitive

/**
 * Subclass of [ValueFormLine].
 * Simple password field.
 */
class PasswordLine : ValueFormLine(), TextWatcher {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = super.onCreateView(inflater, container, savedInstanceState).also { parentView ->

        val ownView = getThemedInflater(inflater).inflate(R.layout.fragment_line_password, container, false).also { view ->
            view.findViewById<TextInputEditText>(R.id.password_text_input_edit).let {
                it.hint = (getFormData() as PasswordLineData).placeholder

                it.addTextChangedListener(this)
            }
        }

        parentView.findViewById<LinearLayout>(R.id.form_value_container).addView(ownView)
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        (getFormData() as PasswordLineData).value = JsonPrimitive(s.toString())
    }
}
