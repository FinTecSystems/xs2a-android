package com.fintecsystems.xs2awizard.form.components.textLine

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.ValueFormLine
import com.fintecsystems.xs2awizard.helper.Utils
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Subclass of [ValueFormLine].
 * Simple TextInput field.
 */
class TextLine : ValueFormLine(), TextWatcher {
    private var autoCompleteAdapter: TextLineAutoCompleteAdapter? = null

    // Runnable and Handler used for auto-completion.
    private var autoCompleteTask: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = super.onCreateView(inflater, container, savedInstanceState).also { parentView ->
        val formData = getFormData() as TextLineData

        val ownView =
            getThemedInflater(inflater).inflate(R.layout.fragment_line_text, container, false)
                .also { view ->
                    view.findViewById<AppCompatAutoCompleteTextView>(R.id.text_input_edit).let {
                        it.setText(formData.value?.jsonPrimitive?.content)
                        it.hint = formData.placeholder
                        it.isEnabled = !(formData.disabled ?: false)

                        if ((formData.maxLength ?: 0) > 0) {
                            // Set max. length filter
                            it.filters = arrayOf(InputFilter.LengthFilter(formData.maxLength!!))
                        }

                        it.inputType = getInputType()

                        if (formData.autoCompleteAction != null) {
                            autoCompleteAdapter =
                                TextLineAutoCompleteAdapter(
                                    Utils.getThemedContext(
                                        requireContext(),
                                        styleIdModel.liveData.value
                                    ), emptyList()
                                )
                            it.setAdapter(autoCompleteAdapter)
                        }

                        it.addTextChangedListener(this)
                    }
                }

        parentView.findViewById<LinearLayout>(R.id.form_value_container).addView(ownView)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (autoCompleteTask != null) {
            handler.removeCallbacks(autoCompleteTask!!)
        }
    }

    /**
     * Return the appropriate input type according to the data.
     *
     * @return the input type.
     */
    private fun getInputType() = when ((getFormData() as TextLineData).overrideType) {
        // TODO: month, search
        "number" -> InputType.TYPE_CLASS_NUMBER
        "email" -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        "tel" -> InputType.TYPE_CLASS_PHONE
        "date" -> InputType.TYPE_DATETIME_VARIATION_DATE
        "datetime" -> InputType.TYPE_CLASS_DATETIME
        else -> InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable?) {}

    /**
     * Callback for auto-complete requests.
     */
    private fun onAutoComplete(autoCompleteResponse: AutoCompleteResponse) {
        autoCompleteAdapter?.setItems(autoCompleteResponse.autoCompleteData.data)
    }

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        val formData = getFormData() as TextLineData

        formData.value = JsonPrimitive(s.toString())

        if (formData.autoCompleteAction != null && (s?.length ?: 0) > 1) {
            if (autoCompleteTask != null) {
                handler.removeCallbacks(autoCompleteTask!!)
            }

            autoCompleteTask = Runnable {
                actionDelegate.submitFormWithCallback(formData.autoCompleteAction, ::onAutoComplete)
            }

            handler.postDelayed(autoCompleteTask!!, 300L)
        }
    }
}
