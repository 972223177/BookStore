package com.ly.utils.ui.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.text.InputFilter
import android.util.AttributeSet
import android.view.InflateException
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import com.ly.utils.R
import com.ly.utils.ui.dp2pxf
import com.ly.utils.ui.sp2pxf

/**
 * https://juejin.cn/post/6844904206118174733
 */
@Suppress("unused")
class SplitEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {
    //画笔
    private val mRectFConnect = RectF()
    private val mRectFSingleBox = RectF()
    private val mPaintDivisionLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintContent = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintBorder = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mPaintUnderline = Paint(Paint.ANTI_ALIAS_FLAG)

    //边框大小
    private var mBorderSize = 0f

    //边框颜色
    private var mBorderColor = 0

    //圆角大小
    private var mCornerSize = 0f

    //分割线大小
    private var mDivisionLineSize = 0f

    //分割线颜色
    private var mDivisionColor = 0

    //圆形密码的半径大小
    private var mCircleRadius = 0f

    //密码框长度
    private var mContentNumber = 0

    //密码显示模式
    private var mContentShowMode = 0

    //单框和下划线输入样式下,每个输入框的间距
    private var mSpaceSize = 0f

    //输入框样式
    private var mInputBoxStyle = 0

    //字体大小
    private var mTextSize = 0f

    //字体颜色
    private var mTextColor = 0

    //每个输入框是否是正方形标识
    private var mInputBoxSquare = false
    private var inputListener: OnInputListener? = null
    private val mPaintCursor = Paint(Paint.ANTI_ALIAS_FLAG)
    private val cursorRunnable = CursorRunnable()

    private var mCursorColor = 0//光标颜色
    private var mCursorWidth = 0f//光标宽度
    private var mCursorHeight = 0//光标高度
    private var mCursorDuration = 0//光标闪烁时长

    private var mUnderlineFocusColor = 0//下划线输入样式下,输入框获取焦点时下划线颜色
    private var mUnderlineNormalColor = 0//下划线输入样式下,下划线颜色
    private var mLineWidth = 0f//线段宽度

    /**
     * 计算3种样式下,相应每个字符item的宽度
     */
    private val contentItemWidth: Float
        get() {
            val viewWidth = width
            //计算每个密码字符所占的宽度,每种输入框样式下,每个字符item所占宽度也不一样
            //单个输入框样式：宽度-间距宽度(字符数-1)*每个间距宽度-每个输入框的左右边框宽度
            //下划线样式：宽度-间距宽度(字符数-1)*每个间距宽度
            //矩形输入框样式：宽度-左右两边框宽度-分割线宽度(字符数-1)*每个分割线宽度
            return viewWidth.getContentItemWidth()
        }


    /**
     * 密码显示模式
     */
    var contentShowMode: Int
        get() = mContentShowMode
        set(mode) {
            if (mode != CONTENT_SHOW_MODE_PASSWORD && mode != CONTENT_SHOW_MODE_TEXT) {
                throw IllegalArgumentException(
                    "the value of the parameter must be one of" +
                            "{1:EDIT_SHOW_MODE_PASSWORD} or " +
                            "{2:EDIT_SHOW_MODE_TEXT}"
                )
            }
            mContentShowMode = mode
            invalidate()
        }


    /**
     * 输入框样式
     */
    var inputBoxStyle: Int
        get() = mInputBoxStyle
        set(inputBoxStyle) {
            if ((inputBoxStyle != INPUT_BOX_STYLE_CONNECT
                        && inputBoxStyle != INPUT_BOX_STYLE_SINGLE
                        && inputBoxStyle != INPUT_BOX_STYLE_UNDERLINE
                        && inputBoxStyle != INPUT_BOX_STYLE_MIDDLE_LINE)
            ) {
                throw IllegalArgumentException(
                    ("the value of the parameter must be one of" +
                            "{1:INPUT_BOX_STYLE_CONNECT}, " +
                            "{2:INPUT_BOX_STYLE_SINGLE} or " +
                            "{3:INPUT_BOX_STYLE_UNDERLINE}")
                )
            }
            mInputBoxStyle = inputBoxStyle
            // 这里没有调用invalidate因为会存在问题
            // invalidate会重绘,但是不会去重新测量,当输入框样式切换的之后,item的宽度其实是有变化的,所以此时需要重新测量
            // requestLayout,调用onMeasure和onLayout,不一定会调用onDraw,当view的l,t,r,b发生改变时会调用onDraw
            //invalidate();
            requestLayout()
        }

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.SplitEditText)
        mBorderSize =
            array.getDimension(R.styleable.SplitEditText_sev_borderSize, dp2pxf(1f))
        mBorderColor =
            array.getColor(R.styleable.SplitEditText_sev_borderColor, Color.BLACK)
        mCornerSize = array.getDimension(R.styleable.SplitEditText_sev_corner_size, 0f)
        mDivisionLineSize = array.getDimension(
            R.styleable.SplitEditText_sev_divisionLineSize,
            dp2pxf(1f)
        )
        mDivisionColor = array.getColor(
            R.styleable.SplitEditText_sev_divisionLineColor,
            Color.BLACK
        )
        mCircleRadius =
            array.getDimension(R.styleable.SplitEditText_sev_circleRadius, dp2pxf(5f))
        mContentNumber = array.getInt(R.styleable.SplitEditText_sev_contentNumber, 6)
        mContentShowMode = array.getInteger(
            R.styleable.SplitEditText_sev_contentShowMode,
            CONTENT_SHOW_MODE_PASSWORD
        )
        mInputBoxStyle = array.getInteger(
            R.styleable.SplitEditText_sev_inputBoxStyle,
            INPUT_BOX_STYLE_CONNECT
        )
        mSpaceSize =
            array.getDimension(R.styleable.SplitEditText_sev_spaceSize, dp2pxf(10f))
        mTextSize =
            array.getDimension(R.styleable.SplitEditText_android_textSize, sp2pxf(16f))
        mTextColor =
            array.getColor(R.styleable.SplitEditText_android_textColor, Color.BLACK)
        mInputBoxSquare =
            array.getBoolean(R.styleable.SplitEditText_sev_inputBoxSquare, true)
        mCursorColor =
            array.getColor(R.styleable.SplitEditText_sev_cursorColor, Color.BLACK)
        mCursorDuration =
            array.getInt(R.styleable.SplitEditText_sev_cursorDuration, 500)
        mCursorWidth =
            array.getDimension(R.styleable.SplitEditText_sev_cursorWidth, dp2pxf(2f))
        mCursorHeight =
            array.getDimension(R.styleable.SplitEditText_sev_cursorHeight, 0f).toInt()
        mLineWidth =
            array.getDimension(R.styleable.SplitEditText_sev_lineWidth, dp2pxf(0f))
        mUnderlineNormalColor = array.getInt(
            R.styleable.SplitEditText_sev_underlineNormalColor,
            Color.BLACK
        )
        mUnderlineFocusColor =
            array.getInt(R.styleable.SplitEditText_sev_underlineFocusColor, 0)
        array.recycle()
        init()
    }


    @SuppressLint("DiscouragedPrivateApi")
    private fun init() {
        mPaintBorder.style = Paint.Style.STROKE
        mPaintBorder.strokeWidth = mBorderSize
        mPaintBorder.color = mBorderColor

        mPaintDivisionLine.style = Paint.Style.STROKE
        mPaintDivisionLine.strokeWidth = mDivisionLineSize
        mPaintDivisionLine.color = mDivisionColor

        mPaintContent.textSize = mTextSize

        mPaintCursor.strokeWidth = mCursorWidth
        mPaintCursor.color = mCursorColor

        mPaintUnderline.strokeWidth = mBorderSize
        mPaintUnderline.color = mUnderlineNormalColor

        //设置单行输入
        setSingleLine()

        //若构造方法中没有写成android.R.attr.editTextStyle的属性,应该需要设置该属性,EditText默认是获取焦点的
        isFocusableInTouchMode = true

        //取消默认的光标
        //这里默认不设置该属性,不然长按粘贴有问题(一开始长按不能粘贴,输入内容就可以长按粘贴)
        //setCursorVisible(false);

        //设置透明光标,若是直接不显示光标的话,长按粘贴会没效果
        /*try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //设置光标的TextSelectHandle
        //这里判断版本,10.0以及以上直接通过方法调用,以下通过反射设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            setTextSelectHandle(android.R.color.transparent)
        } else {
            //通过反射改变光标TextSelectHandle的样式
            try {
                val f = TextView::class.java.getDeclaredField("mTextSelectHandleRes")
                f.isAccessible = true
                f.set(this, android.R.color.transparent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        //设置InputFilter,设置输入的最大字符长度为设置的长度
        filters = arrayOf(InputFilter.LengthFilter(mContentNumber))
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        postDelayed(cursorRunnable, mCursorDuration.toLong())
    }

    override fun onDetachedFromWindow() {
        removeCallbacks(cursorRunnable)
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mInputBoxSquare) {
            val width = MeasureSpec.getSize(widthMeasureSpec)
            //计算view高度,使view高度和每个item的宽度相等,确保每个item是一个正方形
            val itemWidth = getContentItemWidthOnMeasure(width)
            when (mInputBoxStyle) {
                INPUT_BOX_STYLE_UNDERLINE, INPUT_BOX_STYLE_MIDDLE_LINE -> setMeasuredDimension(
                    width,
                    (itemWidth + mBorderSize).toInt()
                )
                INPUT_BOX_STYLE_SINGLE, INPUT_BOX_STYLE_CONNECT -> setMeasuredDimension(
                    width,
                    (itemWidth + mBorderSize * 2).toInt()
                )
                else -> setMeasuredDimension(width, (itemWidth + mBorderSize * 2).toInt())
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        //绘制输入框
        when (mInputBoxStyle) {
            INPUT_BOX_STYLE_SINGLE -> drawSingleStyle(canvas)
            INPUT_BOX_STYLE_UNDERLINE -> drawUnderlineStyle(canvas)
            INPUT_BOX_STYLE_MIDDLE_LINE -> drawMiddleLineStyle(canvas)
            INPUT_BOX_STYLE_CONNECT -> drawConnectStyle(canvas)
            else -> drawConnectStyle(canvas)
        }
        //绘制输入框内容
        drawContent(canvas)
        //绘制光标
        drawCursor(canvas)
    }

    @SuppressLint("MissingSuperCall")
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        //禁止用户通过触摸移动光标，这里的光标是隐藏的那个,不是自定义绘制的那个光标
        setSelection(length())
    }

    /**
     * 根据输入内容显示模式,绘制内容是圆心还是明文的text
     */
    private fun drawContent(canvas: Canvas) {
        val cy = height / 2
        val password = text?.toString()?.trim() ?: ""
        if (mContentShowMode == CONTENT_SHOW_MODE_PASSWORD) {
            mPaintContent.color = Color.BLACK
            for (i in password.indices) {
                val startX = getDrawContentStartX(i)
                canvas.drawCircle(startX, cy.toFloat(), mCircleRadius, mPaintContent)
            }
        } else {
            mPaintContent.color = mTextColor
            //计算baseline
            val baselineText = getTextBaseline(mPaintContent, cy.toFloat())
            for (i in password.indices) {
                val startX = getDrawContentStartX(i)
                //计算文字宽度
                val text = (password[i]).toString()
                val textWidth = mPaintContent.measureText(text)
                //绘制文字x应该还需要减去文字宽度的一半
                canvas.drawText(text, startX - textWidth / 2, baselineText, mPaintContent)
            }
        }
    }

    /**
     * 绘制光标
     * 光标只有一个,所以不需要根据循环来绘制,只需绘制第N个就行
     * 即:
     * 当输入内容长度为0,光标在第0个位置
     * 当输入内容长度为1,光标应在第1个位置
     * ...
     * 所以光标所在位置为输入内容的长度
     * 这里光标的长度默认就是 height/2
     */
    private fun drawCursor(canvas: Canvas) {
        if (mCursorHeight > height) {
            throw InflateException("cursor height must smaller than view height")
        }
        val content = text?.toString()?.trim() ?: ""
        val startX = getDrawContentStartX(content.length)
        //如果设置得有光标高度,那么startY = (高度-光标高度)/2+边框宽度
        if (mCursorHeight == 0) {
            mCursorHeight = height / 2
        }
        val sy = (height - mCursorHeight) / 2
        val startY = sy + mBorderSize
        val stopY = height.toFloat() - sy.toFloat() - mBorderSize

        //此时的绘制光标竖直线,startX = stopX
        canvas.drawLine(startX, startY, startX, stopY, mPaintCursor)
    }

    /**
     * 、
     * 计算三种输入框样式下绘制圆和文字的x坐标
     *
     * @param index 循环里面的下标 i
     */
    private fun getDrawContentStartX(index: Int): Float {
        val itemWidth = contentItemWidth
        return when (mInputBoxStyle) {
            INPUT_BOX_STYLE_SINGLE ->
                //单个输入框样式下的startX
                //即 itemWidth/2 + i*itemWidth + i*每一个间距宽度 + 前面所有的左右边框
                //   i = 0,左侧1个边框
                //   i = 1,左侧3个边框(一个完整的item的左右边框+ 一个左侧边框)
                //   i = ..., (2*i+1)*mBorderSize
                itemWidth / 2 + index * itemWidth + index * mSpaceSize + (2 * index + 1) * mBorderSize
            INPUT_BOX_STYLE_UNDERLINE, INPUT_BOX_STYLE_MIDDLE_LINE ->
                //下划线输入框样式下的startX
                //即 itemWidth/2 + i*itemWidth + i*每一个间距宽度
                itemWidth / 2 + index * mSpaceSize + index * itemWidth
            INPUT_BOX_STYLE_CONNECT -> itemWidth / 2 + index * itemWidth + index * mDivisionLineSize + mBorderSize
            //矩形输入框样式下的startX
            //即 itemWidth/2 + i*itemWidth + i*分割线宽度 + 左侧的一个边框宽度
            else -> itemWidth / 2 + index * itemWidth + index * mDivisionLineSize + mBorderSize
        }
    }


    /**
     * 绘制下划线输入框样式
     * 线条起点startX:每个字符所占宽度itemWidth + 每个字符item之间的间距mSpaceSize
     * 线条终点stopX:stopX与startX之间就是一个itemWidth的宽度
     */
    private fun drawUnderlineStyle(canvas: Canvas) {
        val content = text?.toString()?.trim() ?: ""
        for (i in 0 until mContentNumber) {
            //计算绘制下划线的startX
            val startX = i * contentItemWidth + i * mSpaceSize
            //stopX
            val stopX = contentItemWidth + startX
            //对于下划线这种样式,startY = stopY
            val startY = height - mBorderSize / 2
            //这里判断是否设置有输入框获取焦点时,下划线的颜色
            if (mUnderlineFocusColor != 0) {
                if (content.length >= i) {
                    mPaintUnderline.color = mUnderlineFocusColor
                } else {
                    mPaintUnderline.color = mUnderlineNormalColor
                }
            }
            canvas.drawLine(startX, startY, stopX, startY, mPaintUnderline)
        }
    }

    /**
     * 绘制中间线输入框样式
     * 线条起点startX:每个字符所占宽度itemWidth + 每个字符item之间的间距mSpaceSize
     * 线条终点stopX:stopX与startX之间就是一个itemWidth的宽度
     */
    private fun drawMiddleLineStyle(canvas: Canvas) {
        val content = text?.toString()?.trim() ?: ""
        for (i in 0 until mContentNumber) {
            //计算绘制下划线的startX
            val startX = i * contentItemWidth + i * mSpaceSize
            //stopX
            val stopX = contentItemWidth + startX
            //对于下划线这种样式,startY = stopY
            val startY = (height - mBorderSize) / 2
            // 这里判断是否设置有输入框获取焦点时,下划线的颜色
            mPaintUnderline.color = mUnderlineNormalColor
            val offset = (stopX - startX - mLineWidth) / 2
            if (content.length < i) {
                canvas.drawLine(startX + offset, startY, stopX - offset, startY, mPaintUnderline)
            }
        }
    }

    /**
     * 绘制单框输入模式
     * 这里计算left、right时有点饶,
     * 理解、计算时最好根据图形、参照drawConnectStyle()绘制带边框的矩形
     * left:绘制带边框的矩形等图形时,去掉边框的一半即 + mBorderSize / 2,同时加上每个字符item的间距 + i*mSpaceSize
     * 另外,每个字符item的宽度 + i*itemWidth
     * 最后,绘制时都是以整个view的宽度计算,绘制第N个时,都应该加上以前的边框宽度
     * 即第一个：i = 0 ,边框的宽度为0
     * 第二个：i = 1,边框的宽度 2*mBorderSize,左右两个的边框宽度
     * 以此...最后应该 + i*2*mBorderSize
     * 同理
     * right：去掉边框的一半： -mBorderSize/2,还应该加上前面一个item的宽度：+(i+1)*itemWidth
     * 同样,绘制时都是以整个view的宽度计算,绘制后面的,都应该加上前面的所有宽度
     * 即 间距：+i*mSpaceSize
     * 边框：(注意是计算整个view)
     * 第一个：i = 0,2个边框2*mBorderSize
     * 第二个：i = 1,4个边框,即 (1+1)*2*mBorderSize
     * 所以算上边框 +(i+1)*2*mBorderSize
     */
    private fun drawSingleStyle(canvas: Canvas) {
        for (i in 0 until mContentNumber) {
            mRectFSingleBox.setEmpty()
            val left =
                i * contentItemWidth + i * mSpaceSize + i.toFloat() * mBorderSize * 2f + mBorderSize / 2
            val right =
                i * mSpaceSize + (i + 1) * contentItemWidth + (i + 1).toFloat() * 2f * mBorderSize - mBorderSize / 2
            //为避免在onDraw里面创建RectF对象,这里使用rectF.set()方法
            mRectFSingleBox.set(left, mBorderSize / 2, right, height - mBorderSize / 2)
            canvas.drawRoundRect(mRectFSingleBox, mCornerSize, mCornerSize, mPaintBorder)
        }
    }

    /**
     * 绘制矩形外框
     * 在绘制圆角矩形的时候,应该减掉边框的宽度
     * 不然会有所偏差
     *
     *
     * 在绘制矩形以及其它图形的时候,矩形(图形)的边界是边框的中心,不是边框的边界
     * 所以在绘制带边框的图形的时候应该减去边框宽度的一半
     * https://blog.csdn.net/a512337862/article/details/74161988
     */
    private fun drawConnectStyle(canvas: Canvas) {
        //每次重新绘制时,先将rectF重置下
        mRectFConnect.setEmpty()
        //需要减去边框的一半
        mRectFConnect.set(
            mBorderSize / 2,
            mBorderSize / 2,
            width - mBorderSize / 2,
            height - mBorderSize / 2
        )
        canvas.drawRoundRect(mRectFConnect, mCornerSize, mCornerSize, mPaintBorder)
        //绘制分割线
        drawDivisionLine(canvas)
    }

    /**
     * 分割线条数为内容框数目-1
     * 这里startX应该要加上左侧边框的宽度
     * 应该还需要加上分割线的一半
     * 至于startY和stopY不是 mBorderSize/2 而是 mBorderSize
     * startX是计算整个宽度的,需要算上左侧的边框宽度,所以不是+mBorderSize/2 而是+mBorderSize
     * startY和stopY：分割线是紧挨着边框内部的,所以应该是mBorderSize,而不是mBorderSize/2
     */
    private fun drawDivisionLine(canvas: Canvas) {
        val stopY = height - mBorderSize
        for (i in 0 until mContentNumber - 1) {
            //对于分割线条,startX = stopX
            val startX =
                (i + 1) * contentItemWidth + i * mDivisionLineSize + mBorderSize + mDivisionLineSize / 2
            canvas.drawLine(startX, mBorderSize, startX, stopY, mPaintDivisionLine)
        }
    }

    private fun Int.getContentItemWidth(): Float {
        val tempWidth = when (mInputBoxStyle) {
            INPUT_BOX_STYLE_SINGLE -> this - (mContentNumber - 1) * mSpaceSize - 2f * mContentNumber * mBorderSize
            INPUT_BOX_STYLE_UNDERLINE, INPUT_BOX_STYLE_MIDDLE_LINE -> this - (mContentNumber - 1) * mSpaceSize
            INPUT_BOX_STYLE_CONNECT -> this - mDivisionLineSize * (mContentNumber - 1) - 2 * mBorderSize
            else -> this - mDivisionLineSize * (mContentNumber - 1) - 2 * mBorderSize
        }
        return tempWidth / mContentNumber
    }

    /**
     * 根据view的测量宽度,计算每个item的宽度
     *
     * @param measureWidth view的measure
     * @return onMeasure时的每个item宽度
     */
    private fun getContentItemWidthOnMeasure(measureWidth: Int): Float =
        measureWidth.getContentItemWidth()

    /**
     * 计算绘制文本的基线
     *
     * @param paint      绘制文字的画笔
     * @param halfHeight 高度的一半
     */
    private fun getTextBaseline(paint: Paint, halfHeight: Float): Float {
        val fontMetrics = paint.fontMetrics
        val dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom
        return halfHeight + dy
    }

    fun setOnInputListener(listener: OnInputListener) {
        this.inputListener = listener
    }

    /**
     * 通过复写onTextChanged来实现对输入的监听
     * 如果在onDraw里面监听text的输入长度来实现,会重复的调用该方法,就不妥当
     */
    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        val listener = inputListener ?: return
        val content = text?.toString()?.trim() ?: ""
        if (content.length == mContentNumber) {
            listener.onInputFinished(content)
        } else {
            listener.onInputChanged(content)
        }
    }


    /**
     * 光标Runnable
     * 通过Runnable每500ms执行重绘,每次runnable通过改变画笔的alpha值来使光标产生闪烁的效果
     */
    private inner class CursorRunnable : Runnable {

        override fun run() {
            //获取光标画笔的alpha值
            val alpha = mPaintCursor.alpha
            //设置光标画笔的alpha值
            mPaintCursor.alpha = if (alpha == 0) 255 else 0
            invalidate()
            postDelayed(this, mCursorDuration.toLong())
        }
    }

    companion object {
        //密码显示模式：隐藏密码,显示圆形
        const val CONTENT_SHOW_MODE_PASSWORD = 1

        //密码显示模式：显示密码
        const val CONTENT_SHOW_MODE_TEXT = 2

        //输入框相连的样式
        const val INPUT_BOX_STYLE_CONNECT = 1

        //单个的输入框样式
        const val INPUT_BOX_STYLE_SINGLE = 2

        //下划线输入框样式
        const val INPUT_BOX_STYLE_UNDERLINE = 3
        const val INPUT_BOX_STYLE_MIDDLE_LINE = 4
    }

}//这里没有写成默认的EditText属性样式android.R.attr.editTextStyle,这样会存在EditText默认的样式


abstract class OnInputListener {
    abstract fun onInputFinished(content: String)

    open fun onInputChanged(text: String) {}
}