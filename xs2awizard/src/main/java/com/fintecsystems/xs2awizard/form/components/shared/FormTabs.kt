package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

/**
 * Renders a TabBar.
 *
 * @param selected Selected tab.
 * @param onSelectedChange Callback, when the selected tab changes.
 * @param tabs Label of each tab.
 */
@Composable
fun FormTabs(
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    tabs: List<String>
) {
    TabRow(
        selectedTabIndex = selected,
        backgroundColor = XS2ATheme.CURRENT.backgroundColor.value,
        contentColor = XS2ATheme.CURRENT.tintColor.value,
    ) {
        tabs.forEachIndexed { index, label ->
            Tab(
                modifier = Modifier.height(48.dp), // Material SmallTab height.
                selected = index == selected,
                onClick = {
                    onSelectedChange(index)
                },
            ) {
                FormText(
                    text = label,
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}