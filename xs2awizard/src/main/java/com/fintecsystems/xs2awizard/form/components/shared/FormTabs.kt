package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp) // Material SmallTab height.
    ) {
        TabRow(
            modifier = Modifier
                .width(maxWidth)
                .height(maxHeight),
            selectedTabIndex = selected
        ) {
            tabs.forEachIndexed { index, label ->
                Tab(
                    modifier = Modifier.fillMaxHeight(),
                    selected = index == selected,
                    onClick = {
                        onSelectedChange(index)
                    },
                ) {
                    FormText(
                        text = label,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}