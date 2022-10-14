package com.ly.book.page.main

import androidx.lifecycle.ViewModel
import com.ly.core_model.MainMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _mainEvent = Channel<MainEvent>(Channel.BUFFERED)
    val mainEvent = _mainEvent.receiveAsFlow()

    fun dispatch(action: MainAction) {
        when (action) {
            is MainAction.SwitchBottomBar -> changeBottomTab(action.target)
        }
    }

    private fun changeBottomTab(item: MainMenuItem) {
        _mainEvent.trySend(MainEvent.BottomBarChanged(item.ordinal))
    }

}