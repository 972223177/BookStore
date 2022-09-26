@file:OptIn(ExperimentalPagerApi::class)

package com.ly.book.page.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _bottomIndex = MutableStateFlow(-1)
    val bottomIndex: StateFlow<Int> = _bottomIndex

    fun changeBottomTab(index: Int) {
        _bottomIndex.value = index
    }

    companion object {
        const val BottomIndexKey = "mainBottomIndex"
    }
}