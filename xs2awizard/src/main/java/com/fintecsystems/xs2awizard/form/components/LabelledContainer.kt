package com.fintecsystems.xs2awizard.form.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.form.components.shared.FormText

/**
 * Container of most value-based FormLines.
 * Displays an label above the FormLine.
 *
 * @param label Label of the content
 * @param content Content of the container.
 */
@Composable
fun LabelledContainer(label: String?, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        if (label != null) {
            FormText(
                text = label,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        }

        content()
    }
}