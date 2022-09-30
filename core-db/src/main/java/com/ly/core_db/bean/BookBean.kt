package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "book_categories")
data class BookCategoryBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val createTime: Long,
    val updateTime: Long
)

@Entity(
    tableName = "books",
    foreignKeys = [ForeignKey(
        entity = BookCategoryBean::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"]
    )]
)
data class BookBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val categoryId: Int,
    val name: String,
    val des: String,
    val price: Double,
    val isAudioType: Boolean,
    val cover: String,
    val author: String,
    val primaryColor: Int,
    val createTime: Long,
    val updateTime: Long
)

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = BookBean::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ChapterBean(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val bookId: Int,
    val name: String,
    val content: String,
    val createTime: Long,
    val updateTime: Long
)


@Entity(
    tableName = "book_remarks",
    foreignKeys = [
        ForeignKey(entity = BookBean::class, parentColumns = ["id"], childColumns = ["targetBookId"]),
        ForeignKey(entity = UserBean::class, parentColumns = ["id"], childColumns = ["createOwnerId"])
    ]
)
data class BookRemarkBean(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val createOwnerId: Int,
    val targetBookId: Int,
    val remark: String,
    val createTime: Long,
    val updateTime: Long
)

