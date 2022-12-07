package com.fintecsystems.xs2awizard.components.theme.interop

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize

/**
 * [Color] wrapper for interoperability with non-compose projects.
 */
@Suppress("unused")
@Parcelize
class XS2AColor(
    val value: Color
) : Parcelable {
    constructor(colorInt: Int) : this(Color(colorInt))
    constructor(colorHex: String) : this(colorHex.toColorInt())
    constructor(r: Int, g: Int, b: Int, a: Int) : this(Color(r, g, b, a))

    companion object : Parceler<XS2AColor> {
        val Unspecified = XS2AColor(Color.Unspecified)

        override fun create(parcel: Parcel) = XS2AColor(Color(parcel.readLong()))

        override fun XS2AColor.write(parcel: Parcel, flags: Int) =
            parcel.writeLong(value.value.toLong())

    }
}