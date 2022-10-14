package com.ly.common.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import api.SettingOuterClass.Setting
import com.ly.common.serializer.settingDataStore
import com.ly.core_request.local_logic.TestDbHelper
import com.ly.utils.base.appContext
import com.ly.utils.base.launchAppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

internal val Context.configDataStore: DataStore<Preferences> by preferencesDataStore(name = "config")

@Suppress("unused", "MemberVisibilityCanBePrivate")
object ConfigHelper {
    private const val DATASTORE_CONFIG_TEST = "datastore_config_test"
    private val testKey = booleanPreferencesKey(DATASTORE_CONFIG_TEST)
    var isDarkMode = false
        set(value) {
            field = value
            launchAppScope {
                appContext.settingDataStore.updateData {
                    it.toBuilder()
                        .setIsDarkMode(value)
                        .build()
                }
            }
        }

    val settingFlow: Flow<Setting>
        get() = appContext.settingDataStore.data

    suspend fun init() {
        isDarkMode = settingFlow.first().isDarkMode
        val isInit =  appContext.configDataStore.data.firstOrNull()?.get(testKey)?:false
        if (!isInit) {
            TestDbHelper.initDb()
            appContext.configDataStore.edit { edit ->
                edit[testKey] = true
            }
        }
        UserHelper.init()
    }

}