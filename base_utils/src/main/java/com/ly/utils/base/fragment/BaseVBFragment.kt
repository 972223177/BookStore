package com.ly.utils.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST", "unused")
abstract class BaseVBFragment<VB : ViewBinding> : BaseFragment(0) {
    protected var mBinding: VB? = null
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<*>
        val binding = if (mRootView == null) {
            clazz.getDeclaredMethod(
                "inflate", LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, layoutInflater, container, false) as VB
        } else {
            (mRootView?.parent as? ViewGroup)?.removeView(mRootView)
            clazz.getDeclaredMethod("bind", View::class.java).invoke(null, mRootView) as VB
        }
        //如果改了base中rootView = null 的时机就得改下这里
        binding.apply {
            mBinding = this
            if (mRootView == null) {
                mRootView = root
                initView(root)
            }
        }
        return mRootView
    }


    override fun onDestroyView() {
        if (!removeRootWhenDestroy()) {
            mBinding = null
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        if (removeRootWhenDestroy()) {
            mBinding = null
        }
        super.onDestroy()
    }

    protected inline fun binding(block: VB.() -> Unit) {
        mBinding?.block()
    }

}