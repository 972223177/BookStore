package com.ly.book

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.gyf.immersionbar.ktx.immersionBar
import com.ly.book.page.WaitingPage
import com.ly.book.page.login.LoginMainPage
import com.ly.book.page.login.LoginViewModel
import com.ly.book.page.login.SignInPage
import com.ly.book.page.login.SignUpPage
import com.ly.book.page.main.MainPage
import com.ly.book.page.route.NavRoute
import com.ly.book.utils.LocalNavController
import com.ly.common.logic.ConfigHelper
import com.ly.common.route.Router
import com.ly.common.route.installRouter
import com.therouter.router.Route
import dagger.hilt.android.AndroidEntryPoint

@Route(path = Router.BookHome)
@AndroidEntryPoint
class BookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        immersionBar {
            fullScreen(true)
        }
        super.onCreate(savedInstanceState)
        installRouter()
        setContent {
            val navController = rememberNavController()
            val systemUiController = rememberSystemUiController()
//            isSystemInDarkTheme()
            DisposableEffect(systemUiController) {
                systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = true)

                onDispose { }
            }
            CompositionLocalProvider(LocalNavController provides navController) {
                NavHost(
                    navController,
                    startDestination = if (ConfigHelper.isLogin) NavRoute.PageMain else NavRoute.PageLoginMain
                ) {
                    composable(NavRoute.PageWaiting) {
                        WaitingPage()
                    }

                    composable(NavRoute.PageMain) {
                        MainPage()
                    }

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
                            SignInPage(viewModel)
                        }
                        composable(NavRoute.Login.SignUp) {
                            val parentEntry = remember(it) {
                                navController.getBackStackEntry(NavRoute.PageLoginMain)
                            }
                            val viewModel = hiltViewModel<LoginViewModel>(parentEntry)
                            SignUpPage(viewModel)
                        }
                    }
                }
            }
        }
    }
}