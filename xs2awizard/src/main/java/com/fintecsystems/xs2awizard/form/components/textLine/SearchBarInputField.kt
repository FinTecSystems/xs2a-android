package com.fintecsystems.xs2awizard.form.components.textLine

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.components.shared.FormTextField
import kotlinx.coroutines.delay

private val SearchBarMinWidth: Dp = 360.dp
private val SearchBarMaxWidth: Dp = 720.dp
private const val AnimationDelayMillis: Long = 100L

/**
 * A text field to input a query in a search bar.
 * This is a direct copy of [SearchBarDefaults.InputField] with some changes we need.
 *
 * @param query the query text to be shown in the input field.
 * @param onQueryChange the callback to be invoked when the input service updates the query. An
 *   updated text comes as a parameter of the callback.
 * @param onSearch the callback to be invoked when the input service triggers the
 *   [ImeAction.Search] action. The current [query] comes as a parameter of the callback.
 * @param expanded whether the search bar is expanded and showing search results.
 * @param modifier the [Modifier] to be applied to this input field.
 * @param enabled the enabled state of this input field. When `false`, this component will not
 *   respond to user input, and it will appear visually disabled and disabled to accessibility
 *   services.
 * @param placeholder the placeholder to be displayed when the [query] is empty.
 * @param leadingIcon the leading icon to be displayed at the start of the input field.
 * @param trailingIcon the trailing icon to be displayed at the end of the input field.
 * @param colors [TextFieldColors] that will be used to resolve the colors used for this input
 *   field in different states. See [SearchBarDefaults.inputFieldColors].
 * @param interactionSource an optional hoisted [MutableInteractionSource] for observing and
 *   emitting [Interaction]s for this input field. You can use this to change the search bar's
 *   appearance or preview the search bar in different states. Note that if `null` is provided,
 *   interactions will still happen internally.
 */
@ExperimentalMaterial3Api
@Composable
fun SearchBarInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = inputFieldColors(),
    interactionSource: MutableInteractionSource? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    errorMessage: String? = null
) {
    @Suppress("NAME_SHADOWING")
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val focused = interactionSource.collectIsFocusedAsState().value
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val searchSemantics = stringResource(R.string.search_bar_search)
    val suggestionsAvailableSemantics = stringResource(R.string.search_bar_suggestions_available)

    FormTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = placeholder,
        isError = isError,
        required = required,
        errorMessage = errorMessage,
        modifier =
            modifier
                .sizeIn(
                    minWidth = SearchBarMinWidth,
                    maxWidth = SearchBarMaxWidth,
                )
                .focusRequester(focusRequester)
                .semantics {
                    liveRegion = LiveRegionMode.Polite
                    contentDescription = searchSemantics
                    if (expanded) {
                        stateDescription = suggestionsAvailableSemantics
                    }
                    onClick {
                        focusRequester.requestFocus()
                        true
                    }
                },
        enabled = enabled,
        singleLine = true,
        //textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
        //cursorBrush = SolidColor(colors.cursorColor),
        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        //keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        interactionSource = interactionSource,
    )

    val shouldClearFocus = !expanded && focused
    LaunchedEffect(expanded) {
        if (shouldClearFocus) {
            // Start hack: If we don't do this, the beginning of the entire form will be focused instead of the next element.
            delay(AnimationDelayMillis)
            focusManager.clearFocus()
            delay(AnimationDelayMillis)
            focusRequester.requestFocus()
            // End hack.
            delay(AnimationDelayMillis)
            if (!focusManager.moveFocus(FocusDirection.Down)) {
                focusManager.clearFocus()
            }
        }
    }
}
