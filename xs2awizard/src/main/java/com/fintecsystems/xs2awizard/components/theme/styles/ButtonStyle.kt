package com.fintecsystems.xs2awizard.components.theme.styles

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ButtonStyle(
    val backgroundColor: Color,
    val textColor: Color,
) {
    @Suppress("unused")
    constructor(
        backgroundColor: Int,
        textColor: Int
    ) : this(Color(backgroundColor), Color(textColor))

    companion object {
        val Unspecified = ButtonStyle(Color.Unspecified, Color.Unspecified)
    }
}
