package com.ly.book.route.args

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PicPreData(val urls: List<String> = emptyList(), val initialIndex: Int = 0)