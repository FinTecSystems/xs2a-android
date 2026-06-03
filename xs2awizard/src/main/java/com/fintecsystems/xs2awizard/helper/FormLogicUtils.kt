package com.fintecsystems.xs2awizard.helper

import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.form.FormLineData
import com.fintecsystems.xs2awizard.form.ParagraphLineData
import com.fintecsystems.xs2awizard.form.RedirectLineData
import com.fintecsystems.xs2awizard.form.SubmitLineData
import com.fintecsystems.xs2awizard.form.ValueFormLineData
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * Constructs the JSON body for a form submission request.
 *
 * Iterates over [form] and includes all [ValueFormLineData] fields, skipping unchecked
 * [CheckBoxLineData]. Always appends [action] and merges any additional [values].
 */
internal fun buildFormJsonBody(
    form: List<FormLineData>?,
    action: String,
    values: JsonObject? = null,
) = buildJsonObject {
    form?.forEach {
        if (it is ValueFormLineData) {
            if (it is CheckBoxLineData && it.value?.jsonPrimitive?.booleanOrNull != true) {
                return@forEach
            }

            put(
                it.name,
                it.value?.jsonPrimitive ?: JsonNull,
            )
        }
    }

    put("action", JsonPrimitive(action))

    values?.entries?.forEach {
        put(it.key, it.value.jsonPrimitive.content)
    }
}

/**
 * Filters out error [ParagraphLineData] items that duplicate a [ValueFormLineData.validationError]
 * already present on another form element, preventing double-display of the same error message.
 */
internal fun filterFormLines(form: List<FormLineData>?): List<FormLineData>? {
    return form?.filter { formLineData ->
        if (formLineData !is ParagraphLineData) {
            return@filter true
        }

        if (formLineData.severity != "error") {
            return@filter true
        }

        return@filter form.none {
            it is ValueFormLineData
                    && !it.validationError.isNullOrEmpty()
                    && it.validationError == formLineData.text
        }
    }
}

/**
 * Returns true if the given [form] contains a back button, i.e. a [SubmitLineData] or
 * [RedirectLineData] with a non-empty [SubmitLineData.backLabel] / [RedirectLineData.backLabel].
 */
internal fun isBackButtonPresent(form: List<FormLineData>?): Boolean {
    return form?.any {
        (it is SubmitLineData && !it.backLabel.isNullOrEmpty())
                || (it is RedirectLineData && !it.backLabel.isNullOrEmpty())
    } ?: false
}
