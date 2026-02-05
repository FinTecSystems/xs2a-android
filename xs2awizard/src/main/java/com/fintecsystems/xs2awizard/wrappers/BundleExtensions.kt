package com.fintecsystems.xs2awizard.wrappers

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

/**
 * Platform-safe way to retrieve a Serializable from a Bundle.
 *
 * This extension method handles the API level difference for retrieving Serializable objects:
 * - For API 33 (Tiramisu) and above, uses the new type-safe [Bundle.getSerializable] method
 * - For API levels below 33, uses the deprecated [Bundle.getSerializable] method with safe casting
 *
 * @param T The type of the Serializable object to retrieve
 * @param key The key to retrieve the value for
 * @return The Serializable object associated with the key, or null if not found or wrong type
 */
internal inline fun <reified T : java.io.Serializable> Bundle.getPlatformSafeSerializable(
    key: String
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getSerializable(
        key,
        T::class.java
    )
} else {
    @Suppress("DEPRECATION")
    getSerializable(
        key
    ) as? T
}

/**
 * Platform-safe way to retrieve a Parcelable from a Bundle.
 *
 * This extension method handles the API level difference for retrieving Parcelable objects:
 * - For API 33 (Tiramisu) and above, uses the new type-safe [Bundle.getParcelable] method
 * - For API levels below 33, uses the deprecated [Bundle.getParcelable] method
 *
 * @param T The type of the Parcelable object to retrieve
 * @param key The key to retrieve the value for
 * @return The Parcelable object associated with the key, or null if not found or wrong type
 */
internal inline fun <reified T : Parcelable> Bundle.getPlatformSafeParcelable(
    key: String
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    getParcelable(
        key,
        T::class.java
    )
} else {
    @Suppress("DEPRECATION")
    getParcelable<T>(
        key
    )
}
