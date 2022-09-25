package com.ly.book.page.route

object NavRoute {
    private const val Root = "book/"
    const val PageMain = "$Root/Main"
    const val PageWaiting = "$Root/Waiting"
    const val PageLoginMain = "$Root/Login"

    object Login {
        const val Main = "$PageLoginMain/Main"
        const val SignIn = "$PageLoginMain/SignIn"
        const val SignUp = "$PageLoginMain/SignUp"
    }
}