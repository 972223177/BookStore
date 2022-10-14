package com.ly.book

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.gyf.immersionbar.ktx.immersionBar
import com.ly.book.page.main.MainPage
import com.ly.book.page.other.PicPreviewPage
import com.ly.book.page.other.WaitingPage
import com.ly.book.route.NavRoute
import com.ly.book.route.args.PicPreData
import com.ly.book.utils.LocalNavController
import com.ly.book.utils.router.loginGraph
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
                    startDestination = NavRoute.PageMain
                ) {
                    composable(NavRoute.PageWaiting) {
                        WaitingPage()
                    }

                    composable(NavRoute.PageMain) {
                        MainPage(goPicPre = {
                            navController.navigate(NavRoute.picPre(it))
                        })
                    }

                    composable(
                        NavRoute.PagePicPreviewWithKey,
                        arguments = listOf(navArgument(NavRoute.PagePicPreview_Key) {
                            type = NavType.StringType
                        })
                    ) {
                        val data = remember(key1 = it) {
                            val json = it.arguments?.getString(NavRoute.PagePicPreview_Key, "")
                            NavRoute.jsonDecode<PicPreData>(json ?: "")
                        }
//                        Log.d("Book","data:$data")
                        PicPreviewPage(data, popup = { navController.popBackStack() })
                    }

                    loginGraph(navController)
                }
            }
        }
    }
}