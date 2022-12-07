package com.fintecsystems.xs2awizard.components.theme.styles

import android.os.Parcelable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.helper.DpParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

/**
 * Size of padding or margin of an element.
 */
@Parcelize
@TypeParceler<Dp, DpParceler>()
data class PaddingMarginConfiguration(
    val start: Dp,
    val end: Dp,
    val top: Dp,
    val bottom: Dp,
) : Parcelable {
    constructor(all: Dp) : this(all, all, all, all)
    constructor(vertical: Dp, horizontal: Dp) : this(horizontal, horizontal, vertical, vertical)

    constructor(
        startDp: Int,
        endDp: Int,
        topDp: Int,
        bottomDp: Int,
    ) : this(startDp.dp, endDp.dp, topDp.dp, bottomDp.dp)

    constructor(allDp: Int) : this(allDp.dp)
    constructor(verticalDp: Int, horizontalDp: Int) : this(horizontalDp.dp, verticalDp.dp)

    companion object {
        val None = PaddingMarginConfiguration(0.dp)
    }
}