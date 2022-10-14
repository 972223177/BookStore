package com.ly.core_db.helpers

import com.ly.core_db.bean.Book
import com.ly.core_db.bean.BookCategory
import com.ly.core_db.bean.BookChapter
import com.ly.core_db.bookDao
import com.ly.core_db.composition_bean.UserBookInfo
import com.ly.utils.common.catchOrNull

@Suppress("unused")
object BookDbHelper {

    suspend fun getAllBook(page: Int, pageSize: Int): List<Book> = catchOrNull {
        bookDao.getAllBook(page, pageSize)
    } ?: emptyList()

    suspend fun addBook(book: Book): Boolean = catchOrNull {
        bookDao.addBook(book)
        true
    } ?: false

    suspend fun addChapters(chapters: List<BookChapter>): Boolean = catchOrNull {
        bookDao.addChapters(chapters)
        true
    } ?: false

    suspend fun addBooks(books: List<Book>): Boolean = catchOrNull {
        bookDao.addBooks(books)
        true
    } ?: false

    suspend fun addCategories(categories: List<BookCategory>): Boolean = catchOrNull {
        bookDao.addCategory(categories)
        true
    } ?: false

    suspend fun queryBookById(bookId: Int): Book? = catchOrNull {
        bookDao.queryBookById(bookId)
    }

    suspend fun getAllCategory(): List<BookCategory> = catchOrNull {
        bookDao.getAllCategory()
    } ?: emptyList()


    suspend fun getLastedBookInfo(userId: Int): UserBookInfo? = catchOrNull {
        bookDao.getLastReadBook(userId)
    }

}