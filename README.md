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
    // A technical error occurred.
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

<details>
    <summary>The following attributes are styleable:</summary>

```
xs2a_tintColor
xs2a_textColor
xs2a_fontFamily

xs2a_textInput_backgroundColor
xs2a_textInputShapeAppearance // Refer to "Shape customization"

xs2a_placeholderColor
xs2a_backgroundColor

xs2a_loadingIndicatorColor // Is xs2a_tintColor if left empty
xs2a_loadingIndicatorBackgroundColor


xs2a_paragraph_cornerRadius
xs2a_paragraph_cornerRadiusTopLeft
xs2a_paragraph_cornerRadiusTopRight
xs2a_paragraph_cornerRadiusBottomLeft
xs2a_paragraph_cornerRadiusBottomRight

xs2a_paragraph_containerMarginHorizontal

xs2a_paragraph_containerPadding
xs2a_paragraph_containerPaddingHorizontal
xs2a_paragraph_containerPaddingVertical

xs2a_paragraph_background_color
xs2a_paragraph_text_color

xs2a_paragraph_error_background_color
xs2a_paragraph_error_text_color

xs2a_paragraph_info_background_color
xs2a_paragraph_info_text_color

xs2a_paragraph_warning_background_color
xs2a_paragraph_warning_text_color


xs2a_buttonShapeAppearance // Refer to "Shape customization"

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

xs2a_webview_iconColor
xs2a_webview_backgroundColor
xs2a_webview_borderColor
xs2a_webview_textColor
```
</details>

### Shape customization

To customize the look of the Buttons and Text-Inputs you can use `xs2a_buttonShapeAppearance` and `xs2a_textInputShapeAppearance`.
Please refer to the official [material documentation](https://material.io/develop/android/theming/shape) for all available
properties.

<details>
    <summary>Example</summary>

`res/values/styles.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="CustomXS2ATheme" parent="XS2ATheme">
        <item name="xs2a_buttonShapeAppearance">@style/CustomXS2ATheme.ButtonShapeAppearance</item>
        <item name="xs2a_textInputShapeAppearance">@style/CustomXS2ATheme.InputShapeAppearance</item>
    </style>
    <style name="CustomXS2ATheme.ButtonShapeAppearance">
        <item name="cornerSize">20dp</item>
    </style>
    <style name="CustomXS2ATheme.InputShapeAppearance">
        <item name="cornerSize">20dp</item>
        <item name="cornerFamily">cut</item>
    </style>
</resources>
```
</details>

### Paragraph customization

Per default the background of the paragraph-element stretches the entire width of the container.
![image](https://user-images.githubusercontent.com/54634184/141505929-b2b8a2b1-9b7b-432a-941d-b04a62187db4.png)

If you prefer to change that, you can customize that to your liking.<br>
You're able to change the corner radius of the background, the horizontal margin of the container
and the padding of the content.

Please mind that `xs2a_paragraph_containerMarginHorizontal` ignores normal paragraphs and only
applies to error-, info- and warning-paragraphs.

```
xs2a_paragraph_cornerRadius
xs2a_paragraph_cornerRadiusTopLeft
xs2a_paragraph_cornerRadiusTopRight
xs2a_paragraph_cornerRadiusBottomLeft
xs2a_paragraph_cornerRadiusBottomRight

xs2a_paragraph_containerMarginHorizontal

xs2a_paragraph_containerPadding
xs2a_paragraph_containerPaddingHorizontal
xs2a_paragraph_containerPaddingVertical
```

<details>
    <summary>Example</summary>

![image](https://user-images.githubusercontent.com/54634184/141506008-182238ce-2580-46a9-aabe-1ed574f46436.png)
    
`res/values/styles.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="CustomXS2ATheme" parent="XS2ATheme">
        <item name="xs2a_paragraph_containerMarginHorizontal">10dp</item>
        <item name="xs2a_paragraph_containerPaddingVertical">5dp</item>
        <item name="xs2a_paragraph_cornerRadius">4dp</item>
    </style>
</resources>
```
</details>

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
