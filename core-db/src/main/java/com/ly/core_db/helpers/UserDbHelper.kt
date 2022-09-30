package com.ly.core_db.helpers

import com.ly.core_db.bean.UserBean
import com.ly.core_db.userDao
import com.ly.utils.common.catchOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

@Suppress("unused")
object UserDbHelper {

    suspend fun query(id: Int): UserBean? =catchOrNull {
        userDao.query(id)
    }
    fun observeUser(id: Int): Flow<UserBean?> = userDao.observeUser(id).catch { e ->
        e.printStackTrace()
    }.flowOn(Dispatchers.IO)

    suspend fun update(userBean: UserBean): Boolean = catchOrNull {
        userDao.update(userBean)
        true
    } ?: false

    suspend fun queryLikeAccount(account: String): List<UserBean> =   catchOrNull { userDao.queryLikeAccount(account) } ?: emptyList()

    suspend fun queryByAccount(account: String): UserBean? = catchOrNull { userDao.queryByAccount(account) }

    suspend fun add(userBean: UserBean): Boolean = catchOrNull {
        userDao.add(userBean)
        true
    } ?: false
}