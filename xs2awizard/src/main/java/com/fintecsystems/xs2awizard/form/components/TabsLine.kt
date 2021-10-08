package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.form.TabsLineData
import com.fintecsystems.xs2awizard.helper.Utils
import com.google.android.material.tabs.TabLayout
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Subclass of [FormLine].
 * Displays simple tabs
 */
class TabsLine : FormLine(), TabLayout.OnTabSelectedListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = getThemedInflater(inflater, R.style.XS2ATheme_Container).inflate(R.layout.fragment_line_tabs, container, false)
        .also {
            it as TabLayout

            val formData = getFormData() as TabsLineData

            formData.tabs.forEach{entry ->
                val tab = it.newTab().setText(entry.value)
                it.addTab(tab, false)

                if (entry.key == formData.selected) {
                    it.selectTab(tab)
                }
            }

            it.addOnTabSelectedListener(this)
        }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        val formData = (getFormData() as TabsLineData)

        val selectedTab = tab?.position ?: 0

        Utils.hideSoftKeyboard(activity)

        actionDelegate.submitForm(
            buildJsonObject {
                put("action", formData.action)
                put("params", formData.tabs.keys.elementAt(selectedTab))
            },
            true
        )
    }


    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}