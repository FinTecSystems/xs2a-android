package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.FlickerLineData
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.json.JsonPrimitive
import kotlin.math.max
import kotlin.math.min

/**
 * Subclass of [ValueFormLine].
 * Displays an Flicker-Tan element.
 */
class FlickerLine : ValueFormLine(), TextWatcher {
    private lateinit var task: Runnable

    // Speed of the animation
    private var fps = 10
    // Speed increase/decrease per button press.
    private val fpsStepSize = 2

    // Size increase/decrease per button press.
    private val sizeStepSize = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?) = super.onCreateView(inflater, container, savedInstanceState).also { parentView ->
        getThemedInflater(inflater).inflate(R.layout.fragment_line_flicker, container, false).also { inflatedView ->
            val formData = getFormData() as FlickerLineData
            
            inflatedView.findViewById<TextInputEditText>(R.id.flicker_text_input_edit).also {
                it.hint = formData.placeholder

                it.addTextChangedListener(this)
            }

            val flickerBarsContainer = inflatedView.findViewById<LinearLayout>(R.id.flicker_bars)

            inflatedView.findViewById<ImageButton>(R.id.flicker_slower).setOnClickListener { fps = max(fps - fpsStepSize, 1) }
            inflatedView.findViewById<ImageButton>(R.id.flicker_faster).setOnClickListener { fps = min(fps + fpsStepSize, 60) }
            inflatedView.findViewById<ImageButton>(R.id.flicker_smaller).setOnClickListener { flickerBarsContainer.layoutParams = flickerBarsContainer.layoutParams.apply { width = max(0, flickerBarsContainer.width - sizeStepSize) } }
            inflatedView.findViewById<ImageButton>(R.id.flicker_bigger).setOnClickListener { flickerBarsContainer.layoutParams = flickerBarsContainer.layoutParams.apply { width = flickerBarsContainer.width + sizeStepSize } }

            val flickerBars = flickerBarsContainer.children.toList()

            val context = requireContext()

            // Background and bar color
            val colors = listOf(
                ContextCompat.getColor(context, R.color.flicker_background), // Background color
                ContextCompat.getColor(context, R.color.flicker_foreground) // Bar color
            )

            // The task itself.
            // This runs the animation.
            task = object : Runnable {
                private var currentStep = 0

                override fun run() {
                    val stepValues = formData.code[currentStep]

                    // Apply stepValues to bars.
                    flickerBars.forEachIndexed { index, view ->
                        view.setBackgroundColor(colors[stepValues[index]])
                    }

                    currentStep = (currentStep + 1) % formData.code.size

                    // Call this runnable again by the specified delay.
                    inflatedView.postDelayed(task, 1000L / fps)
                }
            }

            // Start the runnable.
            inflatedView.post(task)

            parentView.findViewById<LinearLayout>(R.id.form_value_container).addView(inflatedView)
        }
    }

    override fun beforeTextChanged(
        s: CharSequence?,
        start: Int,
        count: Int,
        after: Int
    ) {}

    override fun afterTextChanged(s: Editable?) {}

    override fun onTextChanged(
        s: CharSequence?,
        start: Int,
        before: Int,
        count: Int
    ) {
        (getFormData() as FlickerLineData).value = JsonPrimitive(s.toString())
    }
}