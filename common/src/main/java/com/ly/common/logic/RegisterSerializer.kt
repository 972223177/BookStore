package com.ly.common.logic

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import api.UserOuterClass.RegisterList
import com.ly.utils.base.appContext
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.flow.Flow
import java.io.InputStream
import java.io.OutputStream

internal object RegisterSerializer : Serializer<RegisterList> {
    override val defaultValue: RegisterList = RegisterList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RegisterList = catchOrNull {
        RegisterList.parseFrom(input)
    } ?: RegisterList.getDefaultInstance()

    override suspend fun writeTo(t: RegisterList, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.registerDataSource by dataStore(
    fileName = "registers.pb",
    serializer = RegisterSerializer
)

@Suppress("unused")
internal val registerFlow: Flow<RegisterList> get() = appContext.registerDataSource.data