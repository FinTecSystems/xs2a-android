package com.fintecsystems.xs2awizard.screenshot.form

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.loadingIndicator.LoadingIndicator
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.TextLineData
import com.fintecsystems.xs2awizard.form.components.textLine.AutoCompleteData
import com.fintecsystems.xs2awizard.form.components.textLine.AutoCompleteDropdownContent
import com.fintecsystems.xs2awizard.form.components.textLine.AutoCompleteEntry
import com.fintecsystems.xs2awizard.form.components.textLine.AutoCompleteEntryBankObject
import com.fintecsystems.xs2awizard.form.components.textLine.DockedSearchBar
import com.fintecsystems.xs2awizard.form.components.textLine.SearchBarInputField
import com.fintecsystems.xs2awizard.form.components.textLine.TextLine
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTestBase
import com.fintecsystems.xs2awizard.screenshot.ScreenshotTheme
import com.fintecsystems.xs2awizard.screenshot.captureForTheme
import com.fintecsystems.xs2awizard.screenshot.createViewModel
import org.junit.Test

@OptIn(ExperimentalMaterial3Api::class)
class TextLineScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {

    @Composable
    private fun Empty() = TextLine(
        formData = TextLineData(name = "iban", label = "IBAN", placeholder = "DE00 0000 0000 0000 0000 00"),
        viewModel = createViewModel(),
    )

    @Composable
    private fun Invalid() = TextLine(
        formData = TextLineData(
            name = "iban",
            label = "IBAN",
            placeholder = "DE00 0000 0000 0000 0000 00",
            invalid = true,
            validationError = "Invalid IBAN format.",
        ),
        viewModel = createViewModel(),
    )

    // Autocomplete – idle: no query entered, dropdown not shown.
    @Composable
    private fun AutoCompleteIdle() = DockedSearchBar(
        inputField = {
            SearchBarInputField(
                query = "",
                onQueryChange = {},
                onSearch = {},
                expanded = false,
                label = "Bank",
                placeholder = "Search for your bank…",
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = XS2ATheme.CURRENT.inputShape.value,
        expanded = false,
        onExpandedChange = {},
    ) {}

    // Autocomplete – loading: query entered, results still being fetched.
    @Composable
    private fun AutoCompleteLoading() = DockedSearchBar(
        inputField = {
            SearchBarInputField(
                query = "Sparkasse",
                onQueryChange = {},
                onSearch = {},
                expanded = true,
                label = "Bank",
                placeholder = "Search for your bank…",
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        shape = XS2ATheme.CURRENT.inputShape.value,
        expanded = true,
        onExpandedChange = {},
    ) {
        LoadingIndicator(
            Modifier
                .fillMaxSize()
                .padding(0.dp, 5.dp),
        )
    }

    // Autocomplete – no results: query returned an empty list.
    @Composable
    private fun AutoCompleteNoResults() = DockedSearchBar(
        inputField = {
            SearchBarInputField(
                query = "xyzxyz",
                onQueryChange = {},
                onSearch = {},
                expanded = true,
                label = "Bank",
                placeholder = "Search for your bank…",
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 200.dp),
        shape = XS2ATheme.CURRENT.inputShape.value,
        expanded = true,
        onExpandedChange = {},
    ) {
        AutoCompleteDropdownContent(
            autoCompleteData = AutoCompleteData(name = "bank", data = emptyList()),
            onItemClick = {},
        )
    }

    // Autocomplete – with results: query returned 3 matching entries.
    @Composable
    private fun AutoCompleteWithResults() = DockedSearchBar(
        inputField = {
            SearchBarInputField(
                query = "Sparkasse",
                onQueryChange = {},
                onSearch = {},
                expanded = true,
                label = "Bank",
                placeholder = "Search for your bank…",
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = XS2ATheme.CURRENT.inputShape.value,
        expanded = true,
        onExpandedChange = {},
    ) {
        AutoCompleteDropdownContent(
            autoCompleteData = AutoCompleteData(
                name = "bank",
                data = listOf(
                    AutoCompleteEntry(
                        value = "BELADEBB",
                        label = "Sparkasse Berlin",
                        bankObject = AutoCompleteEntryBankObject(
                            name = "Sparkasse Berlin",
                            city = "Berlin",
                            bankCode = "10050000",
                            bic = "BELADEBB",
                            countryId = "DE",
                        ),
                    ),
                    AutoCompleteEntry(
                        value = "SSKMDEMMXXX",
                        label = "Stadtsparkasse München",
                        bankObject = AutoCompleteEntryBankObject(
                            name = "Stadtsparkasse München",
                            city = "München",
                            bankCode = "70150000",
                            bic = "SSKMDEMMXXX",
                            countryId = "DE",
                        ),
                    ),
                    AutoCompleteEntry(
                        value = "SPKHDE2HXXX",
                        label = "Sparkasse Hannover",
                        bankObject = AutoCompleteEntryBankObject(
                            name = "Sparkasse Hannover",
                            city = "Hannover",
                            bankCode = "25050180",
                            bic = "SPKHDE2HXXX",
                            countryId = "DE",
                        ),
                    ),
                ),
            ),
            onItemClick = {},
        )
    }

    @Test fun textLine_empty() = composeRule.captureForTheme("text_line_empty", screenshotTheme) { Empty() }
    @Test fun textLine_invalid() = composeRule.captureForTheme("text_line_invalid", screenshotTheme) { Invalid() }
    @Test fun textLine_autocomplete_idle() = composeRule.captureForTheme("text_line_autocomplete_idle", screenshotTheme) { AutoCompleteIdle() }
    @Test fun textLine_autocomplete_loading() = composeRule.captureForTheme("text_line_autocomplete_loading", screenshotTheme) { AutoCompleteLoading() }
    @Test fun textLine_autocomplete_no_results() = composeRule.captureForTheme("text_line_autocomplete_no_results", screenshotTheme) { AutoCompleteNoResults() }
    @Test fun textLine_autocomplete_with_results() = composeRule.captureForTheme("text_line_autocomplete_with_results", screenshotTheme) { AutoCompleteWithResults() }
}
