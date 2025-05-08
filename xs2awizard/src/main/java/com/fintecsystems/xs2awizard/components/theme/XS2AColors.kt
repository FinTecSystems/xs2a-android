package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.graphics.Color
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AColor

/**
 * Default color-palette of the wizard.
 */
object XS2AColors {
    val primary = XS2AColor("#427783")
    val error = XS2AColor("#EA544A")

    val textColor = XS2AColor("#262626")
    val textColorLight = XS2AColor(Color.White)
    val darkGrey = XS2AColor("#BFBFBF")

    val backgroundLight = XS2AColor(Color.White)
    val backgroundDark = XS2AColor("#121212")
    val backgroundTranslucent = XS2AColor("#AAFFFFFF")
    val backgroundTranslucentDark = XS2AColor("#AA121212")
    val backgroundWarning = XS2AColor("#FEAE22")
    val backgroundInfo = XS2AColor("#0E9EC2")
    val backgroundError = XS2AColor("#EA544A")
    val backgroundInput = XS2AColor("#EBEBEB")
    val backgroundInputDark = XS2AColor("#14FFFFFF")
    val surfaceColor = XS2AColor(Color.White)

    val flickerBackground = XS2AColor(Color.Black)
    val flickerForeground = XS2AColor(Color.White)
    val flickerMarker = XS2AColor("#BFBFBF")

    val unselected = XS2AColor(Color.Gray)
    val disabled = XS2AColor(Color.LightGray)

    val unselectedDark = XS2AColor(Color.LightGray)
    val disabledDark = XS2AColor(Color.Gray)
}