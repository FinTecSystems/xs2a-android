package com.fintecsystems.xs2awizard.helper

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.widget.Toast
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.cast


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
    inline fun <reified T : Activity> Context.getActivity(): T? = getActivity(T::class)

    /**
     * Returns the current activity of the context.
     *
     * @param type Activity type to cast to.
     *
     * @return current activity.
     */
    fun <T : Activity> Context.getActivity(type: KClass<T>): T? {
        if (type.isInstance(this)) return type.cast(this)
        if (this is ContextWrapper) return baseContext.getActivity(type)

        return null
    }

    /**
     * Checks if the language matches the device language.
     * If the language doesn't match, the device language will be checked against our
     * supported languages.
     *
     * @param language language to check.
     *
     * @return true if language should be changed, false otherwise.
     */
    fun checkIfLanguageNeedsToBeChanged(
        language: XS2AWizardLanguage?,
        targetLanguage: XS2AWizardLanguage? = null
    ): Boolean {
        if (language != null) {
            val languageToCheck = targetLanguage ?: XS2AWizardLanguage.fromString(
                Locale.getDefault().language
            )

            return XS2AWizardLanguage.values()
                .contains(languageToCheck) && languageToCheck != language
        }

        return false
    }

    /**
     * Set to true if Device-API-Level is at least 23.
     */
    @get:ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    val isMarshmallow: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}