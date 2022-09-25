package com.ly.book.page.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ly.book.utils.toast
import com.ly.common.logic.LocalLoginLogic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private var account = ""
    private var password = ""
    private var name = ""
    var protocolAccept by mutableStateOf(false)
    var rememberMe by mutableStateOf(false)

    var rememberAccount by mutableStateOf("")
    var rememberPwd by mutableStateOf("")

    fun initRememberInfo(){
        viewModelScope.launch {
            val info = LocalLoginLogic.getRememberInfo()
            if (info.first.isNotEmpty() && info.second.isNotEmpty()) {
                rememberAccount = info.first
                rememberPwd = info.second
                account = info.first
                password = info.second
                rememberMe = true
            }
        }
    }

    fun updateAccount(value: String) {
        account = value
    }

    fun updatePwd(value: String) {
        password = value
    }

    fun updateName(value: String) {
        name = value
    }

    fun login(success: () -> Unit) {
        if (!checkSignInfo()) return
        viewModelScope.launch {
            if (loginSuspend()) {
                success()
            }
        }
    }

    private suspend fun loginSuspend(): Boolean {
        val result =
            LocalLoginLogic.login(account = account, pwd = password, rememberMe = rememberMe)
        if (result.first) return true
        toast(result.second)
        return false
    }

    fun register(success: () -> Unit) {
        if (!checkSignInfo(containName = true)) return
        viewModelScope.launch {
            val result = LocalLoginLogic.register(name = name, account = account, pwd = password)
            if (result.first) {
                val loginResult = loginSuspend()
                if (loginResult) {
                    success()
                }
            } else {
                toast(result.second)
            }
        }
    }

    private fun checkSignInfo(containName: Boolean = false): Boolean {
        if (!protocolAccept) {
            toast("请先同意协议")
            return false
        }
        if (name.isEmpty() && containName) {
            toast("请先输入昵称")
            return false
        }
        if (account.isEmpty()) {
            toast("请先输入邮箱")
            return false
        }
        if (password.isEmpty()) {
            toast("请先输入密码")
            return false
        }
        return true
    }

    fun deleteSignInInfo() {
        account = ""
        password = ""
    }
}