package com.fintecsystems.xs2awizard.form.components.shared

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import kotlinx.coroutines.delay

private data class ContentLayoutInfo(val width: Int, val containerWidth: Int)

/**
 * Scrolls the content automatically if it's to large for the container.
 *
 * @param scrollSpeed Time needed to scroll the container width once.
 * @param delay Start delay.
 * @param children Content of the container. Has to be a single element.
 */
@Composable
fun AnimatedAutoScrollContainer(
    modifier: Modifier = Modifier,
    scrollSpeed: Int = 5000,
    delay: Int = 1000,
    children: @Composable () -> Unit,
) {
    var offset by remember { mutableStateOf(0) }
    val contentLayoutInfoState = remember { mutableStateOf<ContentLayoutInfo?>(null) }

    LaunchedEffect(contentLayoutInfoState.value) {
        val contentLayoutInfo = contentLayoutInfoState.value ?: return@LaunchedEffect

        if (contentLayoutInfo.width <= contentLayoutInfo.containerWidth) return@LaunchedEffect

        val duration = scrollSpeed * contentLayoutInfo.width / contentLayoutInfo.containerWidth

        do {
            val animation = TargetBasedAnimation(
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = duration,
                        delayMillis = delay,
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                typeConverter = Int.VectorConverter,
                initialValue = 0,
                targetValue = contentLayoutInfo.containerWidth - contentLayoutInfo.width
            )

            val startTime = withFrameNanos { it }
            do {
                val playTime = withFrameNanos { it } - startTime
                offset = (animation.getValueFromNanos(playTime))
            } while (!animation.isFinishedFromNanos(playTime))

            delay(delay.toLong())
        } while (true)
    }

    Layout(modifier = modifier.clipToBounds(), content = children) { measurables, constraints ->
        require(measurables.size == 1)

        val infiniteWidthConstraints = constraints.copy(maxWidth = Int.MAX_VALUE)
        val measuredContent = measurables.first().measure(infiniteWidthConstraints)

        if (measuredContent.width >= constraints.maxWidth) {
            contentLayoutInfoState.value =
                ContentLayoutInfo(measuredContent.width, constraints.maxWidth)
        }

        layout(constraints.maxWidth, measuredContent.height) {
            measuredContent.place(offset, 0)
        }
    }
}
