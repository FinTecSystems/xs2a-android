package com.fintecsystems.xs2awizard.screenshot

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onRoot
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.github.takahirom.roborazzi.captureRoboImage

internal const val SNAPSHOTS_DIR = "src/test/snapshots"

/**
 * Creates a real [XS2AWizardViewModel] backed by Robolectric's Application.
 * No network activity starts until [XS2AWizardViewModel.onStart] is called, which we never do in
 * screenshot tests.
 */
internal fun createViewModel(): XS2AWizardViewModel =
    XS2AWizardViewModel(
        ApplicationProvider.getApplicationContext<Application>(),
        SavedStateHandle(),
    )

/**
 * Renders [content] wrapped in [theme] and captures a screenshot to
 * `src/test/snapshots/{name}.png`.
 *
 * Call this once per `@Test` method — one method per theme — so that verify mode failures for the
 * light and dark variants are independent and both appear in CI diff artifacts.
 */
internal fun ComposeContentTestRule.captureForTheme(
    name: String,
    theme: XS2ATheme,
    content: @Composable () -> Unit,
) {
    setContent {
        XS2ATheme(xS2ATheme = theme) { content() }
    }
    onRoot().captureRoboImage("$SNAPSHOTS_DIR/$name.png")
}
