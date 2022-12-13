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

In case you need to know if the current form is the bank search or first login screen, you can use the following convenience methods:
 - `XS2AWizardViewModel.isBankSearch`
 - `XS2AWizardViewModel.isLogin`
 
Refer to [Accessing the ViewModel](#accessing-the-viewmodel) for accessing these methods.

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

> For interoperability reasons wrapper classes are excusively used for the theme.

```kotlin
val theme = XS2ATheme(
    tintColor = XS2AColor("#ff0000"),
    backgroundColor = XS2AColor("#00ff00"),
    submitButtonStyle = ButtonStyle(
        backgroundColor = XS2AColor("#ffffff"),
        textColor = XS2AColor("#000000")
    )

    // ... Other arguments
)

// Now pass the theme

// Compose
XS2AWizard(
    // ...
    theme = theme
)

//Fragment
XS2AWizardFragment(
    // ...
    theme = theme
)
```

### Typography

Instead of within `XS2ATheme` a custom font has to be defined differently depending on the Framework used.

```kotlin
// Compose
XS2AWizard(
    // ...
    typography = Typography(
        defaultFontFamily = FontFamily.Default
    )
)

//Fragment
XS2AWizardFragment(
    // ...
    fontResId = <id-of-your-font>
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

## License

Please note that this mobile SDK is subject to the MIT license. MIT license does not apply to the logo of Tink Germany GmbH, the terms of use and the privacy policy of Tink Germany GmbH. The license terms of the logo of Tink Germany GmbH, the terms of use and the privacy policy of Tink Germany GmbH are included in the LICENSE as Tink Germany LICENSE.
