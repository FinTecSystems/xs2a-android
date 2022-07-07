package com.fintecsystems.xs2awizard.components

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Collection of all supported languages.
 */
@Serializable
enum class XS2AWizardLanguage {
    @SerialName("de")
    DE,
    @SerialName("en")
    EN,
    @SerialName("fr")
    FR,
    @SerialName("it")
    IT,
    @SerialName("es")
    ES
}