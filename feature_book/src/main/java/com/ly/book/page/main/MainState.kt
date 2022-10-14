package com.ly.book.page.main

import com.ly.core_model.MainMenuItem

sealed class MainAction {
    data class SwitchBottomBar(val target: MainMenuItem) : MainAction()
}

sealed class MainEvent {
    data class BottomBarChanged(val index: Int) : MainEvent()
}