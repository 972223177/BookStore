package com.ly.core_request.local_logic

import com.ly.core_db.bookDao
import com.ly.core_model.BookModel
import com.ly.core_request.RequestError
import com.ly.core_request.Response
import com.ly.core_request.db_ext.toModel

object LocalBookLogic {
    suspend fun getLastedBookInfo(userId: Int): Response<BookModel> {
        val lastedBook = bookDao.getLastReadBook(userId) ?: throw RequestError(-1, "未找到")
        return Response(lastedBook.toModel(), Response.Success, "")
    }

    suspend fun getUserAllBooks(userId: Int): Response<List<BookModel>> {
        val books = bookDao.getUserBooks(userId).map { it.toModel() }
        return Response(books, Response.Success, "")
    }
}