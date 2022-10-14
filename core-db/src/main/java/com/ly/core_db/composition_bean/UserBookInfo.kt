package com.ly.core_db.composition_bean

import androidx.room.Embedded
import androidx.room.Relation
import com.ly.core_db.bean.Book
import com.ly.core_db.bean.UserBook

data class UserBookInfo(
    @Embedded val userBook: UserBook,
    @Relation(
        parentColumn = "bookId",
        entityColumn = "id"
    )
    val book: Book,
)
