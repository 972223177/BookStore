package com.ly.book.utils.router

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ly.book.page.login.LoginMainPage
import com.ly.book.page.login.LoginViewModel
import com.ly.book.page.login.SignInPage
import com.ly.book.page.login.SignUpPage
import com.ly.book.page.route.NavRoute


fun NavGraphBuilder.loginGraph(navController: NavHostController) {
    navigation(
        startDestination = NavRoute.Login.Main,
        route = NavRoute.PageLoginMain
    ) {
        composable(NavRoute.Login.Main) {
            LoginMainPage()
        }
        composable(NavRoute.Login.SignIn) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoute.PageLoginMain)
            }
            val viewModel = hiltViewModel<LoginViewModel>(parentEntry)
            SignInPage(viewModel, navToMain = {
                navController.navToMainPage(NavRoute.Login.SignIn)
            }, initFirst = {
                viewModel.initRememberInfo()
            })
        }
        composable(NavRoute.Login.SignUp) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoute.PageLoginMain)
            }
            val viewModel = hiltViewModel<LoginViewModel>(parentEntry)
            SignUpPage(viewModel) {
                navController.navToMainPage(NavRoute.Login.SignUp)
            }
        }
    }
}

private fun NavHostController.navToMainPage(popRoute:String) {
    navigate(NavRoute.PageMain) {
        popUpTo(popRoute) {
            inclusive = true
        }
    }
}