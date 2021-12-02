package com.fintecsystems.xs2awizard.form

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FormResponse(
    val form: List<FormLineData>? = null,
    val language: String? = null,
    val callback: String? = null,
    val callbackParams: List<JsonElement>? = null,
    val error: String? = null,
)
