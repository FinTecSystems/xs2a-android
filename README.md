![header](https://demo.xs2a.com/img/android-sdk-header.png)

# XS2A Android - Native Android SDK for XS2A
![License](https://img.shields.io/badge/License-MIT%20%2B%20File%20LICENSE-427783.svg)
![Platform](https://img.shields.io/badge/Platform-Android-427783.svg)
![Languages](https://img.shields.io/badge/Languages-Kotlin-427783.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.fintecsystems/xs2awizard.svg?label=Maven%20Central&color=427783)](https://search.maven.org/search?q=g:%22com.fintecsystems%22%20AND%20a:%22xs2awizard%22)

## Demo Screencast

A demo screencast of the test bank login flow can be found [here](https://demo.xs2a.com/img/android-screencast.webp).

## Installation

> Your projects `minSDK` has to be 21 or higher.

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

## Migration from v3

Please refer to the [v4 release notes](https://github.com/FinTecSystems/xs2a-android/releases/tag/4.0.0).


## Initialization

```kotlin
// Compose
// Just call composable
XS2AWizard(
    modifier = Modifier.fillMaxSize(), // Recommended
    sessionKey = <your-session-key>, // Required
)

//Fragment
// Create Fragment
val xs2aWizard = XS2AWizardFragment(
    sessionKey = <your-session-key> // Required
)

// Commit fragment
supportFragmentManager.beginTransaction().let {
    it.add(<wizard-container>, xs2aWizard)
    it.commit()
}
```

## Callbacks

### Jetpack Compose

Pass a `XS2AWizardCallbackListener`-instance to the Composable.

```kotlin
val callbackListener = object : XS2AWizardCallbackListener {
    /**
     * interface implementation
     */
}

XS2AWizard(
    modifier = Modifier.fillMaxSize(),
    sessionKey = xS2ASessionKey!!,
    callbackListener = callbackListener,
)
```

### Fragment

Register [Fragment Callbacks](https://developer.android.com/guide/fragments/communicate#fragment-result) to listen for callbacks.

You may use the keys statically provided by `XS2AWizardFragment`.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    
    supportFragmentManager.setFragmentResultListener(
        XS2AWizardFragment.onFinishKey,
        this // May be your Activity
    ) { requestKey, bundle ->
        if (requestKey == XS2AWizardFragment.onFinishKey) {
            // Perform onFinish action
        }
    }
    
    // ...
}
```

You may also use the `FragmentResultOwner.setXs2aCallbacks` convencience method. Similar to the Jetpack Compose implementation this expects an `XS2AWizardCallbackListener`-instance.

Use `FragmentResultOwner.clearXs2aCallbacks` to clear them.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // ...

    val callbackListener = object : XS2AWizardCallbackListener {
        /**
            * interface implementation
            */
    }

    supportFragmentManager.setXs2aCallbacks(
        this, // May be your Activity
        callbackListener
    )

    // ...
}

// ...

override fun onDestroy() {
    // ...
    
    // Clear callbacks
    supportFragmentManager.clearXs2aCallbacks()
}
```

### Wizard-Step handling

> In case you need to know if the current form is the bank search or first login screen, you can use the following convenience methods:
> - `XS2AWizardViewModel.isBankSearch`
> - `XS2AWizardViewModel.isLogin`
> 
> Refer to [Accessing the ViewModel](#accessing-the-viewmodel) for accessing these methods.

## Credentials saving

> This feature must be activated on your XS2A-Account.<br>
> For more details on this feature contact the technical support of Tink Germany.

On API-Level 23+ the user is able to decide to save their credentials for use in future transactions.

> Please note that this does *not* raise the `minSDK` to 23.<br>
> Devices using `API-Level` 21/22 will ignore this feature.

The user must have at least one fingerprint registered, otherwise this feature will be ignored and he will not be asked to save/load his credentials.

### Deletion

You'll be able to delete the credentials by calling
`XS2AWizardViewModel.clearCredentials`.

> You should provide a button that calls this function e.g. in your settings.

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

> It's recommended to use `XS2AThemeLight` or `XS2AThemeDark` as your Base, please use [Delegation](https://kotlinlang.org/docs/delegation.html) to further customize your theme.

Please refer to [here](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/IXS2ATheme.kt) for all customizable attributes.

### Support customization

In non-compose projects you won't have access to classes like `Color`, `Shape` and `FontFamily`.

For these please use the support classes
  - [`SupportColor`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/interop/SupportColor.kt)
  - [`SupportShape`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/interop/SupportShape.kt)
  - [`SupportFontFamily`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/interop/SupportFontFamily.kt)

> Because `ButtonStyle`, `ParagraphStyle`, `PaddingMarginConfiguration` and `LogoVariation` are custom classes, they already have proper non-compose support.

And instead of creating an implementation of `IXS2ATheme` yourself, create an instance of [`XS2ASupportTheme`](xs2awizard/src/main/java/com/fintecsystems/xs2awizard/components/theme/interop/XS2ASupportTheme.kt) and pass your overrides to the constructor.

```kotlin
val theme = XS2ASupportTheme(
    tintColor = SupportColor("#FF0000"),
    backgroundColor = SupportColor(255, 255, 255, 255),
    submitButtonStyle = ButtonStyle(
        backgroundColor = SupportColor("#0000FF"),
        textColor = SupportColor(android.graphics.Color.WHITE)
    ),
    buttonShape = SupportShape(
        45,
        SupportShape.ShapeType.ROUNDED
    ),
)
```

## Accessing the ViewModel
It is possible to access the ViewModel to call form functions like `goBack` or `abort`.

> The ViewModel dependencies are required to access the ViewModel.<br>
> See [here](https://developer.android.com/topic/libraries/architecture/viewmodel) for more information.

### Compose
Pass your ViewModel, using the `viewModels` function, and pass it to the `XS2AWizard`.

> It is possible to freely define the ViewModel-Scope.
> Please refer to [this answer](https://stackoverflow.com/a/68971296) for more information.

#### Example

```kotlin
val xS2AWizardViewModel = viewModel<XS2AWizardViewModel>()

Column {
    Button({
        // Access form functions
        xS2AWizardViewModel.abort()
    }) {
        Text("Abort")
    }

    XS2AWizard(
        xS2AWizardConfig = <your-config>,
        xs2aWizardViewModel = xS2AWizardViewModel
    )
}
```

### Fragment
The ViewModel-Scope of `XS2AWizardFragment` is the Host-Activity, because of that just access the Activity's `viewModels()` directly or if you need to access it within a Fragment with `activityViewModels()`.

#### Example

```kotlin
class MainActivity : AppCompatActivity() {
    val xs2aWizardViewModel: XS2AWizardViewModel by viewModels()

    ...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.backButton).setOnClickListener {
            xs2aWizardViewModel.goBack()
        }
    }

    ...
}
```

## Example (Jetpack Compose)
<details>
    <summary>Code</summary>

`java/com/fintecsystems/xs2awizard_example/MainActivity.kt`
```kotlin
package com.fintecsystems.xs2awizard_example

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig
import com.fintecsystems.xs2awizard.components.theme.XS2AThemeLight


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val xS2AWizardConfig = XS2AWizardConfig(
                sessionKey = "your-session-key",
                onFinish = ::onFinish,
                onAbort = ::onAbort,
                theme = XS2AThemeLight,
            )

            XS2AWizard(xS2AWizardConfig = xS2AWizardConfig)
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

## Example (Fragment)
<details>
    <summary>Layout</summary>

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
    <summary>Code</summary>

`java/com/fintecsystems/xs2awizard_example/MainActivity.kt`
```kotlin
package com.fintecsystems.xs2awizard_example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig
import com.fintecsystems.xs2awizard.components.theme.XS2AThemeLight
import com.fintecsystems.xs2awizard.wrappers.XS2AWizardFragment


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val xS2AWizardConfig = XS2AWizardConfig(
            sessionKey = "your-session-key",
            onFinish = ::onFinish,
            onAbort = ::onAbort,
            theme = XS2AThemeLight,
        )

        val xs2aWizard = XS2AWizardFragment(xS2AWizardConfig)

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

Please note that this mobile SDK is subject to the MIT license. MIT license does not apply to the logo of Tink Germany GmbH, the terms of use and the privacy policy of Tink Germany GmbH. The license terms of the logo of Tink Germany GmbH, the terms of use and the privacy policy of Tink Germany GmbH are included in the LICENSE as Tink Germany LICENSE.
