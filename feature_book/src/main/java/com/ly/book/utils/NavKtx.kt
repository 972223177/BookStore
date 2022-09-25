package com.ly.book.utils

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

@Suppress("unused")
@Composable
fun findActivity(): Activity {
    val context = LocalContext.current
    if (context is Activity) return context
    return (if (context is ContextWrapper) context.baseContext as? Activity else null)
        ?: throw IllegalStateException("Please make sure root attached activity")
}

object LocalNavController {
    private val LocalNavController = compositionLocalOf<NavHostController?> { null }
    val current: NavHostController
        @Composable
        get() = LocalNavController.current
            ?: throw IllegalStateException("not found navController at CompositionLocal")

    infix fun provides(navHostController: NavHostController): ProvidedValue<NavHostController?> =
        LocalNavController.provides(navHostController)
}