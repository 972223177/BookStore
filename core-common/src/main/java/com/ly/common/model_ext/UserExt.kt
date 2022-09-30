package com.ly.common.model_ext

import com.ly.core_db.bean.UserBean
import com.ly.core_model.UserModel

fun UserBean.toModel(): UserModel = UserModel(
    id = id,
    name = name,
    avatar = avatar,
    lastUpdateTime = lastUpdateTime
)