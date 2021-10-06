package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.ImageLineData
import com.fintecsystems.xs2awizard.helper.Utils

/**
 * Subclass of [FormLine].
 * Displays an image
 */
class ImageLine : FormLine() {
    private fun pixelToDp(pixel: Int) = (pixel * requireContext().resources.displayMetrics.density).toInt()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_image, container, false)
            .also { inflatedView ->
                val formData = getFormData() as ImageLineData

                (inflatedView as LinearLayout).gravity = when(formData.align) {
                    "right" -> Gravity.END
                    "center" -> Gravity.CENTER_HORIZONTAL
                    else -> Gravity.START
                }

                inflatedView.findViewById<ImageView>(R.id.image_view).let {
                    Utils.decodeBase64Image(formData.data).apply {
                        it.setImageBitmap(this)
                        it.layoutParams = LinearLayout.LayoutParams(pixelToDp(width), pixelToDp(height))
                    }
                }
            }
}