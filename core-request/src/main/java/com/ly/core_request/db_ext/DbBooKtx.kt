package com.ly.core_request.db_ext

import com.ly.core_db.composition_bean.UserBookInfo
import com.ly.core_model.BookModel
import com.ly.core_model.ReadInfo
import com.ly.utils.common.catchOrNull
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal fun UserBookInfo.toModel(): BookModel {
    val readInfo = ReadInfo(
        chapterId = userBook.readChapterId,
        chapterName = userBook.readChapterName,
        readProgress = catchOrNull { userBook.readChapterIndex * 1.0 / book.chapterCount }
            ?: 0.0
    )
    return BookModel(
        bookId = book.id,
        bookDes = book.des,
        price = book.price,
        isAudioType = book.isAudioType,
        bookCover = book.cover,
        bookPrimaryColor = book.color,
        bookName = book.name,
        isPurchased = userBook.isPurchased,
        readInfo = readInfo,
        updateTime = book.updateTime
    )
}

fun BookModel.isUpdated(): Boolean {
    val time = updateTime
    if (time == 0L) return false
    val now = System.currentTimeMillis()
    val diff = now - time
    if (diff < 0) return false
    val duration = diff.toDuration(DurationUnit.MILLISECONDS)
    val day = duration.inWholeDays
    return day < 3
}