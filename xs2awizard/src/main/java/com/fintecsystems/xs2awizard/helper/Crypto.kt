package com.fintecsystems.xs2awizard.helper

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Helper object for [EncryptedSharedPreferences] related tasks.
 */
@RequiresApi(Build.VERSION_CODES.M)
object Crypto {
    /**
     * Get's or creates a new Master Key to use with [EncryptedSharedPreferences].
     *
     * @param context - Context to use.
     * @param keyAlias - Name of the key.
     *
     * @return New or existing Master Key.
     */
    fun createMasterKey(context: Context, keyAlias: String): MasterKey =
        MasterKey.Builder(context, keyAlias).apply {
            setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            setUserAuthenticationRequired(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setRequestStrongBoxBacked(true)
            }
        }.build()

    /**
     * Checks if the device has biometrics and if it's set.
     *
     * @param context - Context to use.
     *
     * @return true if device is secure.
     */
    fun isDeviceSecure(context: Context) = BiometricManager.from(context)
        .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

    /**
     * Creates a [EncryptedSharedPreferences] instance.
     *
     * @param context - Context to use.
     * @param fileName - Name of the shared-preferences-file.
     * @param masterKey - Key do d-/encrypt data.
     *
     * @return Created instance.
     */
    fun createEncryptedSharedPreferences(
        context: Context,
        fileName: String,
        masterKey: MasterKey
    ) = EncryptedSharedPreferences.create(
        context,
        fileName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Opens an BiometricPrompt, which will call [onSuccess] or [onError] depending on the result of
     * the biometric operation.
     *
     * @param fragmentActivity - Reference to the current [FragmentActivity].
     * @param title - Title of the prompt.
     * @param description - Description of the prompt.
     * @param negativeText - Text of the abort button.
     * @param allowedAuthenticators - Bitmask of all allowed Authenticators. See [BiometricManager.Authenticators] for more.
     * @param onSuccess - Success callback.
     * @param onError - Error/Abort callback.
     */
    fun openBiometricPrompt(
        fragmentActivity: FragmentActivity,
        title: String,
        description: String,
        negativeText: String,
        allowedAuthenticators: Int,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit
    ) {
        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                super.onAuthenticationSucceeded(result)

                onSuccess(result)
            }

            override fun onAuthenticationError(
                errorCode: Int, errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)

                onError(errorCode, errString)
            }
        }

        BiometricPrompt(
            fragmentActivity,
            authenticationCallback
        ).apply {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setDescription(description)
                .setNegativeButtonText(negativeText)
                .setAllowedAuthenticators(allowedAuthenticators)
                .build()

            authenticate(promptInfo)
        }

    }
}