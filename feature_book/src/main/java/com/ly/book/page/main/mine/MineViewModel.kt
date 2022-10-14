package com.ly.book.page.main.mine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ly.common.utils.UserHelper
import com.ly.core_model.BookModel
import com.ly.core_request.db_ext.isUpdated
import com.ly.core_request.use_case.BookUseCase
import com.ly.core_request.use_case.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor(
    private val bookUseCase: BookUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {

    var state by mutableStateOf(MineState())
        private set

    private val _mineEvent = Channel<MineEvent>(Channel.BUFFERED)
    val mineEvent = _mineEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            UserHelper.user.collectLatest {
                if (it == null) {
                    state = MineState()
                    return@collectLatest
                }
                val userBooks = bookUseCase.getUserAllBooks(it.id).data ?: emptyList()
                val totalCount = userBooks.size
                var updateCount = 0
                var audioTypeCount = 0
                var lastedReadBook: BookModel? = null
                for (book in userBooks) {
                    if (lastedReadBook == null) {
                        lastedReadBook = book
                    }
                    if (lastedReadBook.updateTime <= book.updateTime) {
                        lastedReadBook = book
                    }

                    if (book.isAudioType) {
                        audioTypeCount += 1
                    }
                    if (book.isUpdated()) {
                        updateCount += 1
                    }

                }
                state = state.copy(
                    avatar = it.avatar,
                    nickname = it.name,
                    followCount = it.followCount,
                    followingCount = it.followingCount,
                    updatedBookCount = updateCount,
                    bookCount = totalCount,
                    audioBookCount = audioTypeCount,
                    bookModel = lastedReadBook
                )
            }
        }
    }

    fun dispatch(action: MineAction) {
        when (action) {
            MineAction.Logout -> logout()
        }
    }


    private fun logout() {
        viewModelScope.launch {
            userUseCase.logout()
            UserHelper.handleLogout()
            _mineEvent.send(MineEvent.ToHome)
        }
    }

}