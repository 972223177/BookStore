package com.ly.book.page.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ly.book.page.route.NavRoute
import com.ly.book.utils.LocalNavController
import com.ly.common.logic.LocalLoginLogic

@Composable
fun MainPage() {
    Scaffold {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()) {
            val navController = LocalNavController.current
            Button(onClick = {
                LocalLoginLogic.handleLogout()
                navController.navigate(NavRoute.PageLoginMain) {
                    popUpTo(NavRoute.PageLoginMain) { inclusive = true }
                }
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "登出")
            }
        }
    }
}