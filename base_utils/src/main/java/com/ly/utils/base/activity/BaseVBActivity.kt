package com.ly.utils.base.activity

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST", "unused")
abstract class BaseVBActivity<VB : ViewBinding> : BaseActivity() {

    protected open var mBinding: VB? = null

    override fun layoutRes(): Int = 0

    override fun initContentView() {
        val vbClazz =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>
        val binding = vbClazz.getDeclaredMethod("inflate", LayoutInflater::class.java)
            .invoke(null, layoutInflater) as VB
        mBinding = binding
        setContentView(binding.root)
    }


    protected inline fun binding(block: VB.() -> Unit) {
        mBinding?.block()
    }


}