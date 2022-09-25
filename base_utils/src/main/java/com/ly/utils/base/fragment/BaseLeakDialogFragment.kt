package com.ly.utils.base.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Closeable
import java.lang.ref.WeakReference

/**
 * 解决DialogFragment和BottomSheetDialogFragment 由于message泄露的问题
 */
abstract class BaseLeakDialogFragment(@LayoutRes layoutRes: Int) : DialogFragment(layoutRes) {
    private var mCancelListener: DialogCancelListener? = null
    private var mDismissListener: DialogDismissListener? = null


    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val replaceSuccess = replaceDialogInterfaceBySuper()
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        if (!replaceSuccess) {
            replaceDialogInterface()
        }
        return inflater
    }

    private fun replaceDialogInterfaceBySuper(): Boolean = try {
        val superClazz = findSuperClazz(javaClass, DialogFragment::class.java)
            ?: throw IllegalStateException("${javaClass.simpleName} superClass is not DialogFragment")
        val dialogFragment = this@BaseLeakDialogFragment
        superClazz.getDeclaredField("mOnCancelListener").apply {
            isAccessible = true
            set(dialogFragment, DialogCancelListener(dialogFragment))
        }

        superClazz.getDeclaredField("mOnDismissListener").apply {
            isAccessible = true
            set(dialogFragment, DialogDismissListener(dialogFragment))
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    private fun replaceDialogInterface() {
        if (mCancelListener == null) {
            mCancelListener = DialogCancelListener(this)
        }
        dialog?.setOnCancelListener(mCancelListener)
        if (mDismissListener == null) {
            mDismissListener = DialogDismissListener(this)
        }
        dialog?.setOnDismissListener(mDismissListener)
    }

    private fun findSuperClazz(target: Class<*>, need: Class<*>): Class<*>? {
        if (target.isAssignableFrom(need)) return target
        val targetSuperClazz = target.superclass ?: return null
        return findSuperClazz(targetSuperClazz, need)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCancelListener?.close()
        mDismissListener?.close()
        mCancelListener = null
        mDismissListener = null
    }

}

abstract class BaseLeakBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var mCancelListener: DialogCancelListener? = null
    private var mDismissListener: DialogDismissListener? = null


    override fun onGetLayoutInflater(savedInstanceState: Bundle?): LayoutInflater {
        val replaceSuccess = replaceDialogInterfaceBySuper()
        val inflater = super.onGetLayoutInflater(savedInstanceState)
        if (!replaceSuccess) {
            replaceDialogInterface()
        }
        return inflater
    }

    private fun replaceDialogInterfaceBySuper(): Boolean = try {
        val superClazz = findSuperClazz(javaClass, DialogFragment::class.java)
            ?: throw IllegalStateException("${javaClass.simpleName} superClass is not DialogFragment")
        val dialogFragment = this@BaseLeakBottomSheetDialogFragment
        superClazz.getDeclaredField("mOnCancelListener").apply {
            isAccessible = true
            set(dialogFragment, DialogCancelListener(dialogFragment))
        }

        superClazz.getDeclaredField("mOnDismissListener").apply {
            isAccessible = true
            set(dialogFragment, DialogDismissListener(dialogFragment))
        }
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    private fun replaceDialogInterface() {
        if (mCancelListener == null) {
            mCancelListener = DialogCancelListener(this)
        }
        dialog?.setOnCancelListener(mCancelListener)
        if (mDismissListener == null) {
            mDismissListener = DialogDismissListener(this)
        }
        dialog?.setOnDismissListener(mDismissListener)
    }

    private fun findSuperClazz(target: Class<*>, need: Class<*>): Class<*>? {
        if (target.isAssignableFrom(need)) return target
        val targetSuperClazz = target.superclass ?: return null
        return findSuperClazz(targetSuperClazz, need)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCancelListener?.close()
        mDismissListener?.close()
        mCancelListener = null
        mDismissListener = null
    }

}


private class DialogDismissListener(dialogFragment: DialogFragment) :
    DialogInterface.OnDismissListener, Closeable {
    private val reference = WeakReference(dialogFragment)

    override fun onDismiss(dialogInterface: DialogInterface) {
        reference.get()?.onDismiss(dialogInterface)
    }

    override fun close() {
        reference.clear()
    }
}


private class DialogCancelListener(dialogFragment: DialogFragment) :
    DialogInterface.OnCancelListener, Closeable {
    private val reference = WeakReference(dialogFragment)
    override fun onCancel(dialogInterface: DialogInterface) {
        reference.get()?.onCancel(dialogInterface)
    }

    override fun close() {
        reference.clear()
    }

}