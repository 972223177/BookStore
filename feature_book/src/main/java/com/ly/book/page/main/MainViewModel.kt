package com.ly.book.page.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ly.common.logic.LocalLoginLogic
import com.ly.core_model.MainMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _bottomIndex = MutableStateFlow(-1)
    val bottomIndex: StateFlow<Int> = _bottomIndex

    @Suppress("MemberVisibilityCanBePrivate")
    fun changeBottomTab(index: Int) {
        _bottomIndex.value = index
    }

    fun logout() {
        viewModelScope.launch {
            LocalLoginLogic.handleLogout()
            changeBottomTab(MainMenuItem.Home.ordinal)
        }
    }
}