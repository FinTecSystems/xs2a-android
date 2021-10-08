![header](https://fintecsystems.com/android-sdk-header.jpg)
# XS2A Android - Native Android SDK for XS2A

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
    onError = ::onError, // See Callbacks
    styleResId = R.style.CustomXS2ATheme // See Styling
)
```

2. Initialize the wizard.

```kotlin
val xs2aWizard = XS2AWizard(xs2aConfig)
```

3. Add the wizard to your view.

```kotlin
supportFragmentManager.beginTransaction().apply {
    add(R.id.xs2a_container, xs2aWizard)
    commit()
}
```

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
data class XS2AWizardError(
    val errorCode: String,
    val messages: List<String>,
    val recoverable: Boolean, // Can this transaction be continued?
)

fun onError(xs2aWizardError: XS2AWizardError) {
    // A technical error occured.
    // e.g. present a error view.
}
```

## Styling

You are able to define your own theme for the wizard.
An example would be the following:

`res/values/styles.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="CustomXS2ATheme" parent="XS2ATheme">
        <!-- Just change the primary color -->
        <item name="xs2a_tintColor">#E91E63</item>
        <item name="xs2a_placeholderColor">#E91E63</item>
        
        <!-- Apply more styling -->
    </style>
</resources>
```

The following attributes are styleable:
```
xs2a_tintColor
xs2a_textColor

xs2a_textInput_backgroundColor

xs2a_placeholderColor
xs2a_backgroundColor

xs2a_loadingIndicatorColor // Is xs2a_tintColor if left empty
xs2a_loadingIndicatorBackgroundColor


xs2a_paragraph_background_color
xs2a_paragraph_text_color

xs2a_paragraph_error_background_color
xs2a_paragraph_error_text_color

xs2a_paragraph_info_background_color
xs2a_paragraph_info_text_color

xs2a_paragraph_warning_background_color
xs2a_paragraph_warning_text_color


xs2a_button_submit_background_color
xs2a_button_submit_text_color

xs2a_button_abort_background_color
xs2a_button_abort_text_color

xs2a_button_back_background_color
xs2a_button_back_text_color

xs2a_button_restart_background_color
xs2a_button_restart_text_color

xs2a_button_redirect_background_color
xs2a_button_redirect_text_color
```

### Example (Kotlin)
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
