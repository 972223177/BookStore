package com.ly.common.serializer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.ly.utils.base.appContext
import com.ly.utils.common.catchOrNull
import login.LoginInfoOuterClass.LoginInfo
import java.io.InputStream
import java.io.OutputStream

internal object LoginSerializer : Serializer<LoginInfo> {
    override val defaultValue: LoginInfo = LoginInfo.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LoginInfo = catchOrNull {
        LoginInfo.parseFrom(input)
    } ?: LoginInfo.getDefaultInstance()

    override suspend fun writeTo(t: LoginInfo, output: OutputStream) {
        t.writeTo(output)
    }

}

internal val Context.loginDataStore: DataStore<LoginInfo> by dataStore(
    fileName = "loginInfo.pb",
    serializer = LoginSerializer
)

internal val loginInfo: DataStore<LoginInfo> get() = appContext.loginDataStore





