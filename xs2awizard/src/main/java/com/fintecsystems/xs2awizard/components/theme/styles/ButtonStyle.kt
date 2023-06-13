package com.fintecsystems.xs2awizard.components.theme.styles

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AColor
import kotlinx.parcelize.Parcelize

/**
 * Style-Attributes of a button.
 */
@Immutable
@Parcelize
data class ButtonStyle(
    val backgroundColor: XS2AColor,
    val textColor: XS2AColor,
    val borderStyle: BorderStyle = BorderStyle.Unspecified
) : Parcelable
