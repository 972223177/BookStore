package com.ly.book.utils

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ly.book.page.route.NavRoute
import com.ly.common.logic.LocalLoginLogic
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

fun Modifier.unRippleClick(onClick: () -> Unit) = composed {
    clickable(onClick = onClick, indication = null, interactionSource = remember {
        MutableInteractionSource()
    })
}

val zeroBtnElevation: ButtonElevation
    @Composable
    get() = ButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp)

inline fun checkLogin(navController: NavHostController, logged: () -> Unit) {
    if (LocalLoginLogic.isLogin) {
        logged()
    } else {
        navController.navigate(NavRoute.PageLoginMain)
    }
}

/**
 * 预览专用
 */
@Composable
fun PreviewProvideNavLocal(block: @Composable () -> Unit) {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        block()
    }
}