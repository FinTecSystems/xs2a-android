package com.fintecsystems.xs2awizard.components.theme

import android.os.Parcelable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.styles.*
import kotlinx.parcelize.Parcelize

private val LocalExtendedStyle = staticCompositionLocalOf { XS2ATheme.light }

/**
 * Theme-Provider of the Wizard.
 *
 * Uses [MaterialTheme] as the base and extends it by [XS2ATheme].
 */
@Composable
fun XS2ATheme(
    xS2ATheme: XS2ATheme? = null,
    colors: Colors = lightColors(),
    content: @Composable () -> Unit
) {
    val theme = xS2ATheme ?: if (isSystemInDarkTheme()) XS2ATheme.dark else XS2ATheme.light
    CompositionLocalProvider(LocalExtendedStyle provides theme) {
        MaterialTheme(
            colors = colors,
            typography = Typography(defaultFontFamily = XS2ATheme.CURRENT.fontFamily),
            content = content
        )
    }
}

@Parcelize
class XS2ATheme(
    // General
    val tintColor: Color = XS2AColors.primary,
    val onTintColor: Color = XS2AColors.textColorLight,
    val fontFamily: FontFamily = FontFamily.Default,
    val backgroundColor: Color = XS2AColors.backgroundLight,
    val surfaceColor: Color = XS2AColors.surfaceColor,
    val textColor: Color = XS2AColors.textColor,
    val placeholderColor: Color = XS2AColors.darkGrey,
    val logoVariation: LogoVariation = LogoVariation.STANDARD,
    val loadingIndicatorBackgroundColor: Color = XS2AColors.backgroundTranslucent,

    // TextInput
    val inputTextColor: Color = XS2AColors.textColor,
    val inputBackgroundColor: Color = XS2AColors.backgroundInput,
    val inputShape: Shape = RoundedCornerShape(4.dp),
    val inputType: TextFieldType = TextFieldType.NORMAL,

    // Button
    val submitButtonStyle: ButtonStyle = ButtonStyle(XS2AColors.primary, XS2AColors.textColorLight),
    val abortButtonStyle: ButtonStyle = ButtonStyle(XS2AColors.darkGrey, XS2AColors.textColorLight),
    val backButtonStyle: ButtonStyle = ButtonStyle(XS2AColors.darkGrey, XS2AColors.textColorLight),
    val restartButtonStyle: ButtonStyle = ButtonStyle(
        XS2AColors.darkGrey,
        XS2AColors.textColorLight
    ),
    val redirectButtonStyle: ButtonStyle = ButtonStyle(
        XS2AColors.primary,
        XS2AColors.textColorLight
    ),
    val buttonShape: Shape = RoundedCornerShape(4.dp),
    val buttonSize: SizeConstraint = SizeConstraint.FillMaxWidth,
    val buttonHorizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,

    // Paragraph
    val errorParagraphStyle: ParagraphStyle = ParagraphStyle(
        XS2AColors.backgroundError,
        XS2AColors.textColorLight
    ),
    val infoParagraphStyle: ParagraphStyle = ParagraphStyle(
        XS2AColors.backgroundInfo,
        XS2AColors.textColorLight
    ),
    val warningParagraphStyle: ParagraphStyle = ParagraphStyle(
        XS2AColors.backgroundWarning,
        XS2AColors.textColor
    ),
    val paragraphShape: Shape = RoundedCornerShape(4.dp),
    val paragraphPadding: PaddingMarginConfiguration = PaddingMarginConfiguration(0.dp),
    val paragraphMargin: PaddingMarginConfiguration = PaddingMarginConfiguration(4.dp, 8.dp),

    // WebView
    val webViewIconColor: Color = XS2AColors.textColor,
    val webViewBackgroundColor: Color = XS2AColors.surfaceColor,
    val webViewBorderColor: Color = XS2AColors.darkGrey,
    val webViewTextColor: Color = XS2AColors.textColor,

    // Checkbox/Radio
    val unselectedColor: Color = XS2AColors.unselected,
    val disabledColor: Color = XS2AColors.disabled,

    // Connection status Banner
    val connectionStatusBannerBackgroundColor: Color = XS2AColors.backgroundWarning,
    val connectionStatusBannerTextColor: Color = XS2AColors.textColor,
) : Parcelable {
    companion object {
        /**
         * Holder of the current theme attributes.
         */
        val CURRENT: XS2ATheme
            @Composable
            get() = LocalExtendedStyle.current

        /**
         * Implementation of a Light-Theme.
         */
        val light = XS2ATheme()

        /**
         * Implementation of a Dark-Theme
         */
        val dark = XS2ATheme(
            backgroundColor = XS2AColors.backgroundDark,
            surfaceColor = XS2AColors.backgroundDark,
            textColor = XS2AColors.textColorLight,
            logoVariation = LogoVariation.WHITE,
            loadingIndicatorBackgroundColor = XS2AColors.backgroundTranslucentDark,

            inputTextColor = XS2AColors.textColorLight,
            inputBackgroundColor = XS2AColors.backgroundInputDark,

            webViewIconColor = XS2AColors.textColorLight,
            webViewBackgroundColor = XS2AColors.backgroundDark,
            webViewTextColor = XS2AColors.textColorLight,

            unselectedColor = XS2AColors.unselectedDark,
            disabledColor = XS2AColors.disabledDark,
        )
    }
}
