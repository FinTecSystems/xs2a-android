package com.fintecsystems.xs2awizard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.webview.URLBarWebView
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.form.components.*
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine

/**
 * Renders the XS2A-Wizard.
 *
 * @param xS2AWizardConfig Config of the Wizard-Instance.
 * @param xs2aWizardViewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun XS2AWizard(
    modifier: Modifier = Modifier,
    xS2AWizardConfig: XS2AWizardConfig,
    xs2aWizardViewModel: XS2AWizardViewModel = viewModel()
) {
    val form by xs2aWizardViewModel.form.observeAsState(null)
    val loadingIndicatorLock by xs2aWizardViewModel.loadingIndicatorLock.observeAsState(false)
    val currentWebViewUrl by xs2aWizardViewModel.currentWebViewUrl.observeAsState(null)

    DisposableEffect(xs2aWizardViewModel) {
        // Initialize ViewModel
        xs2aWizardViewModel.onStart(xS2AWizardConfig)

        // Cleanup
        onDispose {
            xs2aWizardViewModel.onStop()
        }
    }

    // Render
    XS2ATheme(xS2ATheme = xS2AWizardConfig.theme) {
        Box(modifier) {
            Box(
                modifier = Modifier
                    .background(XS2ATheme.CURRENT.backgroundColor)
                    .padding(10.dp, 5.dp),
            ) {
                form?.let {
                    FormLinesContainer(it, xs2aWizardViewModel)
                }
            }

            if (loadingIndicatorLock) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(XS2ATheme.CURRENT.loadingIndicatorBackgroundColor),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = XS2ATheme.CURRENT.tintColor
                    )

                }
            }

            if (currentWebViewUrl != null)
                URLBarWebView(xs2aWizardViewModel)
        }
    }
}

/**
 * Renders all FormLines and assigns a key to each FormLine.
 *
 * @param formData Form to render.
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun FormLines(formData: List<FormLineData>, viewModel: XS2AWizardViewModel) {
    val formDataHashString = formData.hashCode().toString()

    formData.forEach { formLineData ->
        val formLineKey =
        // We have to prepend the formData hashCode, because if there is an validation error
        // and the same formData is received again, because the key would be the same, the
        // FormLines will be reused, which is bad, because it won't react like expected anymore.
            // If we have have FormLines with values e.g. TextLine, we can just use their name.
            if (formLineData is ValueFormLineData) formDataHashString + formLineData.name
            // Otherwise we can just use the hash of the data, because it's static anyway.
            else formLineData.hashCode()

        // Because the Composables are cached by their index we need to specify an custom key.
        key(formLineKey) {
            when (formLineData) {
                is AutoSubmitLineData -> AutoSubmitLine(formLineData, viewModel)
                is ParagraphLineData -> ParagraphLine(formLineData, viewModel)
                is DescriptionLineData -> DescriptionLine(formLineData, viewModel)
                is TextLineData -> TextLine(formLineData, viewModel)
                is PasswordLineData -> PasswordLine(formLineData)
                is CaptchaLineData -> CaptchaLine(formLineData)
                is SelectLineData -> SelectLine(formLineData)
                is CheckBoxLineData -> CheckBoxLine(formLineData, viewModel)
                is RadioLineData -> RadioLine(formLineData)
                is ImageLineData -> ImageLine(formLineData)
                is LogoLineData -> LogoLine(viewModel)
                is FlickerLineData -> FlickerLine(formLineData)
                is SubmitLineData -> SubmitLine(formLineData, viewModel)
                is AbortLineData -> AbortLine(formLineData, viewModel)
                is RestartLineData -> RestartLine(formLineData, viewModel)
                is TabsLineData -> TabsLine(formLineData, viewModel)
                is RedirectLineData -> RedirectLine(formLineData, viewModel)
                is MultiLineData -> MultiLine(formLineData, viewModel)
                is HiddenLineData -> {
                    /* no-op */
                }
            }
        }
    }
}

/**
 * Renders all FormLines in a Column.
 *
 * @param formData Form to render.
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun FormLinesContainer(formData: List<FormLineData>, viewModel: XS2AWizardViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        FormLines(formData, viewModel)
    }
}
