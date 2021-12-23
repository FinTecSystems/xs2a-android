package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Collection of Helper Methods to create [Color] instances outside of an Jetpack Compose Project.
 */
object ColorHelper {
    /**
     * Creates a new [Color] instance from an ARGB color int.
     * The resulting color is in the [sRGB][ColorSpaces.Srgb]
     * color space.
     *
     * @param colorInt The ARGB color int to create a <code>Color</code> from.
     * @return A non-null instance of {@link Color}
     */
    fun colorFromInt(colorInt: Int) = Color(colorInt)

    /**
     * Creates a new [Color] instance from an ARGB color int.
     * The resulting color is in the [sRGB][ColorSpaces.Srgb]
     * color space. This is useful for specifying colors with alpha
     * greater than 0x80 in numeric form without using [Long.toInt]:
     *
     *     val color = Color(0xFF000080)
     *
     * @param colorLong The 32-bit ARGB color int to create a <code>Color</code> from.
     */
    fun colorFromLong(colorLong: Long) = Color(colorLong)

    /**
     * Create a [Color] by passing individual [red], [green], [blue], [alpha]
     * components. The default [color space][ColorSpaces] is [SRGB][ColorSpaces.Srgb] and
     * the default [alpha] is `1.0` (opaque).
     */
    fun colorFromRGBA(
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float = 1f,
    ) = Color(red, green, blue, alpha)
}

/**
 * Collection of Helper Methods to create [Shape] instances outside of an Jetpack Compose Project.
 */
object ShapeHelper {
    /**
     * Type of the Shape
     */
    enum class ShapeType {
        ROUNDED,
        CUT
    }

    /**
     * Creates Shape of type [ShapeType] with sizes defined in Dp.
     */
    fun createShape(
        all: Float,
        type: ShapeType = ShapeType.ROUNDED
    ) = createShape(all.dp, type)

    /**
     * Creates Shape of type [ShapeType] with sizes defined in Dp.
     */
    fun createShape(
        all: Int,
        type: ShapeType = ShapeType.ROUNDED
    ) = createShape(all.dp, type)

    /**
     * Creates Shape of type [ShapeType] with sizes defined in Dp.
     */
    private fun createShape(
        all: Dp,
        type: ShapeType = ShapeType.ROUNDED
    ) = createShape(all, all, all, all, type)

    /**
     * Creates Shape of type [ShapeType] with sizes defined in Dp.
     */
    fun createShape(
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        type: ShapeType = ShapeType.ROUNDED
    ) = createShape(topStart.dp, topEnd.dp, bottomEnd.dp, bottomStart.dp, type)

    /**
     * Creates Shape of type [ShapeType] with sizes defined in Dp.
     */
    fun createShape(
        topStart: Int,
        topEnd: Int,
        bottomEnd: Int,
        bottomStart: Int,
        type: ShapeType = ShapeType.ROUNDED
    ) = createShape(topStart.dp, topEnd.dp, bottomEnd.dp, bottomStart.dp, type)

    /**
     * Creates Shape of type [ShapeType] with sizes defined in Dp.
     */
    private fun createShape(
        topStart: Dp,
        topEnd: Dp,
        bottomEnd: Dp,
        bottomStart: Dp,
        type: ShapeType = ShapeType.ROUNDED
    ) = when (type) {
        ShapeType.ROUNDED -> RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart)
        ShapeType.CUT -> CutCornerShape(topStart, topEnd, bottomEnd, bottomStart)
    }
}

/**
 * Collection of Helper Methods to create [FontFamily] instances outside of an Jetpack Compose Project.
 */
object FontFamilyHelper {
    enum class FontFamilyStyle {
        NORMAL,
        ITALIC
    }

    val Cursive = FontFamily.Cursive
    val Default = FontFamily.Default
    val Monospace = FontFamily.Monospace
    val SansSerif = FontFamily.SansSerif
    val Serif = FontFamily.Serif

    /**
     * Creates a Font with using resource ID.
     *
     * @param resId The resource ID of the font file in font resources. i.e. "R.font.myfont".
     * @param weight The weight of the font.
     * @param style The style of the font, normal or italic.
     */
    fun fontFamilyFromFontRes(
        resId: Int,
        weight: Int = 400,
        style: FontFamilyStyle = FontFamilyStyle.NORMAL
    ) = Font(resId, FontWeight(weight), FontStyle(style.ordinal))
}
