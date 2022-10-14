package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BookChapter(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val bookId: Int,
    val index: Int,
    val name: String,
    val content: String,
    val createTime: Long,
    val updateTime: Long
)