package com.ly.common.logic

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import api.SettingOuterClass.Setting
import api.UserOuterClass.RegisterList
import api.UserOuterClass.User
import com.ly.utils.base.appContext
import com.ly.utils.base.launchAppScope
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.io.OutputStream

internal object SettingSerializer : Serializer<Setting> {
    override val defaultValue: Setting = Setting.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Setting {
        return catchOrNull {
            Setting.parseFrom(input)
        } ?: Setting.getDefaultInstance()
    }

    override suspend fun writeTo(t: Setting, output: OutputStream) {
        t.writeTo(output)
    }
}

val Context.settingDataStore by dataStore(
    fileName = "setting.pb",
    serializer = SettingSerializer
)

@Suppress("unused", "MemberVisibilityCanBePrivate")
object ConfigHelper {
    var isDarkMode = false
        private set

    var isLogin = false
        private set

    var user: User = User.getDefaultInstance()

    val isGuest: Boolean get() = !isLogin

    val settingFlow: Flow<Setting>
        get() = appContext.settingDataStore.data


    val userFlow: Flow<User> get() = appContext.loginDataStore.data

    fun updateUser(block: (User) -> User) {
        launchAppScope {
            updateUserSuspend(block)
        }
    }

    suspend fun isRegister(account: String): Boolean {
        val list = registerFlow.first().userList
        if (list.isEmpty()) return false
        return list.find { it.email == account } != null
    }

    suspend fun updateUserSuspend(block: (User) -> User) {
       val updated = loginInfo.updateData { block(it) }
        appContext.registerDataSource.updateData {
            val list = it.userList.toMutableList()

            RegisterList.newBuilder()
                .addAllUser(list)
                .build()
        }
    }

    fun init() {
        runBlocking {
            isDarkMode = settingFlow.first().isDarkMode
            user = loginInfo.data.first()
            isLogin = user.isLogin()
        }
    }

    fun logout() {
        isLogin = false
        user = User.getDefaultInstance()
        launchAppScope {
            loginInfo.updateData { User.getDefaultInstance() }
        }
    }


}