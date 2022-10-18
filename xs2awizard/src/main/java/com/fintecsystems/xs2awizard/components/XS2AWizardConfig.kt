package com.fintecsystems.xs2awizard.components

import com.fintecsystems.xs2awizard.components.theme.IXS2ATheme

/**
 * Config of the Wizard-Instance
 *
 * @property sessionKey Session-Key to be used.
 * @property backendURL URL to the Backend.
 * @property onFinish Called when the session is finished successfully.
 *                    When using a XS2A.API session with shared_credentials set to true, the parameter will be
 *                    the shared credential, null otherwise.
 * @property onAbort Called when the user aborted the session.
 * @property onError Called when a Session-Error occurred.
 *                   Supplies the Error.
 * @property onNetworkError Called when a Network-Error occurred.
 * @property onStep Called when a new Wizard-Step is reached.
 * @property onBack Called when the Back-Button of the form is pressed.
 * @property onLoading Called when the  loading state change
 * @property theme Theme to be used.
 *                 If null the default Light- or Dark-Theme, depending on the device settings, is used.
 * @property language Wizard language. If null the device language will be used.
 * @property enableScroll If enabled, the form will add an vertical scroll component.
 *                        Disable this if the wizard is wrapped inside another scrollable view to avoid crashes.
 * @property enableBackButton If enabled, the back button of the form will be rendered.
 *                            Only disable this if you need to implement your own back button logic.
 *                            See [XS2AWizardViewModel.goBack] and [XS2AWizardViewModel.backButtonIsPresent] for your own implementation.
 */
data class XS2AWizardConfig(
    val sessionKey: String,
    val backendURL: String? = null,
    val onFinish: (String?) -> Unit,
    val onAbort: () -> Unit,
    val onError: (XS2AWizardError) -> Unit = {},
    val onNetworkError: () -> Unit = {},
    val onStep: (XS2AWizardStep) -> Unit = {},
    val onBack: () -> Unit = {},
    val onLoading: (isLoading: Boolean) -> Unit = {},
    val theme: IXS2ATheme? = null,
    val language: XS2AWizardLanguage? = null,
    val enableScroll: Boolean = true,
    val enableBackButton: Boolean = true,
)