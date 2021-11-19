package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.CaptchaLineData
import com.fintecsystems.xs2awizard.helper.Utils
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.json.JsonPrimitive

/**
 * Subclass of [ValueFormLine].
 * Simple captcha field.
 */
class CaptchaLine : ValueFormLine(), TextWatcher {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = super.onCreateView(inflater, container, savedInstanceState).also { parentView ->

        val ownView = getThemedInflater(inflater).inflate(R.layout.fragment_line_captcha, container, false).also { view ->
            val formData = getFormData() as CaptchaLineData

            view.findViewById<TextInputEditText>(R.id.captcha_text_input_edit).let {
                it.hint = formData.placeholder

                it.addTextChangedListener(this)
            }

            view.findViewById<ImageView>(R.id.captcha_image_view)
                .setImageBitmap(Utils.decodeBase64Image(formData.data))
        }

        parentView.findViewById<LinearLayout>(R.id.form_value_container).addView(ownView)
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        (getFormData() as CaptchaLineData).value = JsonPrimitive(s.toString())
    }
}
