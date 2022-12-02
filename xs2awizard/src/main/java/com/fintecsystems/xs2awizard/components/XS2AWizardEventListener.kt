package com.fintecsystems.xs2awizard.components

interface XS2AWizardEventListener {
    /**
     * Called when the session is finished successfully.
     *
     * @param credentials When using a XS2A.API session with shared_credentials set to true, the parameter will be
     *                    the shared credential, null otherwise.
     */
    fun onFinish(credentials: String?)

    /**
     * Called when the user aborted the session.
     */
    fun onAbort()

    /**
     * Called when a Session-Error occurred.
     *
     * @param xs2aWizardError
     */
    fun onError(xs2aWizardError: XS2AWizardError)

    /**
     * Called when a Network-Error occurred.
     */
    fun onNetworkError()

    /**
     * Called when a new Wizard-Step is reached.
     */
    fun onStep(newStep: XS2AWizardStep)

    /**
     * Called when the Back-Button of the form is pressed.
     */
    fun onBack()
}