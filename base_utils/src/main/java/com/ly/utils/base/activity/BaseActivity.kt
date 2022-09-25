package com.ly.utils.base.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.InflateException
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar

/**
 * 透明activity注意下横竖屏切换的问题
 */
@Suppress("unused")
abstract class BaseActivity : AppCompatActivity() {


    override fun attachBaseContext(newBase: Context?) {
        if (isFixedFontScale() && newBase != null) {
            val res = newBase.resources
            val metrics = res.displayMetrics
            val config = Configuration().apply {
                setToDefaults()
                densityDpi = metrics.densityDpi
            }
            super.attachBaseContext(newBase.createConfigurationContext(config))
            return
        }
        super.attachBaseContext(newBase)
    }

    /**
     * [savedInstanceState]不是空时，注意下fragmentManager里的fragment
     * 避免重复添加
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (initArgs(intent.extras, savedInstanceState)) {
                initContentView()
            } else {
                Log.d(javaClass.simpleName ?: "", "params error when start this activity")
                finish()
                return
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is InflateException) throw e
        }
        initView()
        initObserver()
    }

    @LayoutRes
    abstract fun layoutRes(): Int

    /**
     * 检查进入此activity的参数，如果返回false就直接finish当前activity，且不会做initContentView操作
     * @param bundle intent.extra
     * @param savedInstanceState savedInstanceState
     */
    protected open fun initArgs(bundle: Bundle?, savedInstanceState: Bundle?): Boolean = true

    protected open fun initView() {}

    protected open fun initObserver() {}

    protected open fun initContentView() {
        val layoutRes = layoutRes()
        if (layoutRes != 0) {
            setContentView(layoutRes())
        }
    }

    /**
     * 是否固定字体缩放的倍率。
     * true就不会跟随系统字体缩放，也不用通过把sp改为dp避免这种情况，
     */
    protected open fun isFixedFontScale(): Boolean = true

    protected fun initImmersionBar(
        topView: View,
        statusBarDarkFont: Boolean = true,
        navColor: Int = android.R.color.white,
        autoNavigationBarDarkModeEnable: Boolean = true,
        keyboardEnable: Boolean = true
    ) {
        ImmersionBar.with(this)
            .titleBar(topView)
            .statusBarDarkFont(statusBarDarkFont)
            .navigationBarColor(navColor)
            .autoNavigationBarDarkModeEnable(autoNavigationBarDarkModeEnable)
            .keyboardEnable(keyboardEnable)
            .init()
    }
}