package com.ly.core_request.db_ext

import com.ly.core_db.composition_bean.UserInfo
import com.ly.core_model.UserModel

internal fun UserInfo.toModel(): UserModel = UserModel(
    id = user.id,
    name = user.name,
    avatar = user.avatar,
    lastUpdateTime = user.lastUpdateTime,
    followCount = follows.size,
    followingCount = followings.size
)