package com.ly.core_request.local_logic

import com.ly.core_db.bean.User
import com.ly.core_db.helpers.UserDbHelper
import com.ly.core_model.UserModel
import com.ly.core_request.RequestError
import com.ly.core_request.Response
import com.ly.core_request.db_ext.toModel
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.delay

@Suppress("unused")
internal object LocalLoginLogic {

    private const val TAG = "LocalLoginLogic"

    suspend fun login(account: String, pwd: String): Response<UserModel> {
        val existedUser = UserDbHelper.queryByAccount(account) ?: throw RequestError(
            -1,
            "请先注册"
        )
        if (existedUser.pwd != pwd) throw RequestError(-1, "密码错误")
        val updatedUser = existedUser.copy(lastUpdateTime = System.currentTimeMillis())
        UserDbHelper.update(updatedUser)
        val userInfo = catchOrNull {
            UserDbHelper.queryInfo(existedUser.id)
        } ?: throw RequestError(-1, "请求失败")
        return Response(userInfo.toModel(), Response.Success, "")
    }

    suspend fun register(name: String, account: String, pwd: String): Response<Boolean> {
        val existedUser = UserDbHelper.queryByAccount(account)
        if (existedUser != null) throw RequestError(-1, "用户已存在")
        val userBean = User(
            account = account,
            name = name,
            pwd = pwd,
            createTime = System.currentTimeMillis()
        )
        val addResult = UserDbHelper.add(userBean)
        if (!addResult) throw RequestError(-1, "注册失败")
        return Response(data = true, Response.Success, "")
    }


    suspend fun logout(): Response<Boolean> {
        delay(50)
        return Response(true, 200, "")
    }

}