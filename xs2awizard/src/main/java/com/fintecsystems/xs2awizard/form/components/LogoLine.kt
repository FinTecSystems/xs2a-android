package com.fintecsystems.xs2awizard.form.components

import android.util.DisplayMetrics
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.LogoVariation
import com.fintecsystems.xs2awizard.form.components.shared.FormText

/**
 * Displays the Logo and shows the Imprint in an Alert on click.
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

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(stringResource(id = getImageUrlId()))
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.logo_image_description),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { showAlertDialog = true }
    )

    if (showAlertDialog) {

        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            confirmButton = {},
            dismissButton = {
                TextButton(
                    shape = XS2ATheme.CURRENT.buttonShape.value,
                    onClick = { showAlertDialog = false }
                ) {
                    FormText(
                        text = stringResource(id = R.string.dialog_dismiss),
                    )
                }
            },
            backgroundColor = XS2ATheme.CURRENT.surfaceColor.value,
            title = {
                FormText(
                    text = stringResource(id = R.string.dialog_imprint_title),
                    style = MaterialTheme.typography.h6.copy(
                        color = XS2ATheme.CURRENT.tintColor.value,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            text = {
                val annotatedString = buildAnnotatedString {
                    appendLine(stringResource(id = R.string.dialog_imprint_message))

                    withLink(
                        LinkAnnotation.Url(
                            url = stringResource(id = R.string.dialog_imprint_link_url),
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = XS2ATheme.CURRENT.tintColor.value,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        )
                    ) {
                        append(stringResource(id = R.string.dialog_imprint_link_text))
                    }
                }

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = annotatedString,
                    style = MaterialTheme.typography.body1,
                )
            }
        )
    }
}