package com.fintecsystems.xs2awizard.components.loadingIndicator

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme

@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center),
            color = XS2ATheme.CURRENT.tintColor.value
        )
    }
}