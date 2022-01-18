package com.fintecsystems.xs2awizard.helper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.fintecsystems.xs2awizard.R
import java.util.*


object Utils {
    /**
     * Decode Base64-String into ImageBitmap
     *
     * @param base64String
     * @return The decoded Bitmap
     */
    fun decodeBase64Image(base64String: String): ImageBitmap {
        val cleanImage: String = base64String
            .replace("data:image/png;base64,", "")
            .replace("data:image/jpeg;base64,", "")
            .replace("data:image/gif;base64,", "")

        val imageDataBytes = Base64.decode(cleanImage, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size).asImageBitmap()
    }

    /**
     * Copies the supplied text to the clipboard.
     *
     * @param context context used to retrieve ClipboardManager.
     * @param text text to save to the clipboard.
     */
    fun setClipboardText(context: Context, text: String) {
        val clipboard =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(context, context.getString(R.string.link_copied), Toast.LENGTH_SHORT).show()
    }

    // https://stackoverflow.com/a/68423182
    /**
     * Returns the current activity of the context.
     *
     * @return current activity.
     */
    fun Context.getActivity(): AppCompatActivity? = when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.getActivity()
        else -> null
    }

    /**
     * Provides easy access to the DataStore instance.
     */
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "xs2a_credentials")

    private val languageWhitelist = listOf("de", "en", "fr", "it", "es")

    /**
     * Checks if the language matches the device language.
     * If the language doesn't match, the device language will be checked against our
     * supported languages.
     *
     * @param language language to check.
     *
     * @return true if language should be changed, false otherwise.
     */
    fun checkIfLanguageNeedsToBeChanged(language: String?): Boolean {
        if (language != null) {
            val deviceLanguage = Locale.getDefault().language
            val formLanguage = Locale(language).language

            return languageWhitelist.contains(deviceLanguage) && !(deviceLanguage.equals(
                formLanguage
            ))
        }

        return false
    }
}