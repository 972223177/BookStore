package com.ly.common.logic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import api.UserOuterClass.User
import com.ly.utils.base.appContext
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.flow.first
import java.io.InputStream
import java.io.OutputStream

internal object LoginSerializer : Serializer<User> {
    override val defaultValue: User = User.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): User {
        return catchOrNull {
            User.parseFrom(input)
        } ?: User.getDefaultInstance()
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.loginDataStore: DataStore<User> by dataStore(
    fileName = "login.pb",
    serializer = LoginSerializer
)

internal val loginInfo: DataStore<User> get() = appContext.loginDataStore


/**
 * 随便代替下
 */
@Suppress("unused")
suspend fun isLogin(): Boolean = loginInfo.data.first().run {
    this.isLogin()
}

fun User.isLogin(): Boolean = email.isNotEmpty() && password.isNotEmpty()



