package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Size of padding or margin of an element.
 */
class PaddingMarginConfiguration(
    val start: Dp,
    val end: Dp,
    val top: Dp,
    val bottom: Dp,
) {
    constructor(all: Dp) : this(all, all, all, all)
    constructor(vertical: Dp, horizontal: Dp) : this(horizontal, horizontal, vertical, vertical)

    constructor(
        start: Int,
        end: Int,
        top: Int,
        bottom: Int,
    ) : this(start.dp, end.dp, top.dp, bottom.dp)
    constructor(all: Int) : this(all, all, all, all)
    constructor(vertical: Int, horizontal: Int) : this(horizontal, horizontal, vertical, vertical)

    companion object {
        val None = PaddingMarginConfiguration(0.dp)
    }
}