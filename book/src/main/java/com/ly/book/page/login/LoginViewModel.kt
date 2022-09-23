package com.ly.book.page.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ly.book.utils.toast
import com.ly.common.logic.ConfigHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private var account = ""
    private var password = ""
    private var name = ""
    var protocolAccept = false
    var rememberMe = ConfigHelper.user.rememberMe


    fun updateAccount(value: String) {
        account = value
    }

    fun updatePwd(value: String) {
        password = value
    }

    fun updateName(value: String) {
        name = value
    }

    fun login(success: () -> Unit){
        if (!checkSignInfo()) return
        viewModelScope.launch {
            ConfigHelper.updateUserSuspend {
                it.toBuilder()
                    .setEmail(account)
                    .setPassword(account)
                    .setRememberMe(rememberMe)
                    .build()
            }
            success()
        }
    }

    fun register(success: () -> Unit) {
        if (!checkSignInfo(containName = true)) return
        viewModelScope.launch {
            ConfigHelper.updateUserSuspend {
                it.toBuilder()
                    .setEmail(account)
                    .setPassword(password)
                    .setName(name)
                    .build()
            }
            success()
        }
    }

    private fun checkSignInfo(containName:Boolean = false):Boolean{
        if (!protocolAccept) {
            toast("请先同意协议")
            return false
        }
        if (name.isEmpty() && containName){
            toast("请先输入昵称")
            return false
        }
        if (account.isEmpty()){
            toast("请先输入邮箱")
            return false
        }
        if (password.isEmpty()){
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