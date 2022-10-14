package com.ly.core_db.dao

import androidx.room.*
import com.ly.core_db.bean.Book
import com.ly.core_db.bean.BookCategory
import com.ly.core_db.bean.BookChapter
import com.ly.core_db.composition_bean.UserBookInfo

@Dao
interface BookDao {
    @Query("select * from books order by updateTime limit ((:page -1)*:pageSize),:pageSize")
    suspend fun getAllBook(page: Int, pageSize: Int): List<Book>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addBooks(books: List<Book>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addBook(book: Book)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addChapters(chapters: List<BookChapter>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addCategory(categories: List<BookCategory>)

    @Query("select * from books where id = :bookId limit 1")
    suspend fun queryBookById(bookId: Int): Book?

    @Query("select * from book_categories")
    suspend fun getAllCategory(): List<BookCategory>

    @Query("select * from books where categoryId = :categoryId")
    suspend fun queryBooksByCategoryId(categoryId: Int): List<Book>

    @Query("select * from chapters where bookId = :bookId order by createTime")
    suspend fun getBookAllChapters(bookId: Int): List<BookChapter>

    @Transaction
    @Query("select * from user_books where ownerId = :userId order by updateTime desc limit 1")
    suspend fun getLastReadBook(userId: Int): UserBookInfo?

    @Transaction
    @Query("select * from user_books where ownerId = :userId order by updateTime desc")
    suspend fun getUserBooks(userId: Int): List<UserBookInfo>

}