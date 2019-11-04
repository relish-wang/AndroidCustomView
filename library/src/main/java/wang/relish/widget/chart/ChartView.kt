package wang.relish.widget.chart

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import wang.relish.widget.IView

/**
 * 曲线图
 * @author relish
 * @since 20191010
 */
class ChartView : IView<ChartDrawable, ChartData> {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    override fun totalHeightRatio(): Float {
        return ChartDrawable.TOTAL_HEIGHT
    }

    override fun newDrawable(data: ChartData): ChartDrawable {
        return ChartDrawable(data)
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
}
