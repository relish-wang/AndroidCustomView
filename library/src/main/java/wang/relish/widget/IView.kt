package wang.relish.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import kotlin.math.pow

/**
 * @author wangxin
 * @since 20191030
 */
abstract class IView<Drawable : IDrawable<Data>, Data>(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    protected var width = -1f

    protected var mDrawable: Drawable? = null

    /**
     * View高度占View宽度的比例
     */
    protected abstract fun totalHeightRatio(): Float

    /**
     * 新建自定义Drawable
     */
    protected abstract fun newDrawable(data: Data): Drawable

    /**
     * 更新数据
     */
    fun update(data: Data) {
        if (mDrawable != null) {
            mDrawable!!.update(data)
        } else {
            mDrawable = newDrawable(data)
        }
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        width = widthSize.toFloat()
        val height = (width * totalHeightRatio()).toInt()
        val measureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode)
        setMeasuredDimension(measureSpec, height)
    }

    override fun onDraw(canvas: Canvas) {
        if (mDrawable == null) return
        if (width - 1f <= 10.0.pow(-6.0)) return
        mDrawable!!.draw(canvas, width)
    }
}
