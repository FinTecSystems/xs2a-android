package com.fintecsystems.xs2awizard.components.theme.support

import androidx.compose.ui.Alignment

/**
 * [Alignment] Wrapper for non-compose projects.
 */
enum class SupportAlignment(val alignmentValue: Alignment.Horizontal) {
    CENTER_HORIZONTALLY(Alignment.CenterHorizontally),
    START(Alignment.Start),
    END(Alignment.End),
}