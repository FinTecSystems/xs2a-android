package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.components.theme.styles.LogoVariation
import com.fintecsystems.xs2awizard.components.theme.styles.PaddingMarginConfiguration
import com.fintecsystems.xs2awizard.components.theme.styles.ParagraphStyle

/**
 * Interface of all styleable attributes of the wizard.
 */
interface IXS2ATheme {
    // General
    val tintColor: Color
    val onTintColor: Color
    val fontFamily: FontFamily
    val backgroundColor: Color
    val surfaceColor: Color
    val textColor: Color
    val placeholderColor: Color
    val logoVariation: LogoVariation
    val loadingIndicatorBackgroundColor: Color

    // TextInput
    val inputTextColor: Color
    val inputBackgroundColor: Color
    val inputShape: Shape

    // Button
    val submitButtonStyle: ButtonStyle
    val abortButtonStyle: ButtonStyle
    val backButtonStyle: ButtonStyle
    val restartButtonStyle: ButtonStyle
    val redirectButtonStyle: ButtonStyle
    val buttonShape: Shape

    // Paragraph
    val errorParagraphStyle: ParagraphStyle
    val infoParagraphStyle: ParagraphStyle
    val warningParagraphStyle: ParagraphStyle
    val paragraphShape: Shape
    val paragraphPadding: PaddingMarginConfiguration
    val paragraphMargin: PaddingMarginConfiguration

    // WebView
    val webViewIconColor: Color
    val webViewBackgroundColor: Color
    val webViewBorderColor: Color
    val webViewTextColor: Color

    // Checkbox/Radio
    val unselectedColor: Color
    val disabledColor: Color

    // Connection status Banner
    val connectionStatusBannerBackgroundColor: Color
    val connectionStatusBannerTextColor: Color
}
