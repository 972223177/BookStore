package com.ly.common.utils

import api.SettingOuterClass.Setting
import com.ly.common.logic.LocalLoginLogic
import com.ly.common.serializer.settingDataStore
import com.ly.utils.base.appContext
import com.ly.utils.base.launchAppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


@Suppress("unused", "MemberVisibilityCanBePrivate")
object ConfigHelper {
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

    fun init() {
        runBlocking {
            isDarkMode = settingFlow.first().isDarkMode
            UserHelper.init()
        }
    }

}