package com.fintecsystems.xs2awizard.components.theme.styles

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ParagraphStyle(
    val backgroundColor: Color,
    val textColor: Color,
) {
    constructor(
        backgroundColor: Int,
        textColor: Int
    ) : this(Color(backgroundColor), Color(textColor))

    companion object {
        val Unspecified = ParagraphStyle(Color.Unspecified, Color.Unspecified)
    }
}
