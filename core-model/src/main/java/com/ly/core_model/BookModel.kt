package com.ly.core_model

data class BookModel(
    val bookId: Int = 0,
    val bookDes: String = "",
    val price: Double = 0.0,
    val isAudioType: Boolean = false,
    val bookCover: String = "",
    val bookPrimaryColor: Long = 0L,
    val bookName: String = "",
    val isPurchased: Boolean = false,
    val readInfo: ReadInfo? = null,
    val updateTime: Long = 0L
)

data class ReadInfo(
    val chapterId: Int,
    val chapterName: String,
    val readProgress: Double
)