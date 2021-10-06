package com.fintecsystems.xs2awizard.components

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Wrapper to easily store data into an fragment's/activity's view model.
 */
class GenericViewModel <A> : ViewModel() {
    val liveData: MutableLiveData<A> = MutableLiveData()
}