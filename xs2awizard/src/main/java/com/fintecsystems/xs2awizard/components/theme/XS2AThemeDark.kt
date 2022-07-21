package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.fintecsystems.xs2awizard.components.theme.styles.LogoVariation

/**
 * Implementation of a Dark-Theme
 */
object XS2AThemeDark : IXS2ATheme by XS2AThemeLight {
    override val backgroundColor: Color = XS2AColors.backgroundDark
    override val surfaceColor: Color = XS2AColors.backgroundDark
    override val textColor: Color = XS2AColors.textColorLight
    override val logoVariation: LogoVariation = LogoVariation.WHITE
    override val loadingIndicatorBackgroundColor: Color = XS2AColors.backgroundTranslucentDark

    override val inputTextColor: Color = XS2AColors.textColorLight
    override val inputBackgroundColor: Color = XS2AColors.backgroundInputDark

    override val webViewIconColor: Color = XS2AColors.textColorLight
    override val webViewBackgroundColor: Color = XS2AColors.backgroundDark
    override val webViewTextColor: Color = XS2AColors.textColorLight

    override val unselectedColor: Color = XS2AColors.unselectedDark
    override val disabledColor: Color = XS2AColors.disabledDark
}
