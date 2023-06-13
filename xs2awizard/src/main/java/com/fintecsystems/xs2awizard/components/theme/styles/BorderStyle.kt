package com.fintecsystems.xs2awizard.components.theme.styles

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AColor
import kotlinx.parcelize.Parcelize

/**
 * Border style definition.
 */
@Immutable
@Parcelize
data class BorderStyle(
    val color: XS2AColor,
    val width: Float,
    val radius: Float
) : Parcelable
