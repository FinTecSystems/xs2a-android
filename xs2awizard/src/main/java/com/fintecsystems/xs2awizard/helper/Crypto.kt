package com.fintecsystems.xs2awizard.helper

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@RequiresApi(Build.VERSION_CODES.M)
object Crypto {
    fun createMasterKey(context: Context, keyAlias: String): MasterKey = MasterKey.Builder(context, keyAlias).apply {
        setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        setUserAuthenticationRequired(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setRequestStrongBoxBacked(true)
        }
    }.build()

    fun isDeviceSecure(context: Context) = with(context.getSystemService(KeyguardManager::class.java)) {
        isDeviceSecure
    }

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