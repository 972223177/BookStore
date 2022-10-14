package com.ly.core_db.dao

import androidx.room.*
import com.ly.core_db.bean.User
import com.ly.core_db.bean.UserBook
import com.ly.core_db.bean.UserFollow
import com.ly.core_db.composition_bean.UserInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("select * from users where id =:id limit 1")
    suspend fun query(id: Int): User?

    @Transaction
    @Query("select * from users where id =:id limit 1")
    suspend fun queryInfo(id: Int): UserInfo?

    @Query("select * from users where id =:id limit 1")
    fun observeUser(id: Int): Flow<User?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addFollowInfo(follow: UserFollow)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userBean: User)

    @Query("select * from users where account like :account")
    suspend fun queryLikeAccount(account: String): List<User>

    @Query("select * from users where account =:account limit 1")
    suspend fun queryByAccount(account: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun add(userBean: User)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addUserBook(userBook: UserBook)

    @Query("select count(followId = :userId) from user_follows")
    suspend fun sumOfFollowCount(userId: Int): Int

    @Query("select count(userId = :userId) from user_follows")
    suspend fun sumOfFollowingCount(userId: Int): Int

    @Query("delete from user_follows where followId = :followId")
    suspend fun removeFollowing(followId: Int): Int

}