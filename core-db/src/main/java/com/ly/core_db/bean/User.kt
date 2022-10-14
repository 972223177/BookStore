package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 所有用户都存到这个表里，如果有搭服务就替换下这里
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val account: String = "",
    val avatar: String = "",
    val pwd: String = "",
    val recentReadBookId: Int = 0,
    val lastUpdateTime: Long = System.currentTimeMillis(),
    val createTime: Long,
)

