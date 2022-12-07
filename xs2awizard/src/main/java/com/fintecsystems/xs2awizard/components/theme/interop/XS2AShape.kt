package com.fintecsystems.xs2awizard.components.theme.interop

import android.os.Parcelable
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.helper.DpParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

/**
 * [Shape] wrapper for interoperability with non-compose projects.
 */
@Suppress("CanBeParameter", "unused") // Needed for @Parcelize
@Parcelize
@TypeParceler<Dp, DpParceler>()
class XS2AShape(
    private val topStart: Dp,
    private val topEnd: Dp,
    private val bottomEnd: Dp,
    private val bottomStart: Dp,
    private val type: ShapeType
) : Parcelable {
    val value: Shape = createShape(topStart, topEnd, bottomEnd, bottomStart, type)

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