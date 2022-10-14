package com.ly.bookstore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ly.common.route.goHome
import com.ly.common.utils.ConfigHelper
import com.ly.utils.base.launchAppScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : ComponentActivity() {
    private var mKeepOn = AtomicBoolean(true)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            return@setKeepOnScreenCondition mKeepOn.get()
        }
        launchAppScope(Dispatchers.Default) {
            val mainActivity = WeakReference(this@MainActivity)
            val interval = 1000L
            val start = System.currentTimeMillis()
            ConfigHelper.init()
            withContext(Dispatchers.Main) {
                goHome()
                mainActivity.get()?.finish()
                val end = System.currentTimeMillis()
                val delayValue = end - start
                delay(if (delayValue >= interval) delayValue else interval - delayValue)
                mKeepOn.compareAndSet(true, false)
            }
        }
    }
}
