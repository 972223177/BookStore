package com.ly.common.utils

import com.ly.common.logic.LocalLoginLogic
import com.ly.common.serializer.loginFlow
import com.ly.core_model.UserModel
import com.ly.utils.base.launchAppScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("unused")
object UserHelper {
    var isLogin: Boolean = false
        internal set

    private val mUser = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = mUser

    internal fun init() {
        launchAppScope {
            var dbJob: Job? = null
            loginFlow.collectLatest {
                isLogin = it.id > 0L
                dbJob?.cancel()
                if (it.id <= 0L) {
                    mUser.value = null
                    return@collectLatest
                }
                dbJob = launch {
                    LocalLoginLogic.listenUserFromDb(it.id).collectLatest { model ->
                        mUser.value = model
                    }
                }
            }
        }
    }
}