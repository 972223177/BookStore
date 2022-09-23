package com.ly.utils.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.gyf.immersionbar.ImmersionBar
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST", "unused")
abstract class BaseVBBottomSheetDialogFragment<VB : ViewBinding> :
    BaseLeakBottomSheetDialogFragment() {
    protected var mBinding: VB? = null
        private set

    protected open var mRootView: View? = null

    protected open var mIsFirstInit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onStart() {
        super.onStart()
        view?.let {
            val parent = it.parent as View
            val behavior = BottomSheetBehavior.from(parent)
            it.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            behavior.peekHeight = it.measuredHeight
            parent.layoutParams = (parent.layoutParams as CoordinatorLayout.LayoutParams).apply {
                gravity = Gravity.TOP and Gravity.CENTER_HORIZONTAL
            }
        }
    }

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
            val vbClazz =
                (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
            val binding = vbClazz.getDeclaredMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, layoutInflater, container, false) as VB
            mBinding = binding
            mRootView = binding.root
            initView(binding.root)
        } else {
            (mRootView?.parent as? ViewGroup)?.removeView(mRootView)
        }
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewObserver()
        if (mIsFirstInit) {
            mIsFirstInit = false
            initObserver()
            onFirstInit()
        }
        initData(savedInstanceState)
    }


    override fun onDestroy() {
        mRootView = null
        mIsFirstInit = true
        mBinding = null
        ImmersionBar.destroy(this)
        super.onDestroy()
    }

    protected inline fun binding(block: VB.() -> Unit) {
        mBinding?.block()
    }

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