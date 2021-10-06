package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.ParagraphLineData

/**
 * Subclass of [FormLine].
 * Able to display a paragraph with title and/or text.
 * Different background colors, depending on the severity.
 */
class ParagraphLine : FormLine() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        getThemedInflater(inflater).inflate(R.layout.fragment_line_paragraph, container, false)
            .also { inflatedView ->
                val formData = getFormData() as ParagraphLineData

                // Retrieve Paragraph values of the current theme
                val themedValues =
                    inflatedView.context.theme.obtainStyledAttributes(R.styleable.Paragraph)

                val backgroundColor = themedValues.getColor(
                    when (formData.severity) {
                        "info" -> R.styleable.Paragraph_paragraph_info_background_color
                        "error" -> R.styleable.Paragraph_paragraph_error_background_color
                        "warning" -> R.styleable.Paragraph_paragraph_warning_background_color
                        else -> R.styleable.Paragraph_paragraph_background_color
                    }, 0
                )
                val textColor = themedValues.getColor(
                    when (formData.severity) {
                        "info" -> R.styleable.Paragraph_paragraph_info_text_color
                        "error" -> R.styleable.Paragraph_paragraph_error_text_color
                        "warning" -> R.styleable.Paragraph_paragraph_warning_text_color
                        else -> R.styleable.Paragraph_paragraph_text_color
                    }, 0
                )

                inflatedView.setBackgroundColor(backgroundColor)

                inflatedView.findViewById<TextView>(R.id.paragraph_title).let {
                    // Check if title exists, otherwise set height to 0, so it won't look
                    // weird in the layout.
                    if (formData.title?.isNotEmpty() == true) {
                        it.text = formData.title
                    } else {
                        it.height = 0
                    }

                    it.setTextColor(textColor)
                }

                inflatedView.findViewById<TextView>(R.id.paragraph_text).let {
                    // Check if text exists, otherwise set height to 0, so it won't look
                    // weird in the layout.
                    if (formData.text.isNotEmpty()) {
                        actionDelegate.parseMarkupText(it, formData.text)
                    } else {
                        it.height = 0
                    }

                    it.setTextColor(textColor)
                }
            }
}