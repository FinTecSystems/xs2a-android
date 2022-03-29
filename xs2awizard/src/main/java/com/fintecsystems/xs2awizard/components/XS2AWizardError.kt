package com.fintecsystems.xs2awizard.components

import kotlinx.serialization.Serializable

/**
 * Enum-Like-Class to differentiate the different Wizard-Errors.
 */
@Serializable
sealed class XS2AWizardError(val code: String, val recoverable: Boolean) : java.io.Serializable {
    /**
    Login to bank failed (e.g. invalid login credentials)
     */
    class LoginFailed(recoverable: Boolean) : XS2AWizardError("login_failed", recoverable)

    /**
    The customer's session has timed out.
     */
    class SessionTimeout(recoverable: Boolean) : XS2AWizardError("session_timeout", recoverable)

    /**
    User entered invalid TAN.
     */
    class TanFailed(recoverable: Boolean) : XS2AWizardError("tan_failed", recoverable)

    /**
    An unknown or unspecified error occurred.
     */
    class TechError(recoverable: Boolean) : XS2AWizardError("tech_error", recoverable)

    /**
    An error occurred using testmode settings.
     */
    class TestmodeError(recoverable: Boolean) : XS2AWizardError("testmode_error", recoverable)

    /**
    A transaction is not possible for various reasons.
     */
    class TransNotPossible(recoverable: Boolean) :
        XS2AWizardError("trans_not_possible", recoverable)

    /**
    Validation error (e.g. entered letters instead of numbers).
     */
    class ValidationFailed(recoverable: Boolean) : XS2AWizardError("validation_failed", recoverable)

    /**
    A different error occurred.
     */
    class Other(code: String, recoverable: Boolean) : XS2AWizardError(code, recoverable)

    companion object {
        fun getRelevantError(code: String, recoverable: Boolean) = when (code) {
            "login_failed" -> LoginFailed(recoverable)
            "session_timeout" -> SessionTimeout(recoverable)
            "tan_failed" -> TanFailed(recoverable)
            "tech_error" -> TechError(recoverable)
            "testmode_error" -> TestmodeError(recoverable)
            "trans_not_possible" -> TransNotPossible(recoverable)
            "validation_failed" -> ValidationFailed(recoverable)
            else -> Other(code, recoverable)
        }
    }
}
