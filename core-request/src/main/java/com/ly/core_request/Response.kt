package com.ly.core_request

data class Response<T>(val data: T?, val code: Int, val msg: String) {

    val success: Boolean get() = code == Success

    companion object {
        const val Success = 200
    }
}


