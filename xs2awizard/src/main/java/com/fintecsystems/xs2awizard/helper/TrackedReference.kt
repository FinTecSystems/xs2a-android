package com.fintecsystems.xs2awizard.helper

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.lang.ref.WeakReference

// https://stackoverflow.com/a/64944753
@Suppress("UNCHECKED_CAST")
@Parcelize
@kotlinx.serialization.Serializable
data class TrackedReference<ReferenceType: Any> constructor(
    private var uniqueID: Int = -1
) : Serializable, Parcelable {

    constructor(reference: ReferenceType) : this() {
        uniqueID = System.identityHashCode(this)
        referenceMap.set(uniqueID, reference)
    }

    val get: ReferenceType?
        get() = referenceMap.get(uniqueID) as? ReferenceType

    fun removeStrongReference() {
        get?.let { referenceMap.set(uniqueID, WeakReference(it)) }
    }

    companion object {
        var referenceMap = hashMapOf<Int, Any>()
            private set
    }

}