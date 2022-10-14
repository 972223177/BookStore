package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_follows")
data class UserFollow(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val followId: Int,
    val userId: Int,
    val createTime: Long,
    val updateTime: Long
)