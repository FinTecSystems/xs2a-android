package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.unit.Dp

class PaddingMarginConfiguration(
    val start: Dp,
    val end: Dp,
    val top: Dp,
    val bottom: Dp,
) {
    constructor(all: Dp) : this(all, all, all, all)
    constructor(vertical: Dp, horizontal: Dp) : this(horizontal, horizontal, vertical, vertical)
}