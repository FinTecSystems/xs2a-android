package com.fintecsystems.xs2awizard.components.theme

import android.os.Parcelable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AAlignmentHorizontal
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AColor
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AShape
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
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    val theme = xS2ATheme ?: if (isSystemInDarkTheme()) XS2ATheme.dark else XS2ATheme.light
    CompositionLocalProvider(LocalExtendedStyle provides theme) {
        val patchedTypography = with(typography) {
            copy(
                h1.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                h2.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                h3.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                h4.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                h5.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                h6.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                subtitle1.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                subtitle2.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                body1.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                body2.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                button.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                caption.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                ),
                overline.copy(
                    color = XS2ATheme.CURRENT.textColor.value
                )
            )
        }

        MaterialTheme(
            colors = colors,
            typography = patchedTypography,
            content = content
        )
    }
}

@Parcelize
class XS2ATheme(
    // General
    val tintColor: XS2AColor = XS2AColors.primary,
    val onTintColor: XS2AColor = XS2AColors.textColorLight,
    val backgroundColor: XS2AColor = XS2AColors.backgroundLight,
    val surfaceColor: XS2AColor = XS2AColors.surfaceColor,
    val textColor: XS2AColor = XS2AColors.textColor,
    val placeholderColor: XS2AColor = XS2AColors.darkGrey,
    val logoVariation: LogoVariation = LogoVariation.STANDARD,
    val loadingIndicatorBackgroundColor: XS2AColor = XS2AColors.backgroundTranslucent,

    // TextInput
    val inputTextColor: XS2AColor = XS2AColors.textColor,
    val inputBackgroundColor: XS2AColor = XS2AColors.backgroundInput,
    val inputShape: XS2AShape = XS2AShape(4.dp, XS2AShape.ShapeType.ROUNDED),
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
    val buttonShape: XS2AShape = XS2AShape(4.dp, XS2AShape.ShapeType.ROUNDED),
    val buttonSize: SizeConstraint = SizeConstraint.FillMaxWidth,
    val buttonHorizontalAlignment: XS2AAlignmentHorizontal = XS2AAlignmentHorizontal.CENTER_HORIZONTALLY,

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
    val paragraphShape: XS2AShape = XS2AShape(4.dp, XS2AShape.ShapeType.ROUNDED),
    val paragraphPadding: PaddingMarginConfiguration = PaddingMarginConfiguration(0.dp),
    val paragraphMargin: PaddingMarginConfiguration = PaddingMarginConfiguration(4.dp, 8.dp),

    // WebView
    val webViewIconColor: XS2AColor = XS2AColors.textColor,
    val webViewBackgroundColor: XS2AColor = XS2AColors.surfaceColor,
    val webViewBorderColor: XS2AColor = XS2AColors.darkGrey,
    val webViewTextColor: XS2AColor = XS2AColors.textColor,

    // Checkbox/Radio
    val unselectedColor: XS2AColor = XS2AColors.unselected,
    val disabledColor: XS2AColor = XS2AColors.disabled,

    // Connection status Banner
    val connectionStatusBannerBackgroundColor: XS2AColor = XS2AColors.backgroundWarning,
    val connectionStatusBannerTextColor: XS2AColor = XS2AColors.textColor,
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
