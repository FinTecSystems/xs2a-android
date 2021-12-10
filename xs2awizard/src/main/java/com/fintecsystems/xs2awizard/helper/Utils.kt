package com.fintecsystems.xs2awizard.helper

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.fintecsystems.xs2awizard.R


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

    // https://stackoverflow.com/a/28359074
    /**
     * Hides the soft keyboard
     *
     * @param activity the [Activity] context.
     */
    fun hideSoftKeyboard(activity: Activity?) {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE)

        if (inputMethodManager != null && activity.currentFocus?.windowToken != null) {
            (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
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
}