package com.fintecsystems.xs2awizard.components.theme.styles

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AColor
import com.fintecsystems.xs2awizard.helper.DpParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

/**
 * Border style definition.
 */
@Immutable
@Parcelize
@TypeParceler<Dp, DpParceler>()
data class BorderStyle(
    val color: XS2AColor = XS2AColor.Unspecified,
    val width: Dp = Dp.Unspecified
) : Parcelable {
    /**
     * Secondary constructor for non-compose projects.
     */
    @Suppress("unused")
    constructor(
        color: XS2AColor = XS2AColor.Unspecified,
        width: Int? = null
    ) : this(
        color,
        width?.dp ?: Dp.Unspecified
    )

    companion object {
        val Unspecified = BorderStyle(color = XS2AColor.Unspecified, width = Dp.Unspecified)
    }
}
