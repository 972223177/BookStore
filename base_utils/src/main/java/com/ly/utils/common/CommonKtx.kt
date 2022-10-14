@file:Suppress("unused")

package com.ly.utils.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.serialization.json.Json

val globalJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

inline fun <T> T?.ifNull(block: () -> T): T {
    return this ?: block()
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext as? Activity
    else -> null
}

/**
 * 直接调用show的时候可能会出现Exception:Can not perform this action after onSaveInstanceState
 * 看意思就知道是activity重建了，这种情况下show就会直接导致崩溃。
 * allowingStateLoss就跳过这个checkStateLoss检查，数据可以保留在activityViewModel中乃至SavedStateHandle中
 * 来间接规避这个数据丢失的问题
 */
fun DialogFragment.showAllowingStateLoss(fragmentManager: FragmentManager, tag: String?) {
    catch(true) {
        if (this.isAdded || (getTag() != null && fragmentManager.findFragmentByTag(tag) == null)) return
        val clazz = DialogFragment::class.java
        clazz.getDeclaredField("mDismissed").apply {
            isAccessible = true
            set(this@showAllowingStateLoss, false)
        }
        clazz.getDeclaredField("mShownByMe").apply {
            isAccessible = true
            set(this@showAllowingStateLoss, true)
        }
        fragmentManager.beginTransaction().apply {
            add(this@showAllowingStateLoss, tag)
            commitAllowingStateLoss()
        }
    }
}
