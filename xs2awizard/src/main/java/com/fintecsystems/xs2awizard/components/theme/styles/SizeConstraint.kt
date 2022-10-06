package com.fintecsystems.xs2awizard.components.theme.styles

import androidx.compose.ui.unit.Dp

sealed class SizeConstraint {
    class Specified(
        val height: Dp = Dp.Unspecified,
        val width: Dp = Dp.Unspecified,
    ) : SizeConstraint()

    object FillMaxWidth : SizeConstraint()

    object WrapContent : SizeConstraint()
}