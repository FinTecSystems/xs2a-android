package com.fintecsystems.xs2awizard.components.theme.styles

import androidx.compose.ui.unit.Dp

/**
 * Wrapper Class to specify different sizing behaviours.
 */
sealed class SizeConstraint {
    /**
     * Sets the [height] and [width] of the component.
     * Same as using Modifier.size.
     */
    class Size(
        val height: Dp = Dp.Unspecified,
        val width: Dp = Dp.Unspecified,
    ) : SizeConstraint()

    /**
     * Sets the width of the component to fill the parent.
     * Same as using Modifier.fillMaxWidth.
     */
    object FillMaxWidth : SizeConstraint()

    /**
     * Sets the width of the component to wrap it's content.
     * Same as just using Modifier or non at all.
     */
    object WrapContent : SizeConstraint()
}