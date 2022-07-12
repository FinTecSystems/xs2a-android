package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Default color-palette of the wizard.
 */
object XS2AColors {
    val primary = Color("#F89572".toColorInt())

    val textColor = Color.Black
    val textColorLight = Color.White
    val darkGrey = Color("#808080".toColorInt())

    val backgroundNone = Color.Transparent
    val backgroundDark = Color("#062A2F".toColorInt())
    val backgroundTranslucent = Color("#AAFFFFFF".toColorInt())
    val backgroundTranslucentDark = Color("#AA062A2F".toColorInt())
    val backgroundWarning = Color("#FEAE22".toColorInt())
    val backgroundInfo = Color("#0E9EC2".toColorInt())
    val backgroundError = Color("#EA544A".toColorInt())
    val backgroundInput = Color("#1F000000".toColorInt())
    val backgroundInputDark = Color("#1FFFFFFF".toColorInt())
    val surfaceColor = Color.White

    val flickerBackground = Color.Black
    val flickerForeground = Color.White
    val flickerMarker = Color("#808080".toColorInt())

    val unselected = Color.Gray
    val disabled = Color.LightGray

    val unselectedDark = Color.LightGray
    val disabledDark = Color.Gray
}