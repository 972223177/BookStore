package com.ly.core_db.helpers

import com.ly.core_db.bean.User
import com.ly.core_db.bean.UserBook
import com.ly.core_db.bean.UserFollow
import com.ly.core_db.composition_bean.UserInfo
import com.ly.core_db.userDao
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

@Suppress("unused")
object UserDbHelper {

    suspend fun query(id: Int): User? = catchOrNull {
        userDao.query(id)
    }

    suspend fun queryInfo(id: Int): UserInfo? = catchOrNull {
        userDao.queryInfo(id)
    }

    fun observeUser(id: Int): Flow<User?> = userDao.observeUser(id).catch { e ->
        e.printStackTrace()
    }.flowOn(Dispatchers.IO)

    suspend fun update(userBean: User): Boolean = catchOrNull {
        userDao.update(userBean)
        true
    } ?: false

    suspend fun queryLikeAccount(account: String): List<User> =
        catchOrNull { userDao.queryLikeAccount(account) } ?: emptyList()

    suspend fun addUserBook(userBook: UserBook): Boolean = catchOrNull {
        userDao.addUserBook(userBook)
        true
    } ?: false

    suspend fun addUserFollow(follow: UserFollow): Boolean = catchOrNull {
        userDao.addFollowInfo(follow)
        true
    } ?: false

    suspend fun queryByAccount(account: String): User? =
        catchOrNull { userDao.queryByAccount(account) }

    suspend fun add(userBean: User): Boolean = catchOrNull {
        userDao.add(userBean)
        true
    } ?: false

    suspend fun sumOfFollowCount(userId: Int): Int = catchOrNull {
        userDao.sumOfFollowCount(userId)
    } ?: 0

    suspend fun sumOfFollowingCount(userId: Int) = catchOrNull {
        userDao.sumOfFollowingCount(userId)
    } ?: 0
}