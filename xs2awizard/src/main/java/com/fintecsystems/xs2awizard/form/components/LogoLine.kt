package com.fintecsystems.xs2awizard.form.components

import android.util.DisplayMetrics
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.LogoVariation
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.LogoLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.helper.Utils.getActivity

/**
 * Displays the FTS-Logo and shows the FTS-Imprint in an Alert on click.
 *
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun LogoLine(viewModel: XS2AWizardViewModel) {
    @Composable
    fun getImageUrlId(): Int {
        val isDarkMode = isSystemInDarkTheme()
        val logoVariation = XS2ATheme.CURRENT.logoVariation

        if (LocalContext.current.resources.displayMetrics.densityDpi >= DisplayMetrics.DENSITY_XHIGH) {
            return when (logoVariation) {
                LogoVariation.WHITE -> R.string.fts_logo_white_2x_url
                LogoVariation.BLACK -> R.string.fts_logo_black_2x_url
                else -> (if (isDarkMode) R.string.fts_logo_white_2x_url else R.string.fts_logo_2x_url)
            }
        }

        return when (logoVariation) {
            LogoVariation.WHITE -> R.string.fts_logo_white_url
            LogoVariation.BLACK -> R.string.fts_logo_black_url
            else -> (if (isDarkMode) R.string.fts_logo_white_url else R.string.fts_logo_url)
        }
    }

    var showAlertDialog by remember { mutableStateOf(false) }

    Image(
        painter = rememberImagePainter(stringResource(id = getImageUrlId())),
        contentDescription = stringResource(R.string.logo_image_description),
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { showAlertDialog = true }
    )

    if (showAlertDialog) {

        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    shape = XS2ATheme.CURRENT.buttonShape,
                    onClick = { showAlertDialog = false }
                ) {
                    FormText(
                        text = stringResource(id = R.string.dialog_dismiss),
                    )
                }
            },
            backgroundColor = XS2ATheme.CURRENT.surfaceColor,
            title = {
                FormText(
                    text = stringResource(id = R.string.dialog_imprint_title),
                    color = XS2ATheme.CURRENT.tintColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                val annotatedString = buildAnnotatedString {
                    append(stringResource(id = R.string.dialog_imprint_message))
                    append("\n")

                    pushStringAnnotation(
                        tag = "URL",
                        annotation = stringResource(id = R.string.dialog_imprint_link_url)
                    )

                    withStyle(
                        style = SpanStyle(
                            color = XS2ATheme.CURRENT.tintColor,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(stringResource(id = R.string.dialog_imprint_link_text))
                    }

                    pop()
                }

                val activity = LocalContext.current.getActivity()

                ClickableText(
                    modifier = Modifier.fillMaxWidth(),
                    text = annotatedString,
                    style = TextStyle(
                        color = XS2ATheme.CURRENT.textColor,
                        fontFamily = XS2ATheme.CURRENT.fontFamily
                    ),
                    onClick = {
                        annotatedString.getStringAnnotations(it, it)
                            .firstOrNull()?.let { annotation ->
                                viewModel.handleAnnotationClick(activity!!, annotation)
                            }
                    }
                )
            }
        )
    }
}