package com.ly.book.utils.router

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ly.book.page.login.*
import com.ly.book.route.NavRoute


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
                navController.popLoginMain()
            }, initFirst = {
                viewModel.dispatch(LoginAction.DeleteInfo)
            })
        }
        composable(NavRoute.Login.SignUp) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry(NavRoute.PageLoginMain)
            }
            val viewModel = hiltViewModel<LoginViewModel>(parentEntry)
            SignUpPage(viewModel) {
                navController.popLoginMain()
            }
        }
    }
}

private fun NavHostController.popLoginMain() {
    popBackStack(NavRoute.Login.Main, true)
}