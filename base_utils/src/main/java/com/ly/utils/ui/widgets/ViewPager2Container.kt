package com.ly.utils.ui.widgets


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * 解决其他滚动view嵌套viewPager2滑动冲突问题。优先滚动viewPager2内的pager，如果到边界了，就将事件交友父容器
 */
@Suppress("unused")
class ViewPager2Container @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = -1
) : ConstraintLayout(context, attr, defStyle) {
    private var mViewPager2: ViewPager2? = null
    private var disallowParentInterceptDownEvent = true
    private var startX = 0
    private var startY = 0


    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (childView is ViewPager2) {
                mViewPager2 = childView
                break
            }
        }
        if (mViewPager2 == null) {
            throw IllegalStateException("viewPager2 not found")
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val doNotNeedIntercept =
            (mViewPager2?.isUserInputEnabled == false ||
                    (mViewPager2?.adapter != null &&
                            (mViewPager2?.adapter?.itemCount ?: 0) <= 1))
        if (doNotNeedIntercept) {
            return super.onInterceptTouchEvent(ev)
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev.x.toInt()
                startY = ev.y.toInt()
                parent.requestDisallowInterceptTouchEvent(!disallowParentInterceptDownEvent)
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = ev.x.toInt()
                val endY = ev.y.toInt()
                val disX = abs(endX - startX)
                val disY = abs(endY - startY)
                if (mViewPager2?.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    onHorizontalActionMove(endX, disX, disY)
                } else if (mViewPager2?.orientation == ViewPager2.ORIENTATION_VERTICAL) {
                    onVerticalActionMove(endY, disX, disY)
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->parent.requestDisallowInterceptTouchEvent(false)
            else -> {}
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun onHorizontalActionMove(endX: Int, disX: Int, disY: Int) {
        if (mViewPager2?.adapter == null) return
        if (disX > disY) {
            val currentItem = mViewPager2?.currentItem
            val itemCount = mViewPager2?.adapter?.itemCount ?: 0
            if (currentItem == 0 && endX - startX > 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(currentItem != itemCount - 1 || endX - startX >= 0)
            }
        } else if (disY > disX) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }

    private fun onVerticalActionMove(endY: Int, disX: Int, disY: Int) {
        if (mViewPager2?.adapter == null) return
        if (disY > disX) {
            val currentItem = mViewPager2?.currentItem
            val itemCount = mViewPager2?.adapter?.itemCount ?: 0
            if (currentItem == 0 && endY - startY > 0) {
                parent.requestDisallowInterceptTouchEvent(false)
            } else {
                parent.requestDisallowInterceptTouchEvent(currentItem != itemCount - 1 || endY - startY >= 0)
            }
        } else if (disX > disY) {
            parent.requestDisallowInterceptTouchEvent(false)
        }
    }


    fun disallowParentInterceptDownEvent(disallow: Boolean) {
        disallowParentInterceptDownEvent = disallow
    }
}