package com.ly.common.serializer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.ly.utils.base.appContext
import com.ly.utils.common.catchOrNull
import login.LoginInfoOuterClass.RememberAccount
import java.io.InputStream
import java.io.OutputStream

internal object RememberAccountSerializer : Serializer<RememberAccount> {
    override val defaultValue: RememberAccount = RememberAccount.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RememberAccount = catchOrNull {
        RememberAccount.parseFrom(input)
    } ?: RememberAccount.getDefaultInstance()

    override suspend fun writeTo(t: RememberAccount, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.rememberAccountDataSource by dataStore(
    fileName = "rememberMe.pb",
    serializer = RememberAccountSerializer
)

internal val rememberAccount: DataStore<RememberAccount> get() = appContext.rememberAccountDataSource