package com.fintecsystems.xs2awizard.helper

import android.content.Context
import android.os.Build
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
}