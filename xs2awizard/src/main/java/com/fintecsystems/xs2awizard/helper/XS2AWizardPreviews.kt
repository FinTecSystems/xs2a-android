package com.fintecsystems.xs2awizard.helper

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.fintecsystems.xs2awizard.FormLines
import com.fintecsystems.xs2awizard.components.theme.IXS2ATheme
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.XS2AThemeLight
import com.fintecsystems.xs2awizard.form.FormLineData
import com.fintecsystems.xs2awizard.form.ParagraphLineData

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