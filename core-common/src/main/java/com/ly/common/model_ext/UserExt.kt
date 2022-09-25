package com.ly.common.model_ext

import com.ly.core_db.user.User
import com.ly.core_model.UserModel

fun User.toModel(): UserModel = UserModel(
    id = id,
    name = name,
    lastUpdateTime = lastUpdateTime
)