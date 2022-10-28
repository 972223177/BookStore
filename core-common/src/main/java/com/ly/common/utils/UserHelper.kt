package com.ly.common.utils

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ly.core_model.RememberInfo
import com.ly.core_model.UserModel
import com.ly.utils.base.appContext
import com.ly.utils.base.launchAppScope
import com.ly.utils.common.catchOrNull
import com.ly.utils.common.globalJson
import kotlinx.coroutines.flow.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

internal val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "user_reference")

@Suppress("unused")
object UserHelper {

    private const val DATASTORE_USER = "datastore_user"
    private const val DATASTORE_REMEMBER_ME = "datastore_remember_me"

    private val userKey = stringPreferencesKey(DATASTORE_USER)

    private val rememberKey = stringPreferencesKey(DATASTORE_REMEMBER_ME)
    var isLogin: Boolean = false
        internal set

    private val mUser = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = mUser

    private val dataStore: DataStore<Preferences> get() = appContext.userDataStore
    internal fun init() {
        launchAppScope {
            dataStore.data.map { pre ->
                val json = pre[userKey] ?: ""
                catchOrNull {
                    globalJson.decodeFromString<UserModel>(json)
                }
            }.catch { e ->
                printLog(e.message ?: "")
            }.collectLatest { model ->
                isLogin = (model?.id ?: 0) > 0
                mUser.value = model
            }
        }
    }

    suspend fun cacheLoginInfo(userModel: UserModel) {
        dataStore.edit {
            val json = globalJson.encodeToString(userModel)
            it[userKey] = json
        }
    }

    suspend fun rememberMe(account: String, pwd: String) {
        if (account.isEmpty() || pwd.isEmpty()) return
        dataStore.edit {
            val info = RememberInfo(account, pwd)
            //这里可以做加密
            val json = globalJson.encodeToString(info)
            it[rememberKey] = json
        }
    }

    suspend fun handleLogout() {
        clearDiskLoginInfo()
        mUser.value = null
    }

    suspend fun getRememberInfo(): Pair<String, String> {
        return catchOrNull {
            val json = dataStore.data.map { it[rememberKey] }.first() ?: ""
            val (account, pwd) = globalJson.decodeFromString<RememberInfo>(json)
            account to pwd
        } ?: ("" to "")
    }

    private fun printLog(msg: String) {
        Log.d("UserHelper", msg)
    }

    private suspend fun clearDiskLoginInfo() {
        dataStore.edit {
            it[userKey] = ""
        }
    }

}