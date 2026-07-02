package com.fintecsystems.xs2awizard.screenshot

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.lifecycle.SavedStateHandle
import androidx.test.core.app.ApplicationProvider
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.ParameterizedRobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

internal const val SNAPSHOTS_DIR = "src/test/snapshots"

/**
 * A theme variant used as a JUnit parameterized test parameter.
 *
 * The [XS2ATheme] is created lazily via [buildTheme] so that no Compose/Color code runs during
 * runner construction (before Robolectric's sandbox classloader is active).
 *
 * Add entries to [SCREENSHOT_THEMES] to cover additional themes — every test class picks them up
 * automatically without any code changes.
 */
class ScreenshotTheme(val label: String, private val createTheme: () -> XS2ATheme) {
    fun buildTheme(): XS2ATheme = createTheme()
    override fun toString() = label
}

/** Single source of truth for all theme variants exercised by screenshot tests. */
val SCREENSHOT_THEMES = listOf(
    ScreenshotTheme("light") { XS2ATheme.light },
    ScreenshotTheme("dark") { XS2ATheme.dark },
)

/**
 * Creates a real [XS2AWizardViewModel] backed by Robolectric's Application.
 * No network activity starts until [XS2AWizardViewModel.onStart] is called, which we never do in
 * screenshot tests.
 */
fun createViewModel(): XS2AWizardViewModel =
    XS2AWizardViewModel(
        ApplicationProvider.getApplicationContext<Application>(),
        SavedStateHandle(),
    )

/**
 * Renders [content] wrapped in [screenshotTheme] and captures a screenshot to
 * `src/test/snapshots/{baseName}_{theme.label}.png`.
 *
 * The theme label is appended automatically, so callers only supply the base name
 * (e.g. `"abort_line"`), keeping snapshot paths consistent regardless of how many themes exist.
 */
fun ComposeContentTestRule.captureForTheme(
    baseName: String,
    screenshotTheme: ScreenshotTheme,
    content: @Composable () -> Unit,
) {
    setContent {
        XS2ATheme(xS2ATheme = screenshotTheme.buildTheme()) { content() }
    }
    onRoot().captureRoboImage("$SNAPSHOTS_DIR/${baseName}_${screenshotTheme.label}.png")
}

/**
 * Base class for all screenshot tests.
 *
 * Centralises the runner, SDK config, graphics mode, parameterisation over [SCREENSHOT_THEMES],
 * and the Compose rule — so individual test classes only need their component content and
 * `@Test` methods.
 *
 * To add a new theme, add a single entry to [SCREENSHOT_THEMES]; every subclass picks it up
 * automatically.
 *
 * Tests with a single content variant should extend [SingleContentScreenshotTest] instead,
 * which reduces them further to just [SingleContentScreenshotTest.baseName] and
 * [SingleContentScreenshotTest.Content].
 */
@RunWith(ParameterizedRobolectricTestRunner::class)
@Config(sdk = [34])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
abstract class ScreenshotTestBase(protected val screenshotTheme: ScreenshotTheme) {
    companion object {
        @JvmStatic
        @ParameterizedRobolectricTestRunner.Parameters(name = "{0}")
        fun themes() = SCREENSHOT_THEMES
    }

    @get:Rule
    val composeRule = createComposeRule()
}

/**
 * Specialisation of [ScreenshotTestBase] for components that need exactly one screenshot per theme.
 *
 * Subclasses only implement [baseName] (the snapshot file stem, e.g. `"abort_line"`) and
 * [Content] (the composable to render). The `@Test` method and all infrastructure are inherited.
 *
 * Components that render multiple distinct states (e.g. empty vs. invalid) should extend
 * [ScreenshotTestBase] directly and declare their own `@Test` methods.
 */
abstract class SingleContentScreenshotTest(screenshotTheme: ScreenshotTheme) : ScreenshotTestBase(screenshotTheme) {
    abstract val baseName: String

    @Composable
    abstract fun Content()

    @Test
    fun screenshot() = composeRule.captureForTheme(baseName, screenshotTheme) { Content() }
}
