package com.fintecsystems.xs2awizard.components

/**
 * Enum-Like-Class to differentiate the different Wizard-Steps.
 */
sealed class XS2AWizardStep(val stepName: String) {
    /**
     * Step for entering a TAN
     */
    class Tan : XS2AWizardStep("tan")

    /**
     * Step for the Bank-Search
     */
    class Bank : XS2AWizardStep("bank")

    /**
     * Step for the Account-Selection
     */
    class Account : XS2AWizardStep("account")

    /**
     * Step for the Login-Page
     */
    class Login : XS2AWizardStep("login")

    /**
     * Used when we're currently in an unknown step.
     */
    class Other(stepName: String) : XS2AWizardStep(stepName)

    companion object {
        fun getRelevantStep(stepName: String?): XS2AWizardStep? {
            if (stepName == null) return null

            return when (stepName) {
                "tan" -> Tan()
                "bank" -> Bank()
                "account" -> Account()
                "login" -> Login()
                else -> Other(stepName)
            }
        }
    }
}
