package com.fintecsystems.xs2awizard.components.theme.interop

import android.os.Parcelable
import androidx.compose.ui.text.font.*
import kotlinx.parcelize.Parcelize

/**
 * [FontFamily] wrapper for interoperability with non-compose projects.
 */
@Suppress("CanBeParameter") // Needed for @Parcelize
@Parcelize
class XS2AFontFamily(
    private val resId: Int,
    private val weight: Int = 400,
    private val style: FontFamilyStyle = FontFamilyStyle.NORMAL
) : Parcelable {
    val value: FontFamily = Font(resId, FontWeight(weight), FontStyle(style.ordinal)).toFontFamily()

    enum class FontFamilyStyle {
        NORMAL,
        ITALIC
    }
}