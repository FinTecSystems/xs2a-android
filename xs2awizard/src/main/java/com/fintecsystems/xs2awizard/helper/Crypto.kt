package com.fintecsystems.xs2awizard.helper

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.flow.firstOrNull
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

/**
 * Helper object for Crypto related tasks.
 */
@RequiresApi(Build.VERSION_CODES.M)
internal object Crypto {
    /**
     * Checks if the device has biometrics and if it's set.
     *
     * @param context - Context to use.
     *
     * @return true if device is secure.
     */
    fun isDeviceSecure(context: Context) = BiometricManager.from(context)
        .canAuthenticate(BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(KEY_GENERATOR_PROVIDER).apply {
            load(null)
        }

        // Return existing key if available
        keyStore.getKey(MASTER_KEY_ALIAS, null)?.let {
            return it as SecretKey
        }

        // Create new key if it doesn't exist
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEY_GENERATOR_PROVIDER)

        val keyGenSpec = KeyGenParameterSpec.Builder(
            MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MASTER_KEY_SIZE)
            .setUserAuthenticationRequired(false)

        // Try to use StrongBox if available, fallback to regular TEE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                keyGenSpec.setIsStrongBoxBacked(true)
                keyGenerator.init(keyGenSpec.build())
                return keyGenerator.generateKey()
            } catch (_: Exception) {
                // StrongBox not available, continue with regular TEE
                keyGenSpec.setIsStrongBoxBacked(false)
            }
        }

        keyGenerator.init(keyGenSpec.build())
        return keyGenerator.generateKey()
    }

    private val Context.xs2aCredentialsDataStore: DataStore<Preferences> by preferencesDataStore(
        name = FILE_NAME
    )

    suspend fun clearDataStore(
        context: Context,
    ) {
        context.xs2aCredentialsDataStore
            .edit {
                it.clear()
            }

        KeyStore.getInstance(KEY_GENERATOR_PROVIDER).apply {
            load(null)
            deleteEntry(MASTER_KEY_ALIAS)
        }
    }

    suspend fun dataStoreContainsProvider(
        context: Context,
        providerName: String
    ): Boolean {
        val prefix = "${providerName}_"
        return context.xs2aCredentialsDataStore
            .data
            .firstOrNull()
            ?.asMap()
            ?.any { it.key.name.startsWith(prefix) } == true
    }

    /**
     * Saves encrypted provider data to DataStore.
     *
     * @param context Application context
     * @param providerName Name of the provider
     * @param valuesToSave List of key-value pairs to encrypt and store
     * @return true if successful, false otherwise
     */
    suspend fun saveProviderDataToDataStore(
        context: Context,
        providerName: String,
        valuesToSave: List<Pair<String, String>>
    ): Boolean {
        return try {
            val secretKey = getOrCreateSecretKey()
            context.xs2aCredentialsDataStore
                .edit { settings ->
                    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                    valuesToSave.forEach { (key, value) ->
                        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

                        val encryptedData = cipher.doFinal(value.toByteArray(CHARSET))
                        val iv = cipher.iv

                        // Store IV + encrypted data
                        val combined = iv + encryptedData

                        val entryKey = byteArrayPreferencesKey("${providerName}_$key")
                        settings[entryKey] = combined
                    }
                }
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * Loads and decrypts provider data from DataStore.
     *
     * @param context Application context
     * @param providerName Name of the provider
     * @return Map of decrypted key-value pairs, or empty map if provider not found or decryption fails
     */
    suspend fun loadProviderDataFromDataStore(
        context: Context,
        providerName: String
    ): Map<String, String> {
        return try {
            val secretKey = getOrCreateSecretKey()
            val prefix = "${providerName}_"

            val dataStoreMap = context.xs2aCredentialsDataStore
                .data
                .firstOrNull()
                ?.asMap()
                ?.filterKeys { it.name.startsWith(prefix) }

            if (dataStoreMap.isNullOrEmpty()) {
                return emptyMap()
            }

            val decryptedData = dataStoreMap.mapNotNull { (key, value) ->
                if (value !is ByteArray) {
                    return@mapNotNull null
                }

                try {
                    // Validate minimum length (IV + at least some encrypted data)
                    if (value.size < GCM_IV_LENGTH + 1) {
                        return@mapNotNull null
                    }

                    // Extract IV and encrypted data
                    val iv = value.copyOfRange(0, GCM_IV_LENGTH)
                    val encryptedData = value.copyOfRange(GCM_IV_LENGTH, value.size)

                    val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
                    cipher.init(
                        Cipher.DECRYPT_MODE,
                        secretKey,
                        javax.crypto.spec.GCMParameterSpec(GCM_TAG_LENGTH, iv)
                    )

                    val decryptedValue = String(cipher.doFinal(encryptedData), CHARSET)
                    key.name.removePrefix(prefix) to decryptedValue
                } catch (_: Exception) {
                    // Decryption failed, skip this entry
                    null
                }
            }.toMap()

            decryptedData
        } catch (_: Exception) {
            emptyMap()
        }
    }

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
     *
     * @return Created [BiometricPrompt].
     */
    fun openBiometricPrompt(
        fragmentActivity: FragmentActivity,
        title: String,
        description: String,
        negativeText: String,
        allowedAuthenticators: Int,
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit
    ): BiometricPrompt {
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

        return BiometricPrompt(
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

    private const val FILE_NAME = "xs2a_credentials"
    private const val MASTER_KEY_ALIAS = "xs2a_credentials_master_key"
    private const val MASTER_KEY_SIZE = 256

    private const val KEY_GENERATOR_PROVIDER = "AndroidKeyStore"
    private const val CIPHER_TRANSFORMATION =
        "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"
    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 128
    private val CHARSET = Charsets.UTF_8
}