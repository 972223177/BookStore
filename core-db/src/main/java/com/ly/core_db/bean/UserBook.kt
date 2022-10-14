package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_books", foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["ownerId"])
    ]
)
data class UserBook(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val ownerId: Int,
    val bookId: Int,
    val bookName: String,
    val readChapterId: Int,
    val readChapterName: String,
    val readChapterIndex: Int,
    val readProgress: Double,
    val isPurchased: Boolean,
    val createTime: Long,
    val updateTime: Long
)
