package com.fintecsystems.xs2awizard.components

import android.graphics.Rect
import android.text.Spannable
import android.text.Spanned
import android.text.method.TransformationMethod
import android.text.style.URLSpan
import android.view.View
import android.widget.TextView

// https://gist.github.com/NikolaDespotoski/e543b532fb6255c7e39c#file-linktransformationmethod-java
/**
 * TransformationMethod implementation to replace [URLSpan] with [CustomTabsURLSpan] within a [TextView].
 */
class LinkTransformationMethod : TransformationMethod {
    override fun getTransformation(source: CharSequence, view: View?): CharSequence {
        if (view is TextView) {
            if (view.text == null || view.text !is Spannable) {
                return source
            }

            val text = view.text as Spannable

            // Retrieve all URLSpans in the text.
            val spans = text.getSpans(0, view.length(), URLSpan::class.java)

            for (i in spans.indices.reversed()) {
                // Replace URLSpan with CustomTabsURLSpan
                val oldSpan = spans[i]
                val start = text.getSpanStart(oldSpan)
                val end = text.getSpanEnd(oldSpan)
                val url = oldSpan.url
                text.removeSpan(oldSpan)

                text.setSpan(CustomTabsURLSpan(url), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }

            return text
        }

        return source
    }

    override fun onFocusChanged(
        view: View?,
        sourceText: CharSequence?,
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
    }
}