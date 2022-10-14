package com.ly.core_request.use_case

import com.ly.core_model.UserModel
import com.ly.core_request.Response
import com.ly.core_request.handleRequestError
import com.ly.core_request.local_logic.LocalLoginLogic
import javax.inject.Inject

class UserUseCase @Inject constructor() {

    suspend fun login(
        account: String,
        pwd: String,
    ): Response<UserModel> = handleRequestError {
        LocalLoginLogic.login(account, pwd)
    }

    suspend fun register(name: String, account: String, pwd: String): Response<Boolean> =
        handleRequestError {
            LocalLoginLogic.register(name, account, pwd)
        }

    suspend fun logout(): Response<Boolean> = handleRequestError { LocalLoginLogic.logout() }
}