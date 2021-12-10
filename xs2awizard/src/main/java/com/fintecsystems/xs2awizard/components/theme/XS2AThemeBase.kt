package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.components.theme.styles.ParagraphStyle

object XS2AThemeBase : IXS2ATheme {
    override val tintColor: Color = Color.Unspecified
    override val onTintColor: Color = Color.Unspecified
    override val fontFamily: FontFamily = FontFamily.Default
    override val backgroundColor: Color = Color.Unspecified
    override val surfaceColor: Color = Color.Unspecified
    override val textColor: Color = Color.Unspecified
    override val placeholderColor: Color = Color.Unspecified
    override val logoVariation: LogoVariation = LogoVariation.STANDARD

    override val inputTextColor: Color = Color.Unspecified
    override val inputBackgroundColor: Color = Color.Unspecified
    override val inputShape: Shape = RoundedCornerShape(4.dp)

    override val submitButtonStyle: ButtonStyle = ButtonStyle.Unspecified
    override val abortButtonStyle: ButtonStyle = ButtonStyle.Unspecified
    override val backButtonStyle: ButtonStyle = ButtonStyle.Unspecified
    override val restartButtonStyle: ButtonStyle = ButtonStyle.Unspecified
    override val redirectButtonStyle: ButtonStyle = ButtonStyle.Unspecified
    override val buttonShape: Shape = RoundedCornerShape(4.dp)

    override val errorParagraphStyle: ParagraphStyle = ParagraphStyle.Unspecified
    override val infoParagraphStyle: ParagraphStyle = ParagraphStyle.Unspecified
    override val warningParagraphStyle: ParagraphStyle = ParagraphStyle.Unspecified
    override val paragraphShape: Shape = RoundedCornerShape(4.dp)
    override val paragraphMargin: PaddingMarginConfiguration = PaddingMarginConfiguration(0.dp)
    override val paragraphPadding: PaddingMarginConfiguration = PaddingMarginConfiguration(0.dp)

    override val webViewIconColor: Color = Color.Unspecified
    override val webViewBackgroundColor: Color = Color.Unspecified
    override val webViewBorderColor: Color = Color.Unspecified
    override val webViewTextColor: Color = Color.Unspecified
}
