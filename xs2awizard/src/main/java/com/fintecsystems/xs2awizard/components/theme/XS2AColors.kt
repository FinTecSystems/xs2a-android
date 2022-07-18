package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * Default color-palette of the wizard.
 */
object XS2AColors {
    val primary = Color("#427783".toColorInt())

    val textColor = Color("#262626".toColorInt())
    val textColorLight = Color.White
    val darkGrey = Color("#BFBFBF".toColorInt())

    val backgroundNone = Color.Transparent
    val backgroundLight = Color.White
    val backgroundDark = Color("#121212".toColorInt())
    val backgroundTranslucent = Color("#AAFFFFFF".toColorInt())
    val backgroundTranslucentDark = Color("#AA121212".toColorInt())
    val backgroundWarning = Color("#FEAE22".toColorInt())
    val backgroundInfo = Color("#0E9EC2".toColorInt())
    val backgroundError = Color("#EA544A".toColorInt())
    val backgroundInput = Color("#14000000".toColorInt())
    val backgroundInputDark = Color("#14FFFFFF".toColorInt())
    val surfaceColor = Color.White

    val flickerBackground = Color.Black
    val flickerForeground = Color.White
    val flickerMarker = Color("#BFBFBF".toColorInt())

    val unselected = Color.Gray
    val disabled = Color.LightGray

    val unselectedDark = Color.LightGray
    val disabledDark = Color.Gray
}