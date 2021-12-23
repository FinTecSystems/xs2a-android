package com.fintecsystems.xs2awizard.form.components.textLine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Single entry of [AutoCompleteData]
 */
@Serializable
data class AutoCompleteEntry(
    val value: String,
    val label: String,
) {
    override fun toString() = value
}

/**
 * Data of [AutoCompleteResponse]
 */
@Serializable
data class AutoCompleteData(
    val name: String,
    val data: List<AutoCompleteEntry>,
)

/**
 * Response of an AutoComplete request.
 */
@Serializable
data class AutoCompleteResponse(
    @SerialName("autocomplete")
    val autoCompleteData: AutoCompleteData,
    val language: String? = null,
)