package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.RadioLineData
import com.fintecsystems.xs2awizard.helper.Utils
import kotlinx.serialization.json.*

/**
 * Subclass of [ValueFormLine].
 * Radio input field, which renders multiple radio buttons.
 */
class RadioLine : ValueFormLine() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = super.onCreateView(inflater, container, savedInstanceState).also { parentView ->
        val formData = getFormData() as RadioLineData

        val themedInflater = getThemedInflater(inflater)

        val ownView = themedInflater.inflate(R.layout.fragment_line_radio, container, false).also { view ->
            view as RadioGroup

            formData.options.forEach { text ->
                themedInflater.inflate(R.layout.fragment_line_radio_button, container, false).also {
                    it as RadioButton

                    it.id = Utils.generateViewId()

                    if (text is JsonPrimitive) {
                        it.text = text.jsonPrimitive.content
                    } else {
                        it.text = text.jsonObject["label"]?.jsonPrimitive?.content

                        val isEnabled = !(text.jsonObject["disabled"]?.jsonPrimitive?.booleanOrNull ?: false)
                        it.isEnabled = isEnabled
                        it.isClickable = isEnabled
                    }

                    view.addView(it)
                }
            }

            val buttonIndexToCheck = formData.value?.jsonPrimitive?.int ?: 0
            if (buttonIndexToCheck > -1) {
                view.check(view.getChildAt(buttonIndexToCheck).id)
            }

            view.setOnCheckedChangeListener { radioGroup: RadioGroup, selected: Int ->
                formData.value = JsonPrimitive(radioGroup.indexOfChild(radioGroup.findViewById(selected)))
            }
        }

        parentView.findViewById<LinearLayout>(R.id.form_value_container).addView(ownView)
    }
}
