@file:Suppress("unused")

package com.ly.utils.ui

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

val displayMetrics: DisplayMetrics
    get() = Resources.getSystem().displayMetrics


fun dp2pxf(value: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, value,
    displayMetrics
)

fun dp2px(value: Float): Int = (dp2pxf(value) + 0.5f).toInt()


fun sp2pxf(value: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_SP,
    value,
    displayMetrics
)

fun sp2px(value: Float): Int = (sp2pxf(value) + 0.5f).toInt()

fun pt2pxf(value: Float): Float = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_PT,
    value,
    displayMetrics
)

fun pt2px(value: Float): Int = (pt2pxf(value) + 0.5f).toInt()
