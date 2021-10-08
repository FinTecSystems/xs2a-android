package com.fintecsystems.xs2awizard.form.components

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.XS2AWizardActionDelegate
import com.fintecsystems.xs2awizard.helper.Utils
import com.fintecsystems.xs2awizard.components.GenericViewModel
import com.fintecsystems.xs2awizard.form.FormLineData

/**
 * Subclass of [Fragment].
 * Baseclass for all [FormLine] elements.
 */
open class FormLine : Fragment() {
    protected val styleIdModel: GenericViewModel<Int> by activityViewModels()

    // Same principle as the Wizard. We cannot access view models on instantiation.
    // That's why we need to save the data into a temporary variable.
    private var mData: FormLineData? = null
    private val data: GenericViewModel<FormLineData> by viewModels()

    lateinit var actionDelegate: XS2AWizardActionDelegate
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve action delegate of parent.
        actionDelegate = when (parentFragment) {
            is MultiLine -> (parentFragment as MultiLine).actionDelegate
            else -> parentFragment as XS2AWizard
        }

        if (mData != null) {
            data.liveData.value = mData
            mData = null
        }
    }

    fun getFormData() = data.liveData.value

    fun setFormData(_data: FormLineData) {
        mData = _data
    }

    /**
     * Apply theme of this [FormLine] to the provided [LayoutInflater]
     *
     * @param inflater to be themed inflater.
     *
     * @return themed inflater.
     */
    fun getThemedInflater(
        inflater: LayoutInflater,
        styleBaseId: Int = R.style.XS2ATheme_Base
    ): LayoutInflater =
        Utils.getThemedInflater(
            inflater,
            requireContext(),
            styleIdModel.liveData.value,
            styleBaseId
        )
}
