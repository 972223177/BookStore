package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_categories")
data class BookCategory(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val createTime: Long,
    val updateTime: Long
)