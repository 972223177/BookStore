package com.ly.utils.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar

@Suppress("unused")
abstract class BaseFragment(@LayoutRes layoutId: Int) : Fragment(layoutId) {
    protected open var mRootView: View? = null

    protected open var mIsFirstInit = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            initArgs(it)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            mRootView = super.onCreateView(inflater, container, savedInstanceState)?.also {
                initView(it)
            }
        } else {
            (mRootView?.parent as? ViewGroup)?.removeView(mRootView)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mIsFirstInit) {
            initObserver()
            mIsFirstInit = false
            onFirstInit()
        }
        initViewObserver()
        initData(savedInstanceState)
    }


    override fun onDestroyView() {
        if (!removeRootWhenDestroy()) {
            mRootView = null
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (removeRootWhenDestroy()) {
            mRootView = null
        }
        super.onDestroy()
        mIsFirstInit = true
    }

    /**
     * 是否将RootView销毁的时机延后。
     * navigation会重走onCreateView导致内容显示出错的问题,主要是BaseVBFragment
     */
    protected open fun removeRootWhenDestroy(): Boolean = false

    protected open fun initArgs(bundle: Bundle) {}

    protected open fun initData(savedInstanceState: Bundle?) {}

    protected open fun initView(view: View) {}

    protected open fun onFirstInit() {}

    protected open fun initObserver() {}

    protected open fun initViewObserver() {}

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