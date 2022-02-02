package com.fintecsystems.xs2awizard.helper

import android.content.Context
import android.os.Build
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object Crypto {
    private fun createMasterKey(context: Context): MasterKey = MasterKey.Builder(context).apply {
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        setUserAuthenticationRequired(true, 30)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setRequestStrongBoxBacked(true)
        }
    }.build()

    fun createEncryptedSharedPreferences(
        context: Context,
        fileName: String,
        masterKey: MasterKey = createMasterKey(context)
    ) = EncryptedSharedPreferences.create(
        context,
        fileName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun openBiometricPrompt(
        fragmentActivity: FragmentActivity,
        title: String,
        description: String,
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
                .setAllowedAuthenticators(allowedAuthenticators)
                .build()

            authenticate(promptInfo)
        }

    }
}