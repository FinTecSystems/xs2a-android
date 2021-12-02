package com.fintecsystems.xs2awizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fintecsystems.xs2awizard.components.theme.IXS2ATheme
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.XS2AThemeLight
import com.fintecsystems.xs2awizard.form.*
import com.fintecsystems.xs2awizard.form.components.ParagraphLine

class XS2AWizardViewModel : ViewModel() {
    val form = MutableLiveData<List<FormLineData>>()
}

@Composable
fun XS2AWizardComponent(xs2aWizardViewModel: XS2AWizardViewModel = viewModel()) {
    val form by xs2aWizardViewModel.form.observeAsState(null)

    // Helper methods

    // Render
    XS2ATheme {
        form?.let { FormLines(it) }
    }
}

@Composable
fun FormLines(formData: List<FormLineData>) {
    Column {
        for (formLineData in formData) {
            when (formLineData) {
                // is AutoSubmitLineData -> AutoSubmitLine(formLineData)
                is ParagraphLineData -> ParagraphLine(formLineData)
                // is DescriptionLineData -> DescriptionLine(formLineData)
                // is TextLineData -> TextLine(formLineData)
                // is PasswordLineData -> PasswordLine(formLineData)
                // is CaptchaLineData -> CaptchaLine(formLineData)
                // is SelectLineData -> SelectLine(formLineData)
                // is CheckBoxLineData -> CheckBoxLine(formLineData)
                // is HiddenLineData -> HiddenLine(formLineData)
                // is RadioLineData -> RadioLine(formLineData)
                // is ImageLineData -> ImageLine(formLineData)
                // is LogoLineData -> LogoLine(formLineData)
                // is FlickerLineData -> FlickerLine(formLineData)
                // is SubmitLineData -> SubmitLine(formLineData)
                // is AbortLineData -> AbortLine(formLineData)
                // is RestartLineData -> RestartLine(formLineData)
                // is TabsLineData -> TabsLine(formLineData)
                // is RedirectLineData -> RedirectLine(formLineData)
                // is MultiLineData -> MultiLine(formLineData)
            }
        }
    }
}

@Composable
@Preview
fun FormLinesPreview() {
    val mockFormData = listOf<FormLineData>(
        ParagraphLineData("TestText1", "TestTitle1"),
        ParagraphLineData("WarningText", "WarningTitle", "warning"),
        ParagraphLineData("ErrorText", "ErrorTitle", "error"),
        ParagraphLineData("InfoText", "InfoTitle", "info")
    )

    val customTheme = object : IXS2ATheme by XS2AThemeLight {
        override val textColor: Color = Color.Red
    }

    XS2ATheme(xS2ATheme = customTheme) {
        FormLines(formData = mockFormData)
    }
}

class XS2AWizard : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                XS2AWizardComponent()
            }
        }
    }
}