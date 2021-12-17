package com.fintecsystems.xs2awizard.form.components.textLine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.LabelledContainer
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

@Composable
fun TextLine(formData: TextLineData, viewModel: XS2AWizardViewModel) {
    // We need an reactive value, because formData.value is not reactive.
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                formData.value?.jsonPrimitive?.content ?: ""
            )
        )
    }

    // AutoComplete fields
    val coroutineScope = rememberCoroutineScope()
    var currentAutoCompleteJob: Job? = null
    val showAutoCompleteDropdown = remember { mutableStateOf(false) }
    val autoCompleteResponse = remember { mutableStateOf<AutoCompleteResponse?>(null) }

    // Workaround to let the dropdown have the same size as the TextField
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    // Cancel AutoComplete-Job, when component is unmounted
    DisposableEffect(null) {
        onDispose { currentAutoCompleteJob?.cancel() }
    }

    /**
     * Performs autocomplete and shows dropdown when successful.
     */
    fun performAutoComplete() {
        currentAutoCompleteJob?.cancel()

        currentAutoCompleteJob = coroutineScope.launch {
            delay(300)

            viewModel.submitFormWithCallback(formData.autoCompleteAction!!) {
                val response =
                    JSONFormatter.formatter.decodeFromString<AutoCompleteResponse>(it)

                autoCompleteResponse.value = response
                showAutoCompleteDropdown.value = true
            }
        }
    }

    /**
     * Callback for when value of the TextField changed.
     *
     * @param newValue
     */
    fun onValueChange(newValue: TextFieldValue) {
        textFieldValue = newValue
        // Update formData.value as well
        formData.value = JsonPrimitive(newValue.text)

        if (formData.autoCompleteAction != null) {
            performAutoComplete()
        }
    }

    Column {
        LabelledContainer(label = formData.label) {
            FormTextField(
                value = textFieldValue,
                onValueChange = ::onValueChange,
                placeholder = formData.placeholder,
                onFocusChanged = { if (!it.isFocused) showAutoCompleteDropdown.value = false },
                onGloballyPositioned = { textFieldSize = it.size.toSize() }
            )
        }

        DropdownMenu(
            expanded = showAutoCompleteDropdown.value,
            onDismissRequest = { showAutoCompleteDropdown.value = false },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .background(
                    XS2ATheme.CURRENT.surfaceColor,
                    XS2ATheme.CURRENT.inputShape,
                )
        ) {
            autoCompleteResponse.value?.autoCompleteData?.data?.let {
                if (it.isNotEmpty()) {
                    it.forEach {
                        DropdownMenuItem(onClick = {
                            textFieldValue = TextFieldValue(it.value, TextRange(it.value.length))
                            formData.value = JsonPrimitive(it.value)
                            showAutoCompleteDropdown.value = false
                        }) {
                            Column(
                                modifier = Modifier.padding(2.dp, 4.dp)
                            ) {
                                FormText(
                                    text = it.value,
                                    color = XS2ATheme.CURRENT.textColor,
                                    fontSize = 17.sp,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold,
                                )

                                FormText(
                                    text = it.label,
                                    color = XS2ATheme.CURRENT.textColor,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(2.dp, 4.dp)
                    ) {
                        FormText(
                            text = stringResource(R.string.no_search_results),
                            color = XS2ATheme.CURRENT.textColor,
                            fontSize = 17.sp,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}