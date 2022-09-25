package com.ly.book.utils

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import com.ly.utils.base.appContext
import com.ly.utils.base.launchAppScope

fun toast(msg: String) {
    if (msg.isEmpty()) return
    launchAppScope {
        Toast.makeText(appContext, msg, Toast.LENGTH_SHORT).show()
    }
}

fun Modifier.rippleClick(onClick: () -> Unit) = composed {
    clickable(interactionSource = remember {
        MutableInteractionSource()
    }, indication = rememberRipple(), onClick = onClick)
}

val zeroBtnElevation: ButtonElevation
    @Composable
    get() = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp)