package com.fintecsystems.xs2awizard.components.theme.styles

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.fintecsystems.xs2awizard.components.theme.support.SupportColor

/**
 * Style-Attributes of a paragraph.
 */
@Immutable
data class ParagraphStyle(
    val backgroundColor: Color,
    val textColor: Color,
) {
    @Suppress("unused")
    constructor(
        backgroundColor: Int,
        textColor: Int
    ) : this(Color(backgroundColor), Color(textColor))

    @Suppress("unused")
    constructor(
        backgroundColor: SupportColor,
        textColor: SupportColor
    ) : this(backgroundColor.colorValue, textColor.colorValue)

    companion object {
        val Unspecified = ParagraphStyle(Color.Unspecified, Color.Unspecified)
    }
}
