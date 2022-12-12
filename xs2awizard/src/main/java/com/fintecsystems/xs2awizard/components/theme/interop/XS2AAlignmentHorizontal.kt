package com.fintecsystems.xs2awizard.components.theme.interop

import androidx.compose.ui.Alignment

/**
 * [Alignment.Horizontal] wrapper for interoperability with non-compose projects.
 */
@Suppress("unused")
enum class XS2AAlignmentHorizontal(val value: Alignment.Horizontal) {
    CENTER_HORIZONTALLY(Alignment.CenterHorizontally),
    START(Alignment.Start),
    END(Alignment.End),
}