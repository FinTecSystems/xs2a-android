package com.fintecsystems.xs2awizard.components.loadingIndicator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider

@Composable
fun PopupLoadingIndicator(
    modifier: Modifier = Modifier,
) {
    val popupPositionProvider = object : PopupPositionProvider {
        override fun calculatePosition(
            anchorBounds: IntRect,
            windowSize: IntSize,
            layoutDirection: LayoutDirection,
            popupContentSize: IntSize
        ): IntOffset {
            return IntOffset.Zero
        }
    }

    Popup(popupPositionProvider = popupPositionProvider) {
        LoadingIndicator(modifier)
    }
}