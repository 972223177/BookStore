package com.ly.core_model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String = "",
    val lastUpdateTime: Long = System.currentTimeMillis(),
    val followCount: Int = 0,
    val followingCount: Int = 0,
)
