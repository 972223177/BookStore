package com.ly.bookstore

import android.content.Context
import com.ly.utils.base.UtilsApplication
import com.therouter.TheRouter
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : UtilsApplication() {
    override fun attachBaseContext(base: Context?) {
        TheRouter.isDebug = BuildConfig.DEBUG
        super.attachBaseContext(base)
    }
}