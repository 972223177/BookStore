package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_remarks")
data class BookRemark(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val createOwnerId: Int,
    val targetBookId: Int,
    val remark: String,
    val createTime: Long,
    val updateTime: Long
)

