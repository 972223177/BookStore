package com.ly.utils.base

import android.app.Application
import android.content.Context
import com.liulishuo.okdownload.core.file.FixProcessFileStrategy
import com.ly.utils.ui.WebViewHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private const val APP_JOB_KEY = "com.ly.utils.coroutine.APP_JOB_KEY"

/**
 * 一个全局coroutineScope，代替GlobalScope。当全局的handler用即可
 */
abstract class UtilsApplication : Application() {
    private val mCoroutineCache = mutableMapOf<String, CoroutineScope>()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        coroutineApp = this
    }

    override fun onCreate() {
        super.onCreate()
        FixProcessFileStrategy.wrapper(this)
        launchAppScope(Dispatchers.Default) {
            WebViewHelper.X5Starter.init(this@UtilsApplication)
        }
        launchAppScope {
            WebViewHelper.Manager.prepare(this@UtilsApplication)
        }
    }

    fun getTag(key: String): CoroutineScope? = synchronized(mCoroutineCache) {
        mCoroutineCache[key]
    }

    fun setTagIfAbsent(key: String, scope: CoroutineScope): CoroutineScope {
        var previous: CoroutineScope?
        synchronized(mCoroutineCache) {
            previous = mCoroutineCache[key]
            if (previous == null) {
                mCoroutineCache[key] = scope
            }
        }
        return previous ?: scope
    }


    override fun onTerminate() {
        super.onTerminate()
        //模拟器会走这里
        WebViewHelper.Manager.clear()
        mCoroutineCache.values.forEach {
            if (it is Closeable) {
                try {
                    it.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    companion object {
        internal var coroutineApp: UtilsApplication? = null
    }
}

@Suppress("unused")
val appContext: Context
    get() {
        return UtilsApplication.coroutineApp
            ?: throw IllegalStateException("Before using this method,please make sure your application extended UtilsApplication")
    }

/**
 * 启动一个SupervisorJob类型的协程，并且如果在主线程上就立马执行
 * 注意：先注意
 */
@Suppress("unused")
fun launchAppScope(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    val app = UtilsApplication.coroutineApp
        ?: throw IllegalStateException("Before using this method,please make sure your application extended UtilsApplication")
    return app.run {
        (getTag(APP_JOB_KEY) ?: setTagIfAbsent(
            APP_JOB_KEY,
            CloseableCoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
        )).launch(context, start, block)
    }
}

internal class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context

    override fun close() {
        coroutineContext.cancel()
    }
}