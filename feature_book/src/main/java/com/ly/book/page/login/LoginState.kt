package com.ly.book.page.login

data class LoginState(
    val name: String = "",
    val account: String = "",
    val pwd: String = "",
    val rememberMe: Boolean = false,
    val protocolAccept: Boolean = false
)

sealed class LoginAction {
    class UpdateName(val value: String) : LoginAction()
    class UpdateAccount(val value: String) : LoginAction()
    class UpdatePwd(val value: String) : LoginAction()
    class UpdateRememberMe(val value: Boolean) : LoginAction()
    class UpdateProtocolAccept(val value: Boolean) : LoginAction()
    object Login : LoginAction()
    object Register : LoginAction()
    object DeleteInfo : LoginAction()
}

sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
    object RegisterSuccess : LoginEvent()
    data class ErrorMsg(val value: String) : LoginEvent()
}