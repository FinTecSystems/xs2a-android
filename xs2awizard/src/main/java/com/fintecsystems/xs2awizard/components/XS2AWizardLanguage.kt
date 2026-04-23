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
    @Deprecated(
        message = "We no longer support French, existing integrations will automatically fallback to English. This will be removed in the next major release.",
        replaceWith = ReplaceWith("EN")
    )
    FR,

    @SerialName("it")
    @Deprecated(
        message = "We no longer support Italian, existing integrations will automatically fallback to English. This will be removed in the next major release.",
        replaceWith = ReplaceWith("EN")
    )
    IT,

    @SerialName("es")
    @Deprecated(
        message = "We no longer support Spanish, existing integrations will automatically fallback to English. This will be removed in the next major release.",
        replaceWith = ReplaceWith("EN")
    )
    ES;

    /**
     * @return this language if it's supported by the SDK, otherwise EN.
     */
    fun conformToSupportedLanguage() = if (isSupported(this)) {
        this
    } else {
        EN
    }

    companion object {
        fun fromString(value: String, default: XS2AWizardLanguage = EN) = try {
            valueOf(value.uppercase())
        } catch (_: IllegalArgumentException) {
            default
        }

        /**
         * @param language language to check.
         *
         * @return true if [language] is supported by the XS2A-Backend, false otherwise.
         */
        fun isSupported(language: XS2AWizardLanguage) = language == DE || language == EN
    }
}