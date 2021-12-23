package com.fintecsystems.xs2awizard.components.theme.support

import androidx.compose.ui.text.font.*

/**
 * [FontFamily] Wrapper for non-compose projects.
 */
class SupportFontFamily(
    fontFamily: FontFamily
) {
    val fontFamilyValue: FontFamily = fontFamily

    constructor(
        resId: Int,
        weight: Int = 400,
        style: FontFamilyStyle = FontFamilyStyle.NORMAL
    ) : this(Font(resId, FontWeight(weight), FontStyle(style.ordinal)).toFontFamily())

    enum class FontFamilyStyle {
        NORMAL,
        ITALIC
    }

    companion object {
        val Cursive = SupportFontFamily(FontFamily.Cursive)
        val Default = SupportFontFamily(FontFamily.Default)
        val Monospace = SupportFontFamily(FontFamily.Monospace)
        val SansSerif = SupportFontFamily(FontFamily.SansSerif)
        val Serif = SupportFontFamily(FontFamily.Serif)
    }
}