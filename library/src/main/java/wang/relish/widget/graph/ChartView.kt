package wang.relish.widget.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 曲线图
 * @author relish
 * @since 20191010
 */
class ChartView : View {

    private var mDrawable: ChartDrawable? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private var width = -1f
    private var height = -1f
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        width = widthSize.toFloat()
        height = width * ChartDrawable.TOTAL_HEIGHT
        //noinspection UnnecessaryLocalVariable,SuspiciousNameCombination for better reading
        val measureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode)
        setMeasuredDimension(measureSpec, height.toInt())
    }

    override fun onDraw(canvas: Canvas) {
        if (mDrawable == null) return
        if (width == -1f) return
        mDrawable!!.draw(canvas, width)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (mDrawable == null) return super.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        return mDrawable!!.onTouchEvent(event, object : Consumer {
            override fun invalidate() {
                this@ChartView.invalidate()
            }

            override fun accept(event: MotionEvent): Boolean {
                return superOnTouchEvent(event)
            }
        })
    }

    @Suppress("RedundantOverride")
    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun superOnTouchEvent(it: MotionEvent): Boolean {
        return super.onTouchEvent(it)
    }

    interface Consumer {
        fun accept(event: MotionEvent): Boolean
        fun invalidate()
    }

    /**
     * 更新数据
     */
    fun update(data: ChartData) {
        if (mDrawable != null) {
            mDrawable!!.updateValue(data)
        } else {
            mDrawable = ChartDrawable(data)
        }
        invalidate()
    }
}
