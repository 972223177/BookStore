package com.ly.core_db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * 所有用户都存到这个表里，如果有搭服务就替换下这里
 */
@Entity(tableName = "users")
data class UserBean(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val account: String = "",
    val avatar: String = "",
    val pwd: String = "",
    val follow: Int = 0,
    val following: Int = 0,
    val lastUpdateTime: Long = System.currentTimeMillis(),
    val createTime: Long,
)

@Entity(
    tableName = "book_libraries",
    foreignKeys = [ForeignKey(
        entity = UserBean::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"]
    )]
)
data class BookLibraryBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val ownerId: Int,
    val name: String,
    @ColumnInfo val createTime: Long,
    val updateTime: Long
)

@Entity(
    tableName = "user_books", foreignKeys = [
        ForeignKey(entity = UserBean::class, parentColumns = ["id"], childColumns = ["ownerId"])
    ]
)
data class UsersBookBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val ownerId: Int,
    val createTime: Long,
    val updateTime: Long
)