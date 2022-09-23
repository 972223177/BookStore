package com.ly.common.db.user

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val account: String,
    val pwd: String,
    val lastUpdateTime: Long = System.currentTimeMillis(),
)

@Dao
interface UserDao {

    @Query("select * from User where id =:id")
    fun query(id: Long): User?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)

    @Query("select * from User where id like :account")
    fun queryByAccount(account: String): List<User>
}