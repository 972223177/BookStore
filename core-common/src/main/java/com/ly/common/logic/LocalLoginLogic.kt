package com.ly.common.logic

import com.ly.common.model_ext.toModel
import com.ly.common.serializer.loginInfo
import com.ly.common.serializer.rememberAccount
import com.ly.core_db.user.User
import com.ly.core_db.user.UserDbHelper
import com.ly.core_model.UserModel
import com.ly.utils.base.launchAppScope
import com.ly.utils.common.catch
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

@Suppress("unused")
object LocalLoginLogic {

    var currentUser: UserModel = UserModel.defaultInstance
        private set

    var isLogin: Boolean = false
        private set


    suspend fun login(
        account: String,
        pwd: String,
        rememberMe: Boolean = false
    ): Pair<Boolean, String> {
        val existedUser = UserDbHelper.queryByAccount(account) ?: return false to "请先注册"
        if (existedUser.pwd != pwd) return false to "密码错误"
        val updatedUser = existedUser.copy(lastUpdateTime = System.currentTimeMillis())
        UserDbHelper.update(updatedUser)
        updateDiskRememberInfo(if (rememberMe) existedUser.id else 0L)
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
        val user = User(account = account, name = name, pwd = pwd)
        val addResult = UserDbHelper.add(user)
        if (!addResult) {
            return false to "注册失败"
        }
        updateDiskRememberInfo(0L)
        return true to ""
    }

    suspend fun getRememberInfo(): Pair<String, String> {
        val rememberId = catchOrNull {
            rememberAccount.data.firstOrNull()
        }
        if (rememberId == null || rememberId.userId == 0L) {
            return "" to ""
        }
        val user = UserDbHelper.query(rememberId.userId) ?: return "" to ""
        return user.account to user.pwd
    }


    /**
     * 获取用户信息的流
     */
    fun listenUserChanged(id: Long = currentUser.id): Flow<UserModel?> =
        UserDbHelper.observeUser(id).map {
            it?.toModel()
        }

    internal suspend fun init() {
        catch {
            val info = loginInfo.data.firstOrNull()
            if (info != null && info.id > 0) {
                val user = UserDbHelper.query(info.id)
                if (user != null) {
                    isLogin = true
                    currentUser = UserModel(
                        id = user.id,
                        name = user.name,
                        lastUpdateTime = user.lastUpdateTime
                    )
                } else {
                    currentUser = UserModel.defaultInstance
                    isLogin = false
                    clearDiskLoginInfo()
                }
            }
        }
    }


    fun handleLogout() {
        launchAppScope {
            isLogin = false
            currentUser = UserModel.defaultInstance
            clearDiskLoginInfo()
        }


    }


    private suspend fun clearDiskLoginInfo() {
        loginInfo.updateData {
            it.toBuilder()
                .setId(0L)
                .build()
        }
    }

    private suspend fun updateDiskRememberInfo(id: Long) {
        rememberAccount.updateData {
            it.toBuilder()
                .setUserId(id)
                .build()
        }
    }


    @Suppress("unused")
    suspend fun isLoginSuspend(): Boolean = loginInfo.data.first().run {
        if (id == 0L) return@run false
        UserDbHelper.query(id) ?: return@run false
        true
    }

}