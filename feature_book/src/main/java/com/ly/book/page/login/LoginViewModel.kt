package com.ly.book.page.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ly.common.utils.UserHelper
import com.ly.core_model.UserModel
import com.ly.core_request.use_case.UserUseCase
import com.ly.utils.common.ifNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: UserUseCase
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val _event = Channel<LoginEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    init {
        viewModelScope.launch {
            val info = UserHelper.getRememberInfo()
            if (info.first.isNotEmpty() && info.second.isNotEmpty()) {
                state = state.copy(
                    account = info.first,
                    pwd = info.second,
                    rememberMe = true
                )
            }
        }
    }

    fun dispatch(action: LoginAction) {
        when (action) {
            LoginAction.DeleteInfo -> deleteInfo()
            LoginAction.Login -> login()
            LoginAction.Register -> register()
            is LoginAction.UpdateAccount -> updateAccount(action.value)
            is LoginAction.UpdateName -> updateName(action.value)
            is LoginAction.UpdatePwd -> updatePwd(action.value)
            is LoginAction.UpdateRememberMe -> updateRememberMe(action.value)
            is LoginAction.UpdateProtocolAccept -> updateProtocolAccept(action.value)
        }
    }

    private fun updateAccount(value: String) {
        state = state.copy(account = value)
    }

    private fun updatePwd(value: String) {
        state = state.copy(pwd = value)
    }

    private fun updateName(value: String) {
        state = state.copy(name = value)
    }

    private fun updateRememberMe(value: Boolean) {
        state = state.copy(rememberMe = value)
    }

    private fun updateProtocolAccept(value: Boolean) {
        state = state.copy(protocolAccept = value)
    }

    private fun login() {
        if (!checkSignInfo()) return
        viewModelScope.launch {
            if (loginSuspend()) {
                _event.send(LoginEvent.LoginSuccess)
            }
        }
    }

    private suspend fun loginSuspend(): Boolean {
        val result =
            loginUseCase.login(account = state.account, pwd = state.pwd)
        if (result.success) {
            with(state) {
                UserHelper.rememberMe(if (rememberMe) account else "", if (rememberMe) pwd else "")
            }
            UserHelper.cacheLoginInfo(result.data.ifNull { UserModel() })
            return true
        }
        _event.send(LoginEvent.ErrorMsg(result.msg))
        return false
    }

    private fun register() {
        if (!checkSignInfo(containName = true)) return
        viewModelScope.launch {
            val result =
                loginUseCase.register(name = state.name, account = state.account, pwd = state.pwd)
            if (result.success) {
                val loginResult = loginSuspend()
                if (loginResult) {
                    _event.send(LoginEvent.RegisterSuccess)
                }
            } else {
                _event.send(LoginEvent.ErrorMsg(result.msg))
            }
        }
    }

    private fun checkSignInfo(containName: Boolean = false): Boolean = with(state) {
        val msg = when {
            !protocolAccept -> "请先同意协议"
            name.isEmpty() && containName -> "请先输入昵称"
            account.isEmpty() -> "请先输入邮箱"
            pwd.isEmpty() -> "请先输入密码"
            else -> ""
        }
        _event.trySend(LoginEvent.ErrorMsg(msg))
        msg.isEmpty()
    }

    private fun deleteInfo() {
        state = LoginState()
    }
}