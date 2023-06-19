package com.fintecsystems.xs2awizard.form.components.textLine

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.LabelledContainer
import com.fintecsystems.xs2awizard.form.components.shared.AnimatedAutoScrollContainer
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Shows an TextInput-Field, which is able to perform AutoComplete as well.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
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
    var currentAutoCompleteJob by remember { mutableStateOf<Job?>(null) }
    var showAutoCompleteDropdown by remember { mutableStateOf(false) }
    var autoCompleteRequestFinished by remember { mutableStateOf(true) }
    var autoCompleteResponse by remember { mutableStateOf<AutoCompleteResponse?>(null) }

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

        if (textFieldValue.text.isEmpty()) {
            showAutoCompleteDropdown = false
            autoCompleteRequestFinished = true

            return
        }

        autoCompleteRequestFinished = false
        showAutoCompleteDropdown = true

        currentAutoCompleteJob = coroutineScope.launch {
            delay(300)

            viewModel.submitFormWithCallback(formData.autoCompleteAction!!) {
                val response =
                    JSONFormatter.formatter.decodeFromString<AutoCompleteResponse>(it)

                autoCompleteResponse = response
                autoCompleteRequestFinished = true
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
                onFocusChanged = { if (!it.isFocused) showAutoCompleteDropdown = false },
                onGloballyPositioned = { textFieldSize = it.size.toSize() }
            )
        }

        DropdownMenu(
            expanded = showAutoCompleteDropdown,
            onDismissRequest = { showAutoCompleteDropdown = false },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .background(
                    XS2ATheme.CURRENT.surfaceColor.value,
                    XS2ATheme.CURRENT.inputShape.value,
                )
        ) {
            if (autoCompleteRequestFinished) {
                AutoCompleteDropdownContent(
                    autoCompleteData = autoCompleteResponse?.autoCompleteData,
                    onItemClick = {
                        textFieldValue =
                            TextFieldValue(it, TextRange(it.length))
                        formData.value = JsonPrimitive(it)
                        showAutoCompleteDropdown = false
                    }
                )
            } else {
                LoadingIndicator(
                    Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 5.dp)
                )
            }
        }
    }
}

@Composable
private fun AutoCompleteDropdownContent(
    autoCompleteData: AutoCompleteData?,
    onItemClick: (String) -> Unit,
) {
    if (autoCompleteData?.data != null) {
        if (autoCompleteData.data.isNotEmpty()) {
            autoCompleteData.data.forEach {
                DropdownMenuItem(onClick = {
                    onItemClick(it.value)
                }) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(2.dp, 4.dp)
                    ) {
                        val bankObject = it.bankObject

                        AnimatedAutoScrollContainer {
                            FormText(
                                text = "${bankObject.name} (${bankObject.city})",
                                maxLines = 1,
                                style = MaterialTheme.typography.subtitle1.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        AnimatedAutoScrollContainer {
                            FormText(
                                text = "${bankObject.bankCode} [${bankObject.bic}]",
                                maxLines = 1,
                                style = MaterialTheme.typography.subtitle2
                            )
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp, 4.dp)
            ) {
                FormText(
                    text = stringResource(R.string.no_search_results),
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp, 4.dp)
        ) {
            FormText(
                text = "Network error",
                maxLines = 1,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}