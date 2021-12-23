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

    constructor(allDp: Int, type: ShapeType) : this(allDp.dp, type)
    constructor(
        topStartDp: Int,
        topEndDp: Int,
        bottomEndDp: Int,
        bottomStartDp: Int,
        type: ShapeType
    ) : this(topStartDp.dp, topEndDp.dp, bottomEndDp.dp, bottomStartDp.dp, type)

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