package com.ly.common.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ly.common.db.user.User
import com.ly.common.db.user.UserDao
import com.ly.utils.base.appContext

@Database(entities = [User::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun userDao():UserDao

    companion object{
        val db by lazy {
            Room.databaseBuilder(appContext,BookDatabase::class.java,"bookDatabase")
        }
    }
}