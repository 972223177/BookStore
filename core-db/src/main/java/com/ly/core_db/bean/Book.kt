package com.ly.core_db.bean

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    foreignKeys = [ForeignKey(
        entity = BookCategory::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"]
    )]
)
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val categoryId: Int,
    val name: String,
    val des: String,
    val price: Double,
    val isAudioType: Boolean = false,
    val cover: String,
    val chapterCount: Int,
    val author: String,
    val color: Long = 0L,
    val createTime: Long,
    val updateTime: Long = System.currentTimeMillis()
)




