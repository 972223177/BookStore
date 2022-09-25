package com.ly.utils.glide

import android.content.Context
import androidx.annotation.Keep
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@Suppress("unused")
@Keep
@GlideModule
class BaseAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setImageDecoderEnabledForBitmaps(true)
        val calculator = MemorySizeCalculator.Builder(context).build()
        val defaultMemoryCacheSize = calculator.memoryCacheSize
        val defaultBitmapPoolSize = calculator.bitmapPoolSize
        val defaultArrayPoolSize = calculator.arrayPoolSizeInBytes
        builder.setMemoryCache(LruResourceCache(defaultMemoryCacheSize / 2L))
        builder.setBitmapPool(LruBitmapPool(defaultBitmapPoolSize / 2L))
        builder.setArrayPool(LruArrayPool(defaultArrayPoolSize / 2))
        builder.setDefaultRequestOptions(RequestOptions.formatOf(DecodeFormat.PREFER_RGB_565))
    }

}