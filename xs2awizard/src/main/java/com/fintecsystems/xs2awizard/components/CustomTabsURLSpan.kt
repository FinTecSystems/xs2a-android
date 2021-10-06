package com.fintecsystems.xs2awizard.components

import android.net.Uri
import android.os.Build
import android.os.Parcel
import android.text.style.URLSpan
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import java.lang.Exception


/**
 * Extension of URLSpan which opens an Custom Tab instead of the Standard Browser.
 */
class CustomTabsURLSpan : URLSpan {
    constructor(url: String) : super(url)
    constructor(src: Parcel) : super(src)

    // https://stackoverflow.com/a/36358745
    override fun onClick(widget: View) {
        try {
            // Custom Tabs are only available since Android 4.1.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                CustomTabsIntent.Builder().build().launchUrl(widget.context, Uri.parse(url))
            } else {
                // Just open the url in the standard Browser.
                super.onClick(widget)
            }
        } catch (e: Exception) {
            // It seems like we're on or above Android 4.1 but there is no Browser with Custom Tab support installed.
            e.printStackTrace()

            // Just open the url in the standard Browser.
            super.onClick(widget)
        }
    }
}