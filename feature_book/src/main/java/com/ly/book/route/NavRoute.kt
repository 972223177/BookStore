package com.ly.book.route

import android.net.Uri
import com.ly.book.route.args.PicPreData
import com.ly.utils.common.globalJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.serializer
import kotlin.reflect.full.starProjectedType

object NavRoute {
    private const val Root = "book/"
    const val PageMain = "$Root/Main"
    const val PageWaiting = "$Root/Waiting"
    const val PageLoginMain = "$Root/Login"

    const val PagePicPreview_Key = "urls"
    private const val PagePicPreview = "$Root/PicPreview"
    const val PagePicPreviewWithKey = "$PagePicPreview/{$PagePicPreview_Key}"

    fun picPre(urls: List<String>): String = routeFormat(PagePicPreview, PicPreData(urls))

    @Suppress("SameParameterValue")
    private fun routeFormat(path: String, arg: Any): String {
        return path.appendArg(
            when (arg) {
                is String,
                is Int,
                is Float,
                is Double,
                is Boolean,
                is Long -> arg.toString()

                else -> {
                    Uri.encode(with(globalJson) {
                        encodeToString(
                            serializer = serializersModule.serializer(arg::class.starProjectedType),
                            value = arg
                        )
                    })
                }
            }
        )
    }

    inline fun <reified T> jsonDecode(json: String): T {
        return globalJson.decodeFromString(Uri.decode(json))
    }

    private fun String.appendArg(argFormatted: String): String = "$this/$argFormatted"

    object Login {
        const val Main = "$PageLoginMain/Main"
        const val SignIn = "$PageLoginMain/SignIn"
        const val SignUp = "$PageLoginMain/SignUp"
    }
}