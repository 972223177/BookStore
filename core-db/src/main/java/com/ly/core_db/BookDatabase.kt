@file:Suppress("unused")

package com.ly.core_db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ly.core_db.bean.*
import com.ly.core_db.dao.BookDao
import com.ly.core_db.dao.UserDao
import com.ly.utils.base.appContext


@Database(
    entities = [User::class,
        UserBook::class,
        BookCategory::class,
        UserFollow::class,
        Book::class,
        BookChapter::class,
        BookRemark::class],
    version = 1,
    exportSchema = true,
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun bookDao(): BookDao

    companion object {

        val db by lazy {
            Room.databaseBuilder(appContext, BookDatabase::class.java, "bookDatabase")
                .fallbackToDestructiveMigrationFrom()
                .build()
        }
    }
}

val bookDb: BookDatabase get() = BookDatabase.db

val userDao: UserDao get() = bookDb.userDao()

val bookDao: BookDao get() = bookDb.bookDao()