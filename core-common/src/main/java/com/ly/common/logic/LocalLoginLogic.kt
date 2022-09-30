package com.ly.common.logic

import android.util.Log
import com.ly.common.model_ext.toModel
import com.ly.common.serializer.loginInfo
import com.ly.common.serializer.rememberAccount
import com.ly.core_db.bean.UserBean
import com.ly.core_db.helpers.UserDbHelper
import com.ly.core_model.UserModel
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Suppress("unused")
object LocalLoginLogic {

    private const val TAG = "LocalLoginLogic"

    suspend fun login(
        account: String,
        pwd: String,
        rememberMe: Boolean = false
    ): Pair<Boolean, String> {
        val existedUser = UserDbHelper.queryByAccount(account) ?: return false to "请先注册"
        if (existedUser.pwd != pwd) return false to "密码错误"
        val updatedUser = existedUser.copy(lastUpdateTime = System.currentTimeMillis())
        UserDbHelper.update(updatedUser)
        updateDiskRememberInfo(if (rememberMe) existedUser.id else 0)
        loginInfo.updateData {
            it.toBuilder()
                .setId(existedUser.id)
                .build()
        }
        return true to ""
    }


    suspend fun register(name: String, account: String, pwd: String): Pair<Boolean, String> {
        val existedUser = UserDbHelper.queryByAccount(account)
        if (existedUser != null) {
            return false to "用户已存在"
        }
        val userBean = UserBean(account = account, name = name, pwd = pwd, createTime = System.currentTimeMillis())
        val addResult = UserDbHelper.add(userBean)
        if (!addResult) {
            return false to "注册失败"
        }
        updateDiskRememberInfo(0)
        return true to ""
    }

    suspend fun getRememberInfo(): Pair<String, String> {
        val rememberId = catchOrNull {
            rememberAccount.data.firstOrNull()
        }
        if (rememberId == null || rememberId.userId == 0) {
            return "" to ""
        }
        val user = UserDbHelper.query(rememberId.userId) ?: return "" to ""
        return user.account to user.pwd
    }

    fun listenUserFromDb(id: Int): Flow<UserModel?> =
        UserDbHelper.observeUser(id)
            .catch { e -> Log.e(TAG, "listenUserChanged error = ${e.message}", e) }
            .map { it?.toModel() }


    suspend fun handleLogout() {
        clearDiskLoginInfo()
    }


    private suspend fun clearDiskLoginInfo() {
        loginInfo.updateData {
            it.toBuilder()
                .setId(0)
                .build()
        }
    }

    private suspend fun updateDiskRememberInfo(id: Int) {
        rememberAccount.updateData {
            it.toBuilder()
                .setUserId(id)
                .build()
        }
    }


    @Suppress("unused")
    suspend fun isLoginSuspend(): Boolean = loginInfo.data.first().run {
        if (id == 0) return@run false
        UserDbHelper.query(id) ?: return@run false
        true
    }

}