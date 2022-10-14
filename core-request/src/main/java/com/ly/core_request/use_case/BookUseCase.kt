package com.ly.core_request.use_case

import com.ly.core_model.BookModel
import com.ly.core_request.Response
import com.ly.core_request.handleRequestError
import com.ly.core_request.local_logic.LocalBookLogic
import javax.inject.Inject

class BookUseCase @Inject constructor() {

    suspend fun getLastedBookInfo(userId: Int): Response<BookModel> =
        handleRequestError { LocalBookLogic.getLastedBookInfo(userId) }

    suspend fun getUserAllBooks(userId: Int): Response<List<BookModel>> =
        handleRequestError { LocalBookLogic.getUserAllBooks(userId) }
}