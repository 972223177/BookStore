package com.ly.core_db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ly.core_db.bean.UserBean
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("select * from users where id =:id limit 1")
    suspend fun query(id: Int): UserBean?

    @Query("select * from users where id =:id limit 1")
    fun observeUser(id: Int): Flow<UserBean?>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userBean: UserBean)

    @Query("select * from users where account like :account")
    suspend fun queryLikeAccount(account: String): List<UserBean>

    @Query("select * from users where account =:account limit 1")
    suspend fun queryByAccount(account: String): UserBean?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(userBean: UserBean)
}