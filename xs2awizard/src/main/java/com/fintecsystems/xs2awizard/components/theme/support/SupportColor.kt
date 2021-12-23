package com.fintecsystems.xs2awizard.components.theme.support

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

/**
 * [Color] Wrapper for non-compose projects.
 */
class SupportColor(
    color: Color
){
    val colorValue: Color = color

    constructor(colorInt: Int) : this(Color(colorInt))
    constructor(colorHex: String): this(colorHex.toColorInt())
    constructor(r: Int, g: Int, b: Int, a: Int): this(Color(r, g, b, a))
}