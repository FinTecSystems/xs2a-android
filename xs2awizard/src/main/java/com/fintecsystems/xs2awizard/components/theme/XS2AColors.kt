package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Default color-palette of the wizard.
 */
object XS2AColors {
    val primary = Color("#194f89".toColorInt())

    val textColor = Color.Black
    val textColorLight = Color.White
    val darkGrey = Color("#808080".toColorInt())

    val backgroundNone = Color.Transparent
    val backgroundTranslucent = Color("#AAFFFFFF".toColorInt())
    val backgroundTranslucentDark = Color("#AA000000".toColorInt())
    val backgroundWarning = Color("#FFFF00".toColorInt())
    val backgroundInfo = Color("#0000FF".toColorInt())
    val backgroundError = Color("#FF0000".toColorInt())
    val backgroundInput = Color("#1F000000".toColorInt())
    val backgroundInputDark = Color("#1FFFFFFF".toColorInt())

    val flickerBackground = Color.Black
    val flickerForeground = Color.White
    val flickerMarker = Color("#808080".toColorInt())

    val unselected = Color.Gray
    val disabled = Color.LightGray
}