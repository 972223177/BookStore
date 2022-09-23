package com.ly.utils.ui.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ly.utils.ui.WebViewHelper

class FastWebView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = -1
) : FrameLayout(context, attributeSet, defStyle) {
    private var webViewHelper: WebViewHelper? = null

    val helper: WebViewHelper? get() = webViewHelper

    init {
        val webView = WebViewHelper.Manager.obtain(context)
        webViewHelper = WebViewHelper(webView)
        addView(webView)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        webViewHelper?.onDestroyView()
        webViewHelper = null
    }
}