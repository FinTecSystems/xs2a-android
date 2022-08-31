package com.fintecsystems.xs2awizard.components

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// https://proandroiddev.com/jetpack-compose-interactionsources-the-ripple-effect-and-you-f451b60fcd37
fun Modifier.mutateInteractionSource(
    interactionSource: MutableInteractionSource,
    enabled: Boolean = true,
    maxDelayMillis: Long = 1000,
    minDelayMillis: Long = 5,
    delayDecayFactor: Float = .20f
): Modifier = pointerInput(interactionSource, enabled) {
    if (!enabled) return@pointerInput

    forEachGesture {
        coroutineScope {
            awaitPointerEventScope {
                val down = awaitFirstDown(requireUnconsumed = false)
                // Create a down press interaction
                val downPress = PressInteraction.Press(down.position)
                val heldButtonJob = launch {
                    // Send the press through the interaction source
                    interactionSource.emit(downPress)
                    var currentDelayMillis = maxDelayMillis
                    while (enabled && down.pressed) {
                        delay(currentDelayMillis)
                        val nextMillis =
                            currentDelayMillis - (currentDelayMillis * delayDecayFactor)
                        currentDelayMillis = nextMillis.toLong().coerceAtLeast(minDelayMillis)
                    }
                }
                val up = waitForUpOrCancellation()
                heldButtonJob.cancel()
                // Determine whether a cancel or release occurred, and create the interaction
                val releaseOrCancel = when (up) {
                    null -> PressInteraction.Cancel(downPress)
                    else -> PressInteraction.Release(downPress)
                }
                launch {
                    // Send the result through the interaction source
                    interactionSource.emit(releaseOrCancel)
                }
            }
        }
    }
}