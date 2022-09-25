package com.ly.common.serializer

import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import api.SettingOuterClass
import com.ly.utils.common.catchOrNull
import java.io.InputStream
import java.io.OutputStream

internal object SettingSerializer : Serializer<SettingOuterClass.Setting> {
    override val defaultValue: SettingOuterClass.Setting = SettingOuterClass.Setting.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingOuterClass.Setting {
        return catchOrNull {
            SettingOuterClass.Setting.parseFrom(input)
        } ?: SettingOuterClass.Setting.getDefaultInstance()
    }

    override suspend fun writeTo(t: SettingOuterClass.Setting, output: OutputStream) {
        t.writeTo(output)
    }
}

internal val Context.settingDataStore by dataStore(
    fileName = "setting.pb",
    serializer = SettingSerializer
)