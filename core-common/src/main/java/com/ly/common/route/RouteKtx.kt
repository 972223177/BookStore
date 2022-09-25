package com.ly.common.route

import android.app.Activity
import com.therouter.TheRouter

fun Activity.installRouter() {
    TheRouter.inject(this)
}

fun goHome(){
    TheRouter.build(Router.BookHome).navigation()
}