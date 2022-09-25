package com.ly.core_db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ly.core_db.user.User
import com.ly.core_db.user.UserDao
import com.ly.utils.base.appContext


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {



        val db by lazy {
            Room.databaseBuilder(appContext, BookDatabase::class.java, "bookDatabase")
                .build()
        }
    }
}

val bookDb: BookDatabase get() = BookDatabase.db

val userDao: UserDao get() = bookDb.userDao()