package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.MultiLineData
import com.fintecsystems.xs2awizard.helper.Utils
import com.google.android.material.tabs.TabLayout

/**
 * Subclass of [FormLine].
 * Displays multiple forms
 */
class MultiLine : FormLine(), TabLayout.OnTabSelectedListener {
    private val forms = mutableMapOf<String, MutableList<FormLine>>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = getThemedInflater(inflater).inflate(R.layout.fragment_line_multi, container, false)
        .also {
            val formData = getFormData() as MultiLineData

            val tabLayout = it.findViewById<TabLayout>(R.id.multi_tabs)
            val linearLayout = it.findViewById<LinearLayout>(R.id.multi_container)

            childFragmentManager.beginTransaction().let { childTransaction ->
                // Construct each form and append to multi_container
                formData.forms.forEach { multiForm ->
                    tabLayout.addTab(tabLayout.newTab().setText(multiForm.label))

                    // List to keep track of all form lines.
                    val constructedFormLines = mutableListOf<FormLine>()
                    val containerId = Utils.generateViewId()

                    // Construct a new container for the form elements by scratch.
                    LinearLayout(requireContext()).apply {
                        id = containerId
                        visibility =
                            if (multiForm.value == formData.selected) View.VISIBLE else View.GONE

                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )

                        orientation = LinearLayout.VERTICAL

                        // Add the container to the multi-form container
                        linearLayout.addView(this)
                    }

                    // Construct the form just like we would do in the wizard, but append them
                    // to the newly created container.
                    multiForm.form.forEach innerForEach@{ multiFormElementData ->
                        val createdFragment =
                            actionDelegate.constructFragment(multiFormElementData) ?: return@innerForEach

                        constructedFormLines.add(createdFragment)
                        childTransaction.add(containerId, createdFragment)
                    }

                    forms[multiForm.value] = constructedFormLines
                }

                childTransaction.commit()
            }

            tabLayout.addOnTabSelectedListener(this)
        }

    /**
     * @return the currently selected sub-form.
     */
    fun getCurrentForm() = forms[(getFormData() as MultiLineData).selected]

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val formData = (getFormData() as MultiLineData)

        val selectedTab = tab?.position ?: 0
        formData.selected = formData.forms[selectedTab].value

        Utils.hideSoftKeyboard(activity)

        view?.findViewById<LinearLayout>(R.id.multi_container).apply {
            this?.children?.forEachIndexed { index, view ->
                view.visibility = if (selectedTab == index) View.VISIBLE else View.GONE
            }
        }
    }


    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}