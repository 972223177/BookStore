package com.ly.utils.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pools
import androidx.core.view.LayoutInflaterCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 可以在子线程初始化view的inflater
 * @author ly
 */
class AsyncLayoutInflater(context: Context) {

    private val mRequestPool = Pools.SynchronizedPool<InflateRequest>(10)
    private val mInflater = BasicInflater(context)

    suspend fun inflate(
        @LayoutRes resId: Int,
        parent: ViewGroup?,
    ): View = withContext(Dispatchers.Main.immediate) {
        return@withContext obtainRequest().apply {
            this.inflater = this@AsyncLayoutInflater
            this.resId = resId
            this.parent = parent
        }.enqueue().let {
            if (it.view == null) {
                it.view = it.inflater?.mInflater?.inflate(it.resId, it.parent, false)
                Log.d(TAG, "retry inflating on the UI thread")
            }
            Log.d(
                TAG,
                "inflate finished ,current thread name:${Thread.currentThread().name}"
            )
            val view = it.view!!
            releaseRequest(it)
            view
        }
    }

    private suspend fun InflateRequest.enqueue(): InflateRequest =
        withContext(Dispatchers.Default) {
            //这里子线程中去创建一个view
            try {
                view = inflater?.mInflater?.inflate(resId, parent, false)
                Log.d(
                    TAG,
                    "inflate on the other thread ,current thread name:${Thread.currentThread().name}"
                )
            } catch (e: RuntimeException) {
                Log.w(
                    TAG, "Failed to inflate resource in the background! Retrying on the UI"
                            + " thread", e
                )
            }
            return@withContext this@enqueue
        }


    private fun obtainRequest(): InflateRequest {
        var obj = mRequestPool.acquire()
        if (obj == null) {
            obj = InflateRequest()
        }
        return obj
    }

    private fun releaseRequest(obj: InflateRequest) {
        obj.apply {
            inflater = null
            parent = null
            resId = 0
            view = null
            mRequestPool.release(obj)
        }
    }

}


private class InflateRequest(
    var inflater: AsyncLayoutInflater? = null,
    var parent: ViewGroup? = null,
    var resId: Int = 0,
    var view: View? = null,
)

private class BasicInflater(context: Context?) : LayoutInflater(context) {
    init {
        if (context is AppCompatActivity) {
            // 手动setFactory2，兼容AppCompatTextView等控件
            val appCompatDelegate = context.delegate
            if (appCompatDelegate is Factory2) {
                LayoutInflaterCompat.setFactory2(this, appCompatDelegate)
            }
        }
    }

    override fun cloneInContext(newContext: Context): LayoutInflater {
        return BasicInflater(newContext)
    }

    @Throws(ClassNotFoundException::class)
    override fun onCreateView(name: String, attrs: AttributeSet): View {
        for (prefix in sClassPrefixList) {
            try {
                val view = createView(name, prefix, attrs)
                if (view != null) {
                    return view
                }
            } catch (e: ClassNotFoundException) {
                // In this case we want to let the base class take a crack
                // at it.
            }
        }
        return super.onCreateView(name, attrs)
    }

    companion object {
        private val sClassPrefixList = arrayOf(
            "android.widget.",
            "android.webkit.",
            "android.app."
        )
    }
}

private const val TAG = "AsyncLayoutInflater"
