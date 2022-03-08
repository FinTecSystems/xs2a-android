package com.fintecsystems.xs2awizard.components.networking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fintecsystems.xs2awizard.R
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.form.components.shared.FormText

@Composable
fun ConnectivityStatusBanner(connectionState: ConnectionState) {
    val isDisconnected = connectionState === ConnectionState.DISCONNECTED

    AnimatedVisibility(
        visible = isDisconnected,
        enter = slideInVertically(),
        exit = slideOutVertically(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(XS2ATheme.CURRENT.errorParagraphStyle.backgroundColor)
                .padding(0.dp, 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.width(48.dp),
                painter = painterResource(
                    R.drawable.ic_warning
                ),
                contentDescription = stringResource(R.string.no_internet_connection),
                colorFilter = ColorFilter.tint(XS2ATheme.CURRENT.errorParagraphStyle.textColor)
            )

            FormText(
                text = stringResource(R.string.no_internet_connection),
                color = XS2ATheme.CURRENT.errorParagraphStyle.textColor,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}