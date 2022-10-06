package com.fintecsystems.xs2awizard.components.theme.support

import androidx.compose.ui.Alignment

/**
 * [Alignment.Horizontal] Wrapper for non-compose projects.
 */
enum class SupportAlignmentHorizontal(val alignmentValue: Alignment.Horizontal) {
    CENTER_HORIZONTALLY(Alignment.CenterHorizontally),
    START(Alignment.Start),
    END(Alignment.End),
}