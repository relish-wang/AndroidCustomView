package wang.relish.widget.sample.sample.rich

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan

class TopAlignedImageSpan constructor(
    private val drawable: Drawable
) : DynamicDrawableSpan() {
    override fun getDrawable(): Drawable {
        return drawable.apply {
            // 确保 Drawable bounds 已设置
            if (bounds.isEmpty) setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        }
    }

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val drawable = drawable
        canvas.save()
        // 调整 Drawable 的 Y 坐标，使其顶部对齐文字顶部
        val transY = top.toFloat()
        canvas.translate(x, transY)
        drawable.draw(canvas)
        canvas.restore()
    }
}
