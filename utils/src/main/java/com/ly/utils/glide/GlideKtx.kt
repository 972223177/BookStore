@file:Suppress("unused")
package com.ly.utils.glide

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget

inline fun ImageView.load(
    model: Any,
    radius: Float = 0f,
    asBitmap: Boolean = false, //动图慎用
    useArgb8888: Boolean = false,//
    target: (ImageView) -> ImageViewTarget<*> = {
        if (asBitmap) {
            object : BitmapImageViewTarget(it) {}
        } else {
            object : DrawableImageViewTarget(it) {}
        }
    },
    option: RequestOptions.() -> Unit = {}
) {
    if (context.assertValidRequest()) {
        roundedCorners(radius)
        val requestManager = Glide.with(context)
        if (asBitmap) {
            val bitmapTarget = target(this) as BitmapImageViewTarget
            requestManager.asBitmap().load(model).apply(
                RequestOptions().format(
                    if (useArgb8888)
                        DecodeFormat.PREFER_ARGB_8888
                    else
                        DecodeFormat.PREFER_RGB_565
                ).apply(option)
            ).into(bitmapTarget)
        } else {
            val drawableTarget = target(this) as DrawableImageViewTarget
            requestManager.load(model).apply(RequestOptions().apply(option)).into(drawableTarget)
        }
    }
}

/**
 * 裁剪成圆形
 */
fun View.circle() {
    val currProvider = outlineProvider as? CircleOutlineProvider
    if (currProvider == null) {
        outlineProvider = CircleOutlineProvider()
        clipToOutline = true
    }
}

/**
 * 裁剪四个角
 */
fun View.roundedCorners(radius: Float) {
    val currProvider = outlineProvider as? RoundCornerOutlineProvider
    if (radius > 0f && (currProvider == null || radius != currProvider.radius)) {
        outlineProvider = RoundCornerOutlineProvider(radius)
        clipToOutline = true
    }
}

class RoundCornerOutlineProvider(val radius: Float) : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        if (view == null || outline == null) return
        outline.setRoundRect(0, 0, view.width, view.height, radius)
    }
}

/**
 * 圆
 */
class CircleOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View?, outline: Outline?) {
        if (view == null || outline == null) return
        val size = view.width.coerceAtLeast(view.height)
        outline.setOval(0, 0, size, size)
    }
}

/**
 * 尽量在recyclerview onViewRecycled时清理下
 */
fun ImageView.clear() {
    if (context.assertValidRequest()) {
        Glide.with(context).clear(this)
    }
}

fun Context.assertValidRequest(): Boolean {
    return when (this) {
        is Activity -> !isDestroy()
        is ContextWrapper -> {
            if (baseContext is Activity) {
                !(baseContext as Activity).isDestroy()
            } else {
                true
            }
        }
        else -> true
    }
}

private fun Activity.isDestroy(): Boolean {
    return isFinishing || isDestroyed
}