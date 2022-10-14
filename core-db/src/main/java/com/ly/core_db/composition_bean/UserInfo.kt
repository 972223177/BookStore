package com.ly.core_db.composition_bean

import androidx.room.Embedded
import androidx.room.Relation
import com.ly.core_db.bean.User
import com.ly.core_db.bean.UserFollow

data class UserInfo(
    @Embedded val user: User,
    @Relation(parentColumn = "id", entityColumn = "followId") val follows: List<UserFollow>,
    @Relation(parentColumn = "id", entityColumn = "userId") val followings: List<UserFollow>,
)
