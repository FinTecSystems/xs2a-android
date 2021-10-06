package com.fintecsystems.xs2awizard.form.components.textLine

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutoCompleteEntry(
    val value: String,
    val label: String,
) {
    override fun toString() = value
}

@Serializable
data class AutoCompleteData(
    val name: String,
    val data: List<AutoCompleteEntry>,
)

@Serializable
data class AutoCompleteResponse(
    @SerialName("autocomplete")
    val autoCompleteData: AutoCompleteData,
    val language: String? = null,
)