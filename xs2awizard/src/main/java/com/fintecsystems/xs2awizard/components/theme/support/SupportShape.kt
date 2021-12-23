package com.fintecsystems.xs2awizard.components.theme.support

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * [Shape] Wrapper for non-compose projects
 */
class SupportShape(
    topStart: Dp,
    topEnd: Dp,
    bottomEnd: Dp,
    bottomStart: Dp,
    type: ShapeType
) {
    val shapeValue: Shape = createShape(topStart, topEnd, bottomEnd, bottomStart, type)

    constructor(all: Dp, type: ShapeType) : this(all, all, all, all, type)
    constructor(all: Int, type: ShapeType) : this(all.dp, type)
    constructor(
        topStart: Int,
        topEnd: Int,
        bottomEnd: Int,
        bottomStart: Int,
        type: ShapeType
    ) : this(topStart.dp, topEnd.dp, bottomEnd.dp, bottomStart.dp, type)

    private fun createShape(
        topStart: Dp,
        topEnd: Dp,
        bottomEnd: Dp,
        bottomStart: Dp,
        type: ShapeType
    ) = when (type) {
        ShapeType.ROUNDED -> RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)
        ShapeType.CUT -> CutCornerShape(topStart, topEnd, bottomEnd, bottomStart)
    }

    enum class ShapeType {
        ROUNDED,
        CUT
    }
}