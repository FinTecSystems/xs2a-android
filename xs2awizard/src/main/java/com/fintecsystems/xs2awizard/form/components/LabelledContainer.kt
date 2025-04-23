package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.components.shared.FormText

/**
 * Container of most value-based FormLines.
 * Displays an label above the FormLine.
 *
 * @param label Label of the content
 * @param content Content of the container.
 */
@Composable
fun LabelledContainer(
    label: String?,
    required: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (label != null) {
            val inputHint = stringResource(R.string.labelled_container_input_hint)
            FormText(
                modifier = Modifier.semantics {
                    contentDescription = "$label. $inputHint"
                },
                text = label + if (required) "*" else "",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }

        content()
    }
}