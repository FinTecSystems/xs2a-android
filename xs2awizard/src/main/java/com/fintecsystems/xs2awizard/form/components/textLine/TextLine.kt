package com.fintecsystems.xs2awizard.form.components.textLine

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.shared.FormText
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import com.fintecsystems.xs2awizard.helper.JSONFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

/**
 * Shows an TextInput-Field, which is able to perform AutoComplete as well.
 *
 * @param formData Data of this FormLine
 * @param viewModel ViewModel of the Wizard-Instance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextLine(formData: TextLineData, viewModel: XS2AWizardViewModel) {
    // We need an reactive value, because formData.value is not reactive.
    var textFieldValue by remember {
        mutableStateOf(
            formData.value?.jsonPrimitive?.content ?: ""
        )
    }

    // AutoComplete fields
    val coroutineScope = rememberCoroutineScope()
    var currentAutoCompleteJob by remember { mutableStateOf<Job?>(null) }
    var showAutoCompleteDropdown by remember { mutableStateOf(false) }
    var autoCompleteRequestFinished by remember { mutableStateOf(true) }
    var autoCompleteResponse by remember { mutableStateOf<AutoCompleteResponse?>(null) }

    // Cancel AutoComplete-Job, when component is unmounted
    DisposableEffect(null) {
        onDispose { currentAutoCompleteJob?.cancel() }
    }

    /**
     * Performs autocomplete and shows dropdown when successful.
     */
    fun performAutoComplete() {
        currentAutoCompleteJob?.cancel()

        if (textFieldValue.isEmpty()) {
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
    fun onValueChange(newValue: String) {
        textFieldValue = newValue
        // Update formData.value as well
        formData.value = JsonPrimitive(newValue)

        if (formData.autoCompleteAction != null) {
            performAutoComplete()
        }
    }

    if (formData.autoCompleteAction == null) {
        FormTextField(
            value = textFieldValue,
            onValueChange = ::onValueChange,
            placeholder = formData.placeholder,
            isError = formData.invalid,
            required = formData.required,
            errorMessage = formData.validationError,
            label = formData.label
        )
    } else {
        DockedSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    max = if (
                        !autoCompleteRequestFinished
                        || autoCompleteResponse?.autoCompleteData?.data.isNullOrEmpty()
                    ) 200.dp
                    else Dp.Unspecified
                ),
            inputField = {
                SearchBarInputField(
                    query = textFieldValue,
                    onQueryChange = { onValueChange(it) },
                    onSearch = { onValueChange(it) },
                    expanded = showAutoCompleteDropdown,
                    placeholder = formData.placeholder,
                    isError = formData.invalid,
                    required = formData.required,
                    errorMessage = formData.validationError,
                    label = formData.label
                )
            },
            shape = XS2ATheme.CURRENT.inputShape.value,
            expanded = showAutoCompleteDropdown,
            onExpandedChange = { showAutoCompleteDropdown = it }
        ) {
            if (autoCompleteRequestFinished) {
                AutoCompleteDropdownContent(
                    autoCompleteData = autoCompleteResponse?.autoCompleteData,
                    onItemClick = {
                        textFieldValue = it
                        formData.value = JsonPrimitive(it)
                        showAutoCompleteDropdown = false
                    }
                )
            } else {
                LoadingIndicator(
                    Modifier
                        .fillMaxSize()
                        .padding(0.dp, 5.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AutoCompleteDropdownContent(
    autoCompleteData: AutoCompleteData?,
    onItemClick: (String) -> Unit,
) {
    if (autoCompleteData?.data != null) {
        if (autoCompleteData.data.isNotEmpty()) {
            autoCompleteData.data.forEach {
                AutoCompleteDropdownItemContainer {
                    val bankObject = it.bankObject
                    ListItem(
                        modifier = Modifier
                            .clickable {
                                onItemClick(it.value)
                            },
                        headlineContent = {
                            FormText(
                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                                text = "${bankObject.name} (${bankObject.city})",
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        },
                        supportingContent = {
                            FormText(
                                modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE),
                                text = "${bankObject.bankCode} [${bankObject.bic}]",
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleSmall
                            )
                        })
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AutoCompleteDropdownItemContainer {
                    FormText(
                        text = stringResource(R.string.no_search_results),
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        AutoCompleteDropdownItemContainer {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .width(48.dp),
                    painter = painterResource(R.drawable.ic_warning),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )

                FormText(
                    text = stringResource(R.string.server_error),
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                )
            }
        }
    }
}

@Composable
private fun AutoCompleteDropdownItemContainer(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp, 4.dp),
        content = content
    )
}