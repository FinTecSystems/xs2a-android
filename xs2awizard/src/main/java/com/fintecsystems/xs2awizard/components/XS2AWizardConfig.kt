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
 *
 * @property onAbort Called when the user aborted the session.
 * @property onError Called when a Session-Error occurred.
 *                   Supplies the Error.
 * @property onNetworkError Called when a Network-Error occurred.
 * @property onStep Called when a new Wizard-Step is reached.
 *                  Supplies the new step, may be null if the step is unknown e.g. on success.
 * @property onBack Called when the Back-Button of the form is pressed.
 *                  Supplies the current step at the moment of pressing the button.
 * @property theme Theme to be used.
 *                 If null the default Light- or Dark-Theme, depending on the device settings, is used.
 */
data class XS2AWizardConfig(
    val sessionKey: String,
    val backendURL: String? = null,
    val onFinish: (String?) -> Unit,
    val onAbort: () -> Unit,
    val onError: (XS2AWizardError) -> Unit = {},
    val onNetworkError: () -> Unit = {},
    val onStep: (XS2AWizardStep?) -> Unit = {},
    val onBack: (XS2AWizardStep?) -> Unit = {},
    val theme: IXS2ATheme? = null,
)