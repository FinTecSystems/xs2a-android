package com.fintecsystems.xs2awizard.form.components

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.mutateInteractionSource
import com.fintecsystems.xs2awizard.components.theme.NoRippleTheme
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.CheckBoxLineData
import com.fintecsystems.xs2awizard.helper.MarkupParser
import com.fintecsystems.xs2awizard.helper.Utils.getActivity
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

/**
 * Displays a CheckBox with a description text.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@Composable
fun CheckBoxLine(formData: CheckBoxLineData, viewModel: XS2AWizardViewModel) {
    var checkBoxValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.boolean ?: false
        )
    }

    val interactionSource = remember {
        MutableInteractionSource()
    }

    /**
     * Callback for when value of the TextField changed.
     *
     * @param newValue
     */
    fun onCheckedChange(newValue: Boolean) {
        checkBoxValue = newValue
        // Update formData.value as well
        formData.value = JsonPrimitive(newValue)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset((-14).dp, 0.dp)
            .clickable(interactionSource, rememberRipple()) {
                onCheckedChange(!checkBoxValue)
            },
        ) {

        CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
            Checkbox(
                checked = checkBoxValue,
                onCheckedChange = ::onCheckedChange,
                interactionSource = interactionSource,
                colors = CheckboxDefaults.colors(
                    checkedColor = XS2ATheme.CURRENT.tintColor,
                    uncheckedColor = XS2ATheme.CURRENT.unselectedColor,
                    checkmarkColor = XS2ATheme.CURRENT.onTintColor
                )
            )
        }

        if (!formData.label.isNullOrEmpty()) {
            val annotatedString = MarkupParser.parseMarkupText(formData.label)
            val activity = LocalContext.current.getActivity<Activity>()

            ClickableText(
                modifier = Modifier
                    .padding(0.dp, 14.dp, 0.dp, 0.dp)
                    .mutateInteractionSource(interactionSource = interactionSource),
                text = annotatedString,
                style = TextStyle(
                    color = XS2ATheme.CURRENT.textColor,
                    fontFamily = XS2ATheme.CURRENT.fontFamily
                ),
                onClick = {
                    annotatedString.getStringAnnotations(it, it)
                        .firstOrNull().let { annotation ->
                            if (annotation != null)
                                viewModel.handleAnnotationClick(activity!!, annotation)
                            else onCheckedChange(!checkBoxValue)
                        }
                }
            )
        }
    }
}
