package com.fintecsystems.xs2awizard.components.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalExtendedStyle = staticCompositionLocalOf<IXS2ATheme> { XS2AThemeBase }

@Composable
fun XS2ATheme(
    xS2ATheme: IXS2ATheme? = null,
    colors: Colors = lightColors(),
    content: @Composable () -> Unit
) {
    val theme = xS2ATheme ?: if (isSystemInDarkTheme()) XS2AThemeDark else XS2AThemeLight
    CompositionLocalProvider(LocalExtendedStyle provides theme) {
        MaterialTheme(
            colors = colors,
            content = content
        )
    }
}

object XS2ATheme {
    val CURRENT: IXS2ATheme
        @Composable
        get() = LocalExtendedStyle.current
}
