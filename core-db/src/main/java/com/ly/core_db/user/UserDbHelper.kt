package com.ly.core_db.user

import com.ly.core_db.userDao
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

@Suppress("unused")
object UserDbHelper {

    suspend fun query(id: Long): User? = withContext(Dispatchers.IO) {
        catchOrNull {
            userDao.query(id)
        }
    }

    fun observeUser(id: Long): Flow<User?> = userDao.observeUser(id).catch { e ->
        e.printStackTrace()
    }.flowOn(Dispatchers.IO)

    suspend fun update(user: User): Boolean = withContext(Dispatchers.IO) {
        catchOrNull {
            userDao.update(user)
            true
        } ?: false
    }

    suspend fun queryLikeAccount(account: String): List<User> = withContext(Dispatchers.IO) {
        catchOrNull { userDao.queryLikeAccount(account) } ?: emptyList()
    }

    suspend fun queryByAccount(account: String): User? = withContext(Dispatchers.IO) {
        catchOrNull { userDao.queryByAccount(account) }
    }

    suspend fun add(user: User): Boolean = withContext(Dispatchers.IO) {
        catchOrNull {
            userDao.add(user)
            true
        } ?: false
    }
}