package com.ly.core_model

data class UserModel(
    val id: Int = 0,
    val name: String = "",
    val avatar: String = "",
    val lastUpdateTime: Long = System.currentTimeMillis()
) {
    companion object {
        val defaultInstance = UserModel()
    }
}
