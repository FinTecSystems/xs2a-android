package com.fintecsystems.xs2awizard.form.components.selectLine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.SelectLineData
import com.fintecsystems.xs2awizard.form.components.ValueFormLine
import com.fintecsystems.xs2awizard.helper.Utils
import kotlinx.serialization.json.*

/**
 * Subclass of [ValueFormLine].
 * Simple Select field.
 */
class SelectLine : ValueFormLine(), OnItemSelectedListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = super.onCreateView(inflater, container, savedInstanceState).also { parentView ->
        val formData = getFormData() as SelectLineData

        val ownView =
            getThemedInflater(inflater).inflate(R.layout.fragment_line_select, container, false)
                .also { view ->
                    val spinner = view.findViewById<Spinner>(R.id.select_input)

                    val themedContext =
                        Utils.getThemedContext(requireContext(), styleIdModel.liveData.value)

                    // If we receive an object we need to use the custom adapter implementation,
                    // otherwise we can just use an normal ArrayAdapter
                    spinner.adapter = when (formData.options) {
                        is JsonObject -> SelectLineSpinnerAdapter(
                            themedContext,
                            formData.options.jsonObject.toMap()
                                .mapValues { it.value.jsonPrimitive.content }
                        )
                        is JsonArray -> ArrayAdapter(
                            themedContext,
                            R.layout.fragment_line_select_item,
                            formData.options.jsonArray.map { it.jsonPrimitive.content })
                        else -> throw IllegalArgumentException()
                    }

                    if (!formData.value?.jsonPrimitive?.content.isNullOrEmpty()) {
                        spinner.setSelection(getCurrentKeyIndex()!!)
                    }

                    spinner.onItemSelectedListener = this
                }

        parentView.findViewById<LinearLayout>(R.id.form_value_container).addView(ownView)
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        val formData = getFormData() as SelectLineData

        formData.value = when (formData.options) {
            is JsonObject -> JsonPrimitive(formData.options.keys.elementAt(position))
            is JsonArray -> JsonPrimitive(position.toString())
            else -> throw IllegalArgumentException()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    /**
     * @return the index of the currently selected element.
     */
    private fun getCurrentKeyIndex() = (getFormData() as SelectLineData).let {
        when (it.options) {
            is JsonObject -> it.options.keys.indexOf(it.value?.jsonPrimitive?.content)
            is JsonArray -> it.value?.jsonPrimitive?.int
            else -> throw IllegalArgumentException()
        }
    }
}
