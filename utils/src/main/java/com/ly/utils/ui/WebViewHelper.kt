package com.ly.utils.ui

import android.content.Context
import android.content.Intent
import android.content.MutableContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Looper
import android.view.ViewGroup
import com.ly.utils.common.simpleDownload
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.export.external.interfaces.IX5WebSettings
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.*
import kotlinx.coroutines.runBlocking
import okio.ByteString.Companion.encodeUtf8
import java.io.File
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.math.sqrt

@Suppress("unused", "SetJavaScriptEnabled", "DEPRECATION", "UNUSED_PARAMETER")
class WebViewHelper(private val webView: WebView) {
    private var onPageChangedListener: OnPageChangedListener? = null
    private var originalUrl = "about:blank"
    private var overrideUrlLoadingInterceptor: OverrideUrlLoadingInterceptor? = null

    init {
        initWebViewClient()
        initWebChromeClient()
    }

    fun evaluateJavascript(js: String, callback: (String) -> Unit): WebViewHelper {
        webView.evaluateJavascript(js, callback)
        return this
    }

    fun tryGoBack(): Boolean {
        val canBack = webView.canGoBack()
        if (canBack) webView.goBack()
        val backForwardList = webView.copyBackForwardList()
        val currentIndex = backForwardList.currentIndex
        if (currentIndex == 0) { //就已经在栈底的情况
            val currentUrl = backForwardList.currentItem.url
            val currentHost = Uri.parse(currentUrl).host
            //不是链接的情况
            if (currentHost.isNullOrBlank()) return false
            //也不是原始的链接
            if (originalUrl != currentUrl) return false
        }
        return canBack
    }

    fun tryGoForward(): Boolean {
        val canForward = webView.canGoForward()
        if (canForward) {
            webView.goForward()
        }
        return canForward
    }

    fun loadUrl(url: String?): WebView {
        if (!url.isNullOrEmpty()) {
            webView.loadUrl(url)
            originalUrl = url
        }
        return webView
    }

    fun reload() {
        webView.reload()
    }

    /**
     * 截图
     */
    fun snapshotWholePage(callback: (Bitmap) -> Unit) {
        val maxBitmapSize = 10f * 1024 * 1024 //图片 小于10M
        var wholeWidth = webView.computeHorizontalScrollRange()
        var wholeHeight = webView.computeVerticalScrollRange()
        if (wholeWidth == 0 || wholeHeight == 0 || webView.x5WebViewExtension == null) return
        var x5Bitmap = Bitmap.createBitmap(wholeWidth, wholeHeight, Bitmap.Config.ARGB_8888)
        val bitmapSize = x5Bitmap.byteCount
        if (bitmapSize > maxBitmapSize) {
            val scale = sqrt(maxBitmapSize / bitmapSize)
            wholeWidth *= scale.toInt()
            wholeHeight *= scale.toInt()
            x5Bitmap = Bitmap.createBitmap(wholeWidth, wholeHeight, Bitmap.Config.ARGB_8888)
        }
        val x5Canvas = Canvas(x5Bitmap).apply {
            scale(
                wholeWidth.toFloat() / webView.contentWidth,
                wholeHeight.toFloat() / webView.contentHeight
            )
        }
        webView.x5WebViewExtension.snapshotWholePage(x5Canvas, false, false) {
            callback(x5Bitmap)
        }
    }

    fun onResume() {
        webView.onResume()
    }

    fun onPause() {
        webView.onPause()
    }

    fun onDestroyView() {
        onPageChangedListener = null
        Manager.recycle(webView)
    }

    private fun initWebViewClient() {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (view == null || request == null || request.url == null) return false
                val scheme = request.url.scheme ?: return false
                if (scheme.equals(SCHEME_HTTP, true) ||
                    scheme.equals(SCHEME_HTTPS, true)
                ) return false
                try {
                    if (overrideUrlLoadingInterceptor?.intercept(request.url) != true) {
                        view.context.startActivity(Intent(Intent.ACTION_VIEW, request.url))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                return true
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (view == null || request == null)
                    return super.shouldInterceptRequest(view, request)
                val context = view.context
                return when {
                    canAssetsResource(request) -> assetResourceRequest(context, request)
                    canCacheResource(request) -> cacheResourceRequest(context, request)
                    else -> super.shouldInterceptRequest(view, request)
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, bitmap: Bitmap?) {
                super.onPageStarted(view, url, bitmap)
                onPageChangedListener?.onPageStared(view, url, bitmap)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                onPageChangedListener?.onPageFinished(view, url)
            }
        }
    }

    private fun initWebChromeClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, progress: Int) {
                super.onProgressChanged(view, progress)
                onPageChangedListener?.onProgressChanged(view, progress)
            }


        }
        webView.setDownloadListener { url, _, _, _, _ ->
            try {
                //直接让浏览器处理这个下载任务，如果外部有需求，可以通过回调外抛自定义实现
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                intent.addCategory(Intent.CATEGORY_BROWSABLE)
                webView.context.startActivity(intent)
            } catch (e: Exception) {

            }
        }
    }

    private fun canAssetsResource(webRequest: WebResourceRequest): Boolean {
        val url = webRequest.url.toString()
        return url.startsWith("file:///android_asset/")
    }

    private fun canCacheResource(webRequest: WebResourceRequest): Boolean {
        val url = webRequest.url.toString()
        val extension = getExtensionFromUrl(url)
        return extension == "ico" || extension == "bmp" || extension == "gif"
                || extension == "jpeg" || extension == "jpg" || extension == "png"
                || extension == "svg" || extension == "webp" || extension == "css"
                || extension == "js" || extension == "json" || extension == "eot"
                || extension == "otf" || extension == "ttf" || extension == "woff"
    }

    private fun assetResourceRequest(
        context: Context,
        webRequest: WebResourceRequest
    ): WebResourceResponse? = try {
        val url = webRequest.url.toString()
        val filenameIndex = url.lastIndexOf("/") + 1
        val filename = url.substring(filenameIndex)
        val suffixIndex = url.lastIndexOf(".")
        val suffix = url.substring(suffixIndex + 1)
        WebResourceResponse().apply {
            mimeType = getMimeTypeFromUrl(url)
            encoding = "UTF-8"
            data = context.assets.open("$suffix/$filename")
            responseHeaders = mapOf("access-control-allow-origin" to "*")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    /**
     * todo test
     */
    private fun cacheResourceRequest(
        context: Context,
        webRequest: WebResourceRequest
    ): WebResourceResponse? = try {
        val url = webRequest.url.toString()
        val cachePath = File(context.cacheDir, "web_cache").also {
            it.mkdir()
        }.absolutePath
        val fileName = url.encodeUtf8().md5().hex()
        val filePathName = cachePath + File.separator + fileName
        val file = File(filePathName)
        if (!file.exists() || !file.isFile) {
            //tips:是否会阻碍主线程
            runBlocking {
                simpleDownload(
                    cachePath,
                    fileName,
                    url,
                    retryTimes = 2,
                    headers = webRequest.requestHeaders
                )
            }
        }
        if (file.exists() && file.isFile) {
            WebResourceResponse().apply {
                mimeType = getMimeTypeFromUrl(url)
                encoding = "UTF-8"
                data = file.inputStream()
                responseHeaders = mapOf("access-control-allow-origin" to "*")
            }
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }


    private fun getMimeTypeFromUrl(url: String): String = try {
        val extension = getExtensionFromUrl(url)
        if (extension.isNotBlank() && extension.equals("null", true)) {
            if (extension == "json") {
                "application/json"
            } else {
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: "*/*"
            }
        } else {
            ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        "*/*"
    }

    private fun getExtensionFromUrl(url: String): String = try {
        if (url.isNotBlank() && url.equals("null", true)) {
            MimeTypeMap.getFileExtensionFromUrl(url)
        } else {
            ""
        }
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }


    fun setOnPageChangedListener(listener: OnPageChangedListener): WebViewHelper {
        this.onPageChangedListener = listener
        return this
    }

    /**
     * 处理非http请求，返回false就走隐式intent让系统处理
     */
    fun setOverrideUrlLoadingInterceptor(interceptor: OverrideUrlLoadingInterceptor): WebViewHelper {
        this.overrideUrlLoadingInterceptor = interceptor
        return this
    }

    abstract class OnPageChangedListener {
        fun onPageStared(view: WebView?, url: String?, favicon: Bitmap?) {}
        fun onPageFinished(view: WebView?, url: String?) {}
        fun onProgressChanged(view: WebView?, progress: Int) {}
    }

    /**
     * 非http请求
     * 例如跳转某个activity之类的自定义scheme
     */
    interface OverrideUrlLoadingInterceptor {
        /**
         * @return 是否拦截。不拦截就返回false，默认走隐式intent让系统处理
         */
        fun intercept(uri: Uri): Boolean
    }

    companion object {
        const val SCHEME_HTTP = "http"
        const val SCHEME_HTTPS = "https"
    }

    /**
     * 复用回收webView
     */
    object Manager {
        private val webViewCache = CopyOnWriteArrayList<WebView>()


        private fun create(context: Context, javaScriptEnabled: Boolean = false): WebView =
            WebView(context).apply {
                setBackgroundColor(Color.TRANSPARENT)
                view.setBackgroundColor(Color.TRANSPARENT)
                overScrollMode = WebView.OVER_SCROLL_NEVER
                view.overScrollMode = WebView.OVER_SCROLL_NEVER
                settings.apply {
                    allowFileAccess = true // 文件访问
                    setAppCacheEnabled(true) //
                    cacheMode = WebSettings.LOAD_DEFAULT
                    domStorageEnabled = true // 允许写入缓存
                    setGeolocationEnabled(true) // 允许请求定位
                    //注释说会有xss注入风险，但绝大部分都需要允许的,google上架会检查这个，置为false就好
                    this.javaScriptEnabled = javaScriptEnabled
                    loadWithOverviewMode = true //适应屏幕
                    setSupportZoom(true) //手势缩放
                    displayZoomControls = false
                    useWideViewPort = true //像素对齐
                    mixedContentMode =
                        android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW //允许加载其他源内容，即使是不安全的
                }
                CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                settingsExtension?.apply {
                    setContentCacheEnable(true)
                    setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY)
                }
            }

        /**
         * 传applicationContext，完成初始化，后边可以替换
         */
        fun prepare(context: Context) {
            if (webViewCache.isEmpty()) {
                Looper.myQueue().addIdleHandler {
                    webViewCache.add(create(MutableContextWrapper(context)))
                    false
                }
            }
        }

        fun obtain(context: Context): WebView {
            if (webViewCache.isEmpty()) {
                webViewCache.add(create(MutableContextWrapper(context)))
            }
            val webView = webViewCache.removeFirst()
            val contextWrapper = webView.context as MutableContextWrapper
            contextWrapper.baseContext = context
            webView.clearHistory()
            webView.resumeTimers()
            return webView
        }

        /**
         * 页面销毁时调用这个
         */
        fun recycle(webView: WebView) {
            try {
                webView.apply {
                    stopLoading()
                    loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
                    clearHistory()
                    pauseTimers()
                    webChromeClient = null
                    webViewClient = null
                    (webView.parent as? ViewGroup)?.removeView(this)
                    //替换上下文，防止泄露
                    (context as? MutableContextWrapper)?.baseContext = context.applicationContext
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (!webViewCache.contains(webView)) {
                    webViewCache.add(webView)
                }
            }
        }

        /**
         * 应用退出时调这个
         */
        fun clear() {
            webViewCache.forEach {
                try {
                    it.removeAllViews()
                    it.destroy()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            webViewCache.clear()
        }
    }


    /**
     * Application使用初始化x5
     */
    object X5Starter {
        /**
         * 是否是x5内核
         */
        internal var isX5 = false


        val usingX5: Boolean get() = isX5

        private var initialized = false

        /**
         * @param downloadWithoutWifi 非wifi情况下是否允许下载x5内核，大概40-50MB大小
         * @param forceSysWebView 是否强制使用系统内核，有些模拟器要支持这个内核需要关硬
         *                        件加速，那肯定不行，不如直接用系统内核
         */
        fun init(
            appContext: Context,
            downloadWithoutWifi: Boolean = true,
            forceSysWebView: Boolean = appContext.isSimulator()
        ) {
            if (initialized) return
            QbSdk.setDownloadWithoutWifi(downloadWithoutWifi)
            QbSdk.initTbsSettings(
                mapOf(
                    TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER to true,
                    TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE to true
                )
            )
            if (forceSysWebView) {
                QbSdk.forceSysWebView()
            }
            //关闭敏感权限的请求，有写手机会因为x5初始化的时候获取敏感权限而弹出权限弹窗，例如vivo
//            QbSdk.disableSensitiveApi() //据官方说明，新版不再需要这个
//            QbSdk.setTbsLogClient(object : TbsLogClient(appContext) {
//                override fun writeLog(log: String?) {
//                    super.writeLog(log)
//                    //x5 日志
//                }
//            })
            QbSdk.initX5Environment(appContext, object : QbSdk.PreInitCallback {
                override fun onCoreInitFinished() {
                    //内核初始化完成，可能为系统内核，也可能是x5内核

                }

                /**
                 * 预初始化结束
                 * 由于X5内核体积较大，需要依赖网络动态下发，所以当内核不存在的时候会优先使用系统内核代替
                 * @param isX5 如上没有下载完前就是false
                 */
                override fun onViewInitFinished(isX5: Boolean) {
                    this@X5Starter.isX5 = isX5
                }
            })
            initialized = true
        }
    }

}