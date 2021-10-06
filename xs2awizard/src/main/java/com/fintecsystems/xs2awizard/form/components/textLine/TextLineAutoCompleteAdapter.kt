package com.fintecsystems.xs2awizard.form.components.textLine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.view.ContextThemeWrapper
import com.fintecsystems.xs2awizard.R

/**
 * [BaseAdapter] implementation used by [TextLine] for auto-completion.
 */
class TextLineAutoCompleteAdapter(
    context: Context,
    private var items: List<AutoCompleteEntry>,
): BaseAdapter(), Filterable {
    private val inflater: LayoutInflater = LayoutInflater.from(
        ContextThemeWrapper(context, R.style.XS2ATheme)
    )

    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = 0L

    fun setItems(_items: List<AutoCompleteEntry>) {
        items = _items
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View = inflater.inflate(R.layout.fragment_line_text_autocomplete_item, null).also {
        // Populate item with text.
        getItem(position).apply {
            it.findViewById<TextView>(R.id.text_autocomplete_item).apply {
                text = value
                // isSelected is set to true, so that the marquee animation will play.
                isSelected = true
            }
            it.findViewById<TextView>(R.id.text_autocomplete_label).apply {
                text = label
                // isSelected is set to true, so that the marquee animation will play.
                isSelected = true
            }
        }
    }

    // Implement empty and functionless filter as it's required by the text input.
    // Because we do the filtering on the backend, we can leave this functionless.
    override fun getFilter(): Filter = object: Filter() {
        override fun performFiltering(constraint: CharSequence?) = FilterResults()

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
    }
}