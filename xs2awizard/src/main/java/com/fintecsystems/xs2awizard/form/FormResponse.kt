package com.fintecsystems.xs2awizard.form

import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FormResponse(
    val form: List<FormLineData>? = null,
    val language: XS2AWizardLanguage? = null,
    val callback: String? = null,
    val callbackParams: List<JsonElement>? = null,
    val error: String? = null,
    val isErrorRecoverable: Boolean? = null,
)
