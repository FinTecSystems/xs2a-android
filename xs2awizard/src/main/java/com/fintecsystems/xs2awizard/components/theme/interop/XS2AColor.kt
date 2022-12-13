package com.fintecsystems.xs2awizard.components.theme.interop

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import com.fintecsystems.xs2awizard.helper.ColorParceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler

/**
 * [Color] wrapper for interoperability with non-compose projects.
 */
@Suppress("unused")
@Parcelize
@TypeParceler<Color, ColorParceler>()
class XS2AColor(
    val value: Color
) : Parcelable {
    constructor(colorInt: Int) : this(Color(colorInt))
    constructor(colorHex: String) : this(colorHex.toColorInt())
    constructor(r: Int, g: Int, b: Int, a: Int) : this(Color(r, g, b, a))

    companion object {
        val Unspecified = XS2AColor(Color.Unspecified)
    }
}