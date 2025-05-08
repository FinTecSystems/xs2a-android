package com.fintecsystems.xs2awizard.form.components.textLine

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

private val DockedExpandedTableMinHeight: Dp = 240.dp
private const val DockedExpandedTableMaxHeightScreenRatio: Float = 2f / 3f
private val SearchBarMinWidth: Dp = 360.dp

// Animation specs
private const val AnimationEnterDurationMillis: Int = 600
private const val AnimationExitDurationMillis: Int = 350
private const val AnimationDelayMillis: Int = 100
private val AnimationEnterEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
private val AnimationExitEasing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f)
private val AnimationEnterFloatSpec: FiniteAnimationSpec<Float> =
    tween(
        durationMillis = AnimationEnterDurationMillis,
        delayMillis = AnimationDelayMillis,
        easing = AnimationEnterEasing,
    )
private val AnimationExitFloatSpec: FiniteAnimationSpec<Float> =
    tween(
        durationMillis = AnimationExitDurationMillis,
        delayMillis = AnimationDelayMillis,
        easing = AnimationExitEasing,
    )
private val AnimationEnterSizeSpec: FiniteAnimationSpec<IntSize> =
    tween(
        durationMillis = AnimationEnterDurationMillis,
        delayMillis = AnimationDelayMillis,
        easing = AnimationEnterEasing,
    )
private val AnimationExitSizeSpec: FiniteAnimationSpec<IntSize> =
    tween(
        durationMillis = AnimationExitDurationMillis,
        delayMillis = AnimationDelayMillis,
        easing = AnimationExitEasing,
    )
private val DockedEnterTransition: EnterTransition =
    fadeIn(AnimationEnterFloatSpec) + expandVertically(AnimationEnterSizeSpec)
private val DockedExitTransition: ExitTransition =
    fadeOut(AnimationExitFloatSpec) + shrinkVertically(AnimationExitSizeSpec)

/**
 * <a href="https://m3.material.io/components/search/overview" class="external"
 * target="_blank">Material Design search</a>.
 *
 * This is a direct copy of [DockedSearchBar] with some changes we need.
 * The only significant change is the removal of the min-width of the content container.
 *
 * A search bar represents a floating search field that allows users to enter a keyword or phrase
 * and get relevant information. It can be used as a way to navigate through an app via search
 * queries.
 *
 * An search bar expands into a search "view" and can be used to display dynamic suggestions or
 * search results.
 *
 * ![Search bar
 * image](https://developer.android.com/images/reference/androidx/compose/material3/docked-search-bar.png)
 *
 * A [DockedSearchBar] displays search results in a bounded table below the input field. It is an
 * alternative to [SearchBar] when expanding to full-screen size is undesirable on large screens
 * such as tablets.
 *
 * An example looks like:
 *
 * @sample androidx.compose.material3.samples.DockedSearchBarSample
 *
 * @param inputField the input field of this search bar that allows entering a query, typically a
 *   [SearchBarDefaults.InputField].
 * @param expanded whether this search bar is expanded and showing search results.
 * @param onExpandedChange the callback to be invoked when this search bar's expanded state is
 *   changed.
 * @param modifier the [Modifier] to be applied to this search bar.
 * @param shape the shape of this search bar.
 * @param colors [SearchBarColors] that will be used to resolve the colors used for this search bar
 *   in different states. See [SearchBarDefaults.colors].
 * @param tonalElevation when [SearchBarColors.containerColor] is [ColorScheme.surface], a
 *   translucent primary color overlay is applied on top of the container. A higher tonal elevation
 *   value will result in a darker color in light theme and lighter color in dark theme. See also:
 *   [Surface].
 * @param shadowElevation the elevation for the shadow below the search bar.
 * @param content the content of this search bar to display search results below the [inputField].
 */
@ExperimentalMaterial3Api
@Composable
fun DockedSearchBar(
    inputField: @Composable () -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = SearchBarDefaults.dockedShape,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    tonalElevation: Dp = SearchBarDefaults.TonalElevation,
    shadowElevation: Dp = SearchBarDefaults.ShadowElevation,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = shape,
        color = colors.containerColor,
        contentColor = contentColorFor(colors.containerColor),
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        modifier = modifier.zIndex(1f).width(SearchBarMinWidth)
    ) {
        Column {
            inputField()

            AnimatedVisibility(
                visible = expanded,
                enter = DockedEnterTransition,
                exit = DockedExitTransition,
            ) {
                val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                val maxHeight =
                    remember(screenHeight) {
                        screenHeight * DockedExpandedTableMaxHeightScreenRatio
                    }

                Column(Modifier.heightIn(max = maxHeight)) {
                    HorizontalDivider(color = colors.dividerColor)
                    content()
                }
            }
        }
    }

    BackHandler(enabled = expanded) { onExpandedChange(false) }
}