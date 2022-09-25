package com.ly.core_db.user

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * 所有用户都存到这个表里，如果有搭服务就替换下这里
 */
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String = "",
    val account: String = "",
    val pwd: String = "",
    val lastUpdateTime: Long = System.currentTimeMillis(),
)

@Dao
interface UserDao {

    @Query("select * from User where id =:id limit 1")
    fun query(id: Long): User?

    @Query("select * from User where id =:id limit 1")
    fun observeUser(id: Long): Flow<User?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)

    @Query("select * from User where id like :account")
    fun queryLikeAccount(account: String): List<User>

    @Query("select * from User where account =:account limit 1")
    fun queryByAccount(account: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun add(user: User)
}