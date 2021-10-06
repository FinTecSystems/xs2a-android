package com.fintecsystems.xs2awizard.form.components.selectLine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import com.fintecsystems.xs2awizard.R

/**
 * [BaseAdapter] implementation, that's able to use an Map for it's data.
 */
class SelectLineSpinnerAdapter(
    context: Context,
    private val items: Map<String, String>,
): BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(
        ContextThemeWrapper(context, R.style.XS2ATheme)
    )

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[items.keys.elementAt(position)]

    override fun getItemId(position: Int) = 0L

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View = inflater.inflate(R.layout.fragment_line_select_item, null).also {
        it.findViewById<TextView>(R.id.select_input_item).text = getItem(position)
    }

}