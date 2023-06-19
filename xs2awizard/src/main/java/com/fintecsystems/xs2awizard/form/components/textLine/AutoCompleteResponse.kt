package com.fintecsystems.xs2awizard.form.components.textLine

import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The bank metadata of an [AutoCompleteEntry].
 */
@Serializable
data class AutoCompleteEntryBankObject (
    val name: String,
    val city: String,
    @SerialName("bank_code")
    val bankCode: String,
    val bic: String,
    @SerialName("country_id")
    val countryId: String,
)

/**
 * Single entry of [AutoCompleteData]
 */
@Serializable
data class AutoCompleteEntry(
    val value: String,
    val label: String,
    @SerialName("object")
    val bankObject: AutoCompleteEntryBankObject
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
    val autoCompleteData: AutoCompleteData? = null,
    val language: XS2AWizardLanguage? = null,
)