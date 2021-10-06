package com.fintecsystems.xs2awizard.components

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.fintecsystems.xs2awizard.R

/**
 * Simple loading indicator with helper methods to easily hide or show it.
 */
class LoadingIndicator(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
): LinearLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    init {
        inflate(context, R.layout.view_indicator_loading, this)
    }

    fun hide() {
        visibility = GONE
    }

    fun show() {
        visibility = VISIBLE
    }
}