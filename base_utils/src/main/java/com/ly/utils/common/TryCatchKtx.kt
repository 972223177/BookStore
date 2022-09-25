package com.ly.utils.common

inline fun catch(printStacktrace: Boolean = false, block: () -> Unit) {
    try {
        block()
    } catch (e: Throwable) {
        if (printStacktrace) {
            e.printStackTrace()
        }
    }
}

inline fun <T> catchOrNull(printStacktrace: Boolean = false, block: () -> T?): T? = try {
    block()
} catch (e: Throwable) {
    if (printStacktrace) {
        e.printStackTrace()
    }
    null
}

