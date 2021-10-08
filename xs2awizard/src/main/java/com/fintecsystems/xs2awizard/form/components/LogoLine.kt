package com.fintecsystems.xs2awizard.form.components

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.android.volley.VolleyError
import com.android.volley.toolbox.ImageLoader
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.LinkTransformationMethod
import com.fintecsystems.xs2awizard.form.LogoLineData
import com.fintecsystems.xs2awizard.helper.Utils
import com.fintecsystems.xs2awizard_networking.NetworkingInstance


/**
 * Subclass of [FormLine].
 * Displays the FTS Logo
 */
class LogoLine : FormLine(), ImageLoader.ImageListener {
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_logo, container, false)
        .also { inflatedView ->
            val formData = getFormData() as LogoLineData

            imageView = inflatedView.findViewById(R.id.logo_image_view)

            actionDelegate.incrementLoadingIndicatorLock()

            NetworkingInstance.getInstance(requireContext()).imageLoader.get(
                requireContext().getString(
                    getImageUrlId(formData)
                ),
                this,
            )

            if (formData.tooltip != null) {
                inflatedView.setOnClickListener {
                    // Remove tabs, so it won't look weird.
                    val prunedTooltip = formData.tooltip.replace("\t", "")

                    AlertDialog.Builder(
                        Utils.getThemedContext(
                            requireContext(),
                            styleIdModel.liveData.value,
                            R.style.XS2ATheme_Container
                        )
                    )
                        .setMessage(
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                                Html.fromHtml(prunedTooltip, Html.FROM_HTML_MODE_LEGACY)
                            else
                                @Suppress("DEPRECATION")
                                Html.fromHtml(prunedTooltip)
                        )
                        .setCancelable(true)
                        .setNegativeButton(R.string.dialog_dismiss, null)
                        .create().apply {
                            show()

                            // Make urls in the alert clickable
                            findViewById<TextView>(android.R.id.message)?.also {
                                it.transformationMethod = LinkTransformationMethod()
                                it.movementMethod = LinkMovementMethod.getInstance()
                            }
                        }
                }

            }
        }

    private fun getImageUrlId(formData: LogoLineData): Int {
        val isDarkMode =
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }

        if (resources.displayMetrics.densityDpi >= DisplayMetrics.DENSITY_XHIGH) {
            return when (formData.logoVariation) {
                "white" -> R.string.fts_logo_white_2x_url
                "black" -> R.string.fts_logo_black_2x_url
                else -> (if (isDarkMode) R.string.fts_logo_white_2x_url else R.string.fts_logo_2x_url)
            }
        }

        return when (formData.logoVariation) {
            "white" -> R.string.fts_logo_white_url
            "black" -> R.string.fts_logo_black_url
            else -> (if (isDarkMode) R.string.fts_logo_white_url else R.string.fts_logo_url)
        }
    }

    override fun onErrorResponse(error: VolleyError?) {
        Log.e("XS2AWizard", "onErrorResponse: $error")

        actionDelegate.decrementLoadingIndicatorLock()
    }

    override fun onResponse(response: ImageLoader.ImageContainer?, isImmediate: Boolean) {
        if (isImmediate && response?.bitmap == null)
            return

        imageView.setImageBitmap(
            response!!.bitmap
        )

        actionDelegate.decrementLoadingIndicatorLock()
    }
}