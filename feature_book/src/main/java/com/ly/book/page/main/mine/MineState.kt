package com.ly.book.page.main.mine

import com.ly.core_model.BookModel

data class MineState(
    val avatar: String = "",
    val nickname: String = "",
    val followCount: Int = 0,
    val followingCount: Int = 0,
    val bookModel: BookModel? = null,
    val updatedBookCount: Int = 0,
    val bookCount: Int = 0,
    val audioBookCount: Int = 0
)

sealed class MineAction {
    object Logout : MineAction()
}

sealed class MineEvent {
    object ToHome : MineEvent()
}