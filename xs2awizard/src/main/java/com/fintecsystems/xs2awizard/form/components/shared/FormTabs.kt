package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.foundation.layout.height
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

@Composable
fun FormTabs(
    selected: Int,
    onSelectedChange: (Int) -> Unit,
    tabs: List<String>
) {
    TabRow(
        selectedTabIndex = selected,
        backgroundColor = XS2ATheme.CURRENT.backgroundColor,
        contentColor = XS2ATheme.CURRENT.tintColor,
    ) {
        tabs.forEachIndexed { index, label ->
            Tab(
                modifier = Modifier.height(48.dp), // Material SmallTab height.
                selected = index == selected,
                onClick = {
                    onSelectedChange(index)
                },
            ) {
                Text(
                    text = label,
                    color = XS2ATheme.CURRENT.textColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
            }
        }
    }
}