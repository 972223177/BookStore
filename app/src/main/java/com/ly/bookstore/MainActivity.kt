package com.ly.bookstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ly.common.logic.ConfigHelper
import com.ly.common.route.goHome
import com.ly.utils.base.launchAppScope
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            true
        }
        launchAppScope {
            delay(150L)
            ConfigHelper.init()
            goHome()
            finish()
        }
    }
}
