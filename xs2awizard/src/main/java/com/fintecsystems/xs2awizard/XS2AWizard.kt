package com.fintecsystems.xs2awizard

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fintecsystems.xs2awizard.components.XS2AWizardCallbackListener
import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
import com.fintecsystems.xs2awizard.components.networking.ConnectionState
import com.fintecsystems.xs2awizard.components.networking.ConnectivityStatusBanner
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.webview.URLBarWebView
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.form.components.*
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine

/**
 * Renders the XS2A-Wizard.
 *
 * @param modifier - Modifier used for the form-elements layout.
 * @param sessionKey - Session key used by the wizard.
 * @param backendURL - Optional URL to target a different backend.
 * @param theme - Theme to be used.
 *                If null the default Light- or Dark-Theme, depending on the device settings, is used.
 * @param typography - Custom typography to be used by all form elements.
 * @param language - Specifies the wizard language.
 *                   Defaults to the device language if supported, [XS2AWizardLanguage.EN] otherwise.
 * @param enableScroll - If true the form container allows for automatic scrolling.
 *                       Disable if you need to implement nested scrolling.
 * @param enableBackButton - If true renders back button on some forms.
 *                           Disable this only if you implement your own back-handling-logic.
 *                           The user needs to have a way to "go-back".
 * @param enableAutomaticRetry - If true requests to the backend will be retried if the device is offline and goes online again.
 *                               This also means that the loading indicator will stay during that time.
 * @param callbackListener - Listener to all XS2A callbacks.
 * @param xs2aWizardViewModel - ViewModel of the Wizard-Instance.
 */
@Composable
fun XS2AWizard(
    modifier: Modifier = Modifier,
    sessionKey: String,
    backendURL: String? = null,
    theme: XS2ATheme? = null,
    typography: Typography = MaterialTheme.typography,
    language: XS2AWizardLanguage? = null,
    enableScroll: Boolean = true,
    enableBackButton: Boolean = true,
    enableAutomaticRetry: Boolean = true,
    callbackListener: XS2AWizardCallbackListener? = null,
    redirectDeepLink: String? = null,
    xs2aWizardViewModel: XS2AWizardViewModel = viewModel()
) {
    val form by xs2aWizardViewModel.form.observeAsState(null)
    val loadingIndicatorLock by xs2aWizardViewModel.loadingIndicatorLock.observeAsState(false)
    val currentWebViewUrl by xs2aWizardViewModel.currentWebViewUrl.observeAsState(null)
    val connectionState by xs2aWizardViewModel.connectionState.observeAsState(ConnectionState.UNKNOWN)

    val context = LocalContext.current

    DisposableEffect(sessionKey) {
        xs2aWizardViewModel.callbackListener = callbackListener

        // Initialize ViewModel
        xs2aWizardViewModel.onStart(
            sessionKey,
            backendURL,
            language,
            enableScroll,
            enableBackButton,
            enableAutomaticRetry,
            redirectDeepLink,
            context as Activity
        )

        // Cleanup
        onDispose {
            xs2aWizardViewModel.onStop()
        }
    }

    // Render
    XS2ATheme(
        xS2ATheme = theme,
        typography = typography,
    ) {
        Box(modifier) {
            Column(
                modifier = Modifier
                    .background(XS2ATheme.CURRENT.backgroundColor.value)
            ) {
                ConnectivityStatusBanner(connectionState)

                Column(
                    modifier = Modifier
                        .padding(14.dp, 5.dp),
                ) {
                    form?.let {
                        FormLinesContainer(it, xs2aWizardViewModel)
                    }
                }
            }

            if (loadingIndicatorLock) {
                LoadingIndicator(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        )
                        .background(XS2ATheme.CURRENT.loadingIndicatorBackgroundColor.value),
                )
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
fun FormLinesContainer(
    formData: List<FormLineData>,
    viewModel: XS2AWizardViewModel
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxSize()
            .then(
                if (viewModel.enableScroll)
                    Modifier
                        .height(IntrinsicSize.Min)
                        .verticalScroll(rememberScrollState())
                else
                    Modifier
            )
    ) {
        FormLines(formData, viewModel)
    }
}
