package com.fintecsystems.xs2awizard.helper

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.view.ContextThemeWrapper
import com.fintecsystems.xs2awizard.R
import java.util.concurrent.atomic.AtomicInteger


object Utils {
    private val sNextGeneratedId: AtomicInteger = AtomicInteger(1)

    // https://peacecodemonk.wordpress.com/2016/01/12/generating-id-for-view-elements-programatically-for-android/
    /**
     * Generate a value suitable for use in [.setId].
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    fun generateViewId(): Int {
        while (true) {
            val result: Int = sNextGeneratedId.get()
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            var newValue = result + 1
            if (newValue > 0x00FFFFFF) newValue = 1 // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result
            }
        }
    }

    /**
     * Decode Base64-String into Bitmap
     *
     * @param base64String
     * @return The decoded Bitmap
     */
    fun decodeBase64Image(base64String: String): Bitmap {
        val cleanImage: String = base64String
            .replace("data:image/png;base64,", "")
            .replace("data:image/jpeg;base64,", "")
            .replace("data:image/gif;base64,", "")

        val imageDataBytes = Base64.decode(cleanImage, Base64.DEFAULT)

        return BitmapFactory.decodeByteArray(imageDataBytes, 0, imageDataBytes.size)
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
     * Clones provided inflater and applies provided theme onto it.
     *
     * @param inflater to be cloned inflater.
     * @param context context used for retrieving theme.
     * @param styleRes id to the style res.
     *
     * @return the themed layout inflater.
     */
    fun getThemedInflater(
        inflater: LayoutInflater,
        context: Context,
        styleRes: Int?
    ): LayoutInflater = inflater.cloneInContext(
        getThemedContext(context, styleRes)
    )

    /**
     * Wraps context with theme
     *
     * @param context context used for retrieving theme.
     * @param styleRes id to the style res.
     * @param styleBaseRes id to the base style res.
     *
     * @return the themed context.
     */
    fun getThemedContext(
        context: Context,
        styleRes: Int?,
        styleBaseRes: Int = R.style.XS2ATheme_Base
    ): Context =
        ContextThemeWrapper(context, styleBaseRes).apply {
            if (styleRes != null) {
                theme.applyStyle(styleRes, true)
            }
        }
}