package com.fintecsystems.xs2awizard.components

data class XS2AWizardError(
    val errorCode: String,
    val messages: List<String>,
    val recoverable: Boolean,
)
