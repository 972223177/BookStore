package com.ly.core_model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String = "",
    val lastUpdateTime: Long = System.currentTimeMillis(),
    val followCount: Int = 0,
    val followingCount: Int = 0,
) : Parcelable
