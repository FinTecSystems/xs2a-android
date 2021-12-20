package com.fintecsystems.xs2awizard.components

import com.fintecsystems.xs2awizard.components.theme.IXS2ATheme

data class XS2AWizardConfig(
    val sessionKey: String,
    val backendURL: String? = null,
    val onFinish: (String?) -> Unit,
    val onAbort: () -> Unit,
    val onError: (XS2AWizardError) -> Unit = {},
    val onNetworkError: () -> Unit = {},
    val theme: IXS2ATheme? = null,
)