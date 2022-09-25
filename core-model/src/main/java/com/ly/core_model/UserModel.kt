package com.ly.core_model

data class UserModel(
    val id:Long = 0,
    val name:String = "",
    val lastUpdateTime:Long = System.currentTimeMillis()
){
    companion object{
        val defaultInstance = UserModel()
    }
}
