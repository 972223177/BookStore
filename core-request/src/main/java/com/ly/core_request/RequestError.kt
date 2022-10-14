package com.ly.core_request

class RequestError(val code: Int, msg: String) : Exception(msg)

inline fun <T> handleRequestError(block: () -> Response<T>): Response<T> {
    return try {
        block()
    } catch (e: Exception) {
        if (e is RequestError) {
            return Response(null, e.code, e.message ?: "")
        } else {
            throw e
        }
    }
}