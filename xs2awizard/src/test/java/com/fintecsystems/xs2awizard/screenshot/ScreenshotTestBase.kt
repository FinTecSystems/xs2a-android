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
 * Renders [content] twice — once wrapped in [XS2ATheme.light] and once in [XS2ATheme.dark] — and
 * captures a screenshot for each.
 *
 * Generated images land in `src/test/snapshots/` under the names `{name}_light.png` and
 * `{name}_dark.png` so they can be committed and tracked via Git LFS.
 */
internal fun ComposeContentTestRule.captureForThemes(
    name: String,
    content: @Composable () -> Unit,
) {
    listOf(
        XS2ATheme.light to "light",
        XS2ATheme.dark to "dark",
    ).forEach { (theme, suffix) ->
        setContent {
            XS2ATheme(xS2ATheme = theme) { content() }
        }
        onRoot().captureRoboImage("$SNAPSHOTS_DIR/${name}_$suffix.png")
    }
}
