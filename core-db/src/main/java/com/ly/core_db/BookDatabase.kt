package com.ly.core_db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ly.core_db.bean.BookBean
import com.ly.core_db.bean.BookCategoryBean
import com.ly.core_db.bean.BookLibraryBean
import com.ly.core_db.bean.BookRemarkBean
import com.ly.core_db.bean.ChapterBean
import com.ly.core_db.bean.UserBean
import com.ly.core_db.bean.UsersBookBean
import com.ly.core_db.dao.UserDao
import com.ly.utils.base.appContext


@Database(
    entities = [UserBean::class,
        BookLibraryBean::class,
        UsersBookBean::class,
        BookCategoryBean::class,
        BookBean::class,
        ChapterBean::class,
        BookRemarkBean::class],
    version = 1,
    exportSchema = true,
)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

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