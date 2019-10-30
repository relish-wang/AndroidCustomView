package wang.relish.widget

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/**
 * @author wangxin
 * @since 20191030
 */
abstract class IDrawable<Data>(
    internal var mData: Data
) : Drawable() {

    internal var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    abstract fun draw(canvas: Canvas, w: Float)

    abstract fun update(data: Data)

    override fun draw(canvas: Canvas) {
        throw UnsupportedOperationException("please use #draw(Canvas canvas, float w)")
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    /**
     * Return the intrinsic width of the underlying drawable object.  Returns
     * -1 if it has no intrinsic width, such as with a solid color.
     */
    override fun getIntrinsicWidth(): Int {
        return -1
    }

    /**
     * Return the intrinsic height of the underlying drawable object. Returns
     * -1 if it has no intrinsic height, such as with a solid color.
     */
    override fun getIntrinsicHeight(): Int {
        return -1
    }
}
