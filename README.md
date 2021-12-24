![header](https://fintecsystems.com/android-sdk-header.jpg)
# XS2A Android - Native Android SDK for XS2A

## Demo Screencast
<img src="https://fintecsystems.com/android_sdk_testbank_screencast.webp" alt="Screencast Demo" height="400"/>

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/com.fintecsystems/xs2awizard.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.fintecsystems%22%20AND%20a:%22xs2awizard%22)

Add the dependency to your `build.gradle`.

```gradle
dependencies {
    // ... Other Dependencies

    implementation "com.fintecsystems:xs2awizard:<version>"
}
```

### Update proguard rules

Since we use reflection for our serialization, please add the following to your 
`proguard-rules.pro` file:

```
-keep class com.fintecsystems.xs2awizard.** { *; }
-keepclassmembers class com.fintecsystems.xs2awizard.** { *; }
```

## Initialization

1. Setup a configuration object.

```kotlin
val xs2aConfig = XS2AWizardConfig(
    sessionKey = "your-session-key", // Generated on your backend.
    backendURL = "http://localhost:8000", // Optional
    onFinish = ::onFinish, // See Callbacks
    onAbort = ::onAbort, // See Callbacks
    onError = ::onError, // Optional, see Callbacks
    onNetworkError = ::onNetworkError, // Optional, see Callbacks
    theme = XS2AThemeLight // Optional, see Customization
)
```

<details>
    <summary>Fragment</summary>

```kotlin
// 2. Initialize the wizard.
val xs2aWizard = XS2AWizardFragment(xs2aConfig)

// 3. Add the wizard to your view.
supportFragmentManager.beginTransaction().apply {
    add(R.id.xs2a_container, xs2aWizard)
    commit()
}
```
</details>

<details>
    <summary>Jetpack Compose</summary>

```kotlin
// 2. Call the composable.
XS2AWizard(xS2AWizardConfig = xs2aConfig)
```
</details>

## Callbacks

```kotlin
fun onFinish(credentials: String?) {
    // credentials will only be set for XS2A.API with connection sync_mode set to "shared"
    // credentials will contain the shared credentials then.
    // e.g. present a success view.
}
```

```kotlin
fun onAbort() {
    // The user aborted the transaction.
    // e.g. present a abort view.
}
```

```kotlin
fun onNetworkError() {
    // A network error occured.
    // e.g. present a error message.
}
```

<details>
    <summary>Error Codes</summary>

```kotlin
sealed class XS2AWizardError(
    val code: String, // The error code, only relevant with "Other"
    val recoverable: Boolean, // Can this transaction continue?
) {
    /**
    Login to bank failed (e.g. invalid login credentials)
     */
    class LoginFailed(recoverable: Boolean) : XS2AWizardError

    /**
    The customer's session has timed out.
     */
    class SessionTimeout(recoverable: Boolean) : XS2AWizardError

    /**
    User entered invalid TAN.
     */
    class TanFailed(recoverable: Boolean) : XS2AWizardError

    /**
    An unknown or unspecified error occurred.
     */
    class TechError(recoverable: Boolean) : XS2AWizardError

    /**
    An error occurred using testmode settings.
     */
    class TestmodeError(recoverable: Boolean) : XS2AWizardError

    /**
    A transaction is not possible for various reasons.
     */
    class TransNotPossible(recoverable: Boolean) : XS2AWizardError

    /**
    Validation error (e.g. entered letters instead of numbers).
     */
    class ValidationFailed(recoverable: Boolean) : XS2AWizardError

    /**
    A different error occurred.
     */
    class Other(code: String, recoverable: Boolean) : XS2AWizardError
}
```
</details>

```kotlin
fun onError(xs2aWizardError: XS2AWizardError) {
    // A Session error occurred.
    // e.g. present a error message.
}
```

## Customization

You are able to define your own theme for the wizard.
An example would be the following:

```kotlin
val theme = object : IXS2ATheme by XS2AThemeLight {
    override val tintColor: Color = Color.Red
    override val backgroundColor: Color = Color("#FFFFFF".toColorInt())

    override val submitButtonStyle: ButtonStyle = ButtonStyle(
        backgroundColor = Color.Blue,
        textColor = Color.White
    )
}
```

It's recommended to use `XS2AThemeLight` or `XS2AThemeLight` as your Base, please use [Delegation](https://kotlinlang.org/docs/delegation.html) to further customize your theme.

Please refer to [here](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/IXS2ATheme.kt) for all customizable attributes.

### Support customization

In non-compose projects you won't have access to classes like `Color`, `Shape` and `FontFamily`.

For these please use the support classes
  - [`SupportColor`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/support/SupportColor.kt)
  - [`SupportShape`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/support/SupportShape.kt)
  - [`SupportFontFamily`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/support/SupportFontFamily.kt)

And instead of creating an implementation of `IXS2ATheme` yourself, create an instance of [`XS2ASupportTheme`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/support/XS2ASupportTheme.kt) and pass your overrides to the constructor.

```kotlin
val theme = XS2ASupportTheme(
    tintColor = SupportColor("#FF0000"),
    backgroundColor = SupportColor(255, 255, 255, 255),
    submitButtonStyle = ButtonStyle(
        backgroundColor = SupportColor("#0000FF"), // Provide a color string
        textColor = SupportColor(android.graphics.Color.WHITE) // Or Color-Int
    )
)
```

## Example (Kotlin)
<details>
    <summary>Style file</summary>

`res/values/styles.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="CustomXS2ATheme" parent="XS2ATheme">
        <item name="xs2a_tintColor">#E91E63</item>
        <item name="xs2a_placeholderColor">#E91E63</item>

        <item name="xs2a_button_submit_background_color">#E91E63</item>
        <item name="xs2a_button_submit_text_color">#000</item>
        <item name="xs2a_button_abort_background_color">#E91E63</item>
        <item name="xs2a_button_abort_text_color">#000</item>
        <item name="xs2a_button_back_background_color">#673AB7</item>
        <item name="xs2a_button_back_text_color">#fff</item>
        <item name="xs2a_button_restart_background_color">#ffff00</item>
        <item name="xs2a_button_restart_text_color">#000</item>
        <item name="xs2a_button_redirect_background_color">#000</item>
        <item name="xs2a_button_redirect_text_color">#fff</item>
    </style>
</resources>
```
</details>

<details>
    <summary>Layout file</summary>

`res/layout/activity_main.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.fintecsystems.xs2awizard_example.MainActivity">
    <FrameLayout
        android:id="@+id/xs2a_wizard_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
    </FrameLayout>
</FrameLayout>
```
</details>

<details>
    <summary>Code File</summary>

`java/com/fintecsystems/xs2awizard_example/MainActivity.kt`
```kotlin
package com.fintecsystems.xs2awizard_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.XS2AWizard.XS2AWizardConfig


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xS2AWizardConfig = XS2AWizardConfig(
            sessionKey = "your-session-key",
            styleResId = R.style.CustomXS2ATheme,
            onFinish = ::onFinish,
            onAbort = ::onAbort,
        )

        val xs2aWizard = XS2AWizard(xS2AWizardConfig)

        supportFragmentManager.beginTransaction().let {
            it.add(R.id.xs2a_wizard_container, xs2aWizard)
            it.commit()
        }
    }

    private fun onFinish(credentials: String?) {
        Log.d(TAG, "onFinish: $credentials")
    }

    private fun onAbort() {
        Log.d(TAG, "onAbort")
    }
}
```
</details>

## License

Please note that this mobile SDK is subject to the MIT license. MIT license does not apply to the logo of FinTecSystems GmbH, the terms of use and the privacy policy of FinTecSystems GmbH. The license terms of the logo of FinTecSystems GmbH, the terms of use and the privacy policy of FinTecSystems GmbH are included in the LICENSE as FTS LICENSE.
