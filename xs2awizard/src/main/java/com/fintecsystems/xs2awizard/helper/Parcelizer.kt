package com.fintecsystems.xs2awizard.helper

import android.os.Parcel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlinx.parcelize.Parceler

object DpParceler : Parceler<Dp> {
    override fun create(parcel: Parcel) = Dp(parcel.readFloat())

    override fun Dp.write(parcel: Parcel, flags: Int) = parcel.writeFloat(value)
}

object ColorParceler : Parceler<Color> {
    override fun create(parcel: Parcel) = Color(parcel.readLong().toULong())

    override fun Color.write(parcel: Parcel, flags: Int) = parcel.writeLong(value.toLong())
}