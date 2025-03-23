package wang.relish.widget.sample.sample.rich

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

class ClickableDrawableTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var drawableRight: Drawable? = null
    private var drawableWidth = 0
    private var drawableHeight = 0
    private val clickableArea: Rect = Rect(0, 0, 0, 0)
    private var onClickListener: (() -> Unit)? = null

    fun setDrawableRight(drawable: Drawable, size: Int, listener: () -> Unit) {
        this.drawableRight = drawable
        // 将尺寸强制设置为传入的size
        drawable.bounds = Rect(0, 0, size, size)
        drawableWidth = size
        drawableHeight = size
        this.onClickListener = listener
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x55FF0000
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val layout = layout ?: return
        // 获取最后一行的索引
        val lastLine = layout.lineCount - 1
        // 修正垂直居中计算
        val lineTop = layout.getLineTop(lastLine)
        val lineBottom = layout.getLineBottom(lastLine)
        val centerY = (lineTop + lineBottom) / 2  // 获取文字行的垂直中心
        val drawableTop = centerY - drawableHeight / 2 // 图标顶部位置

        val lastLineEndX = (layout.getLineRight(lastLine) - paddingEnd).toInt()  // 水平位置
        drawableRight?.let {
            clickableArea.apply {
                left = lastLineEndX
                top = drawableTop
                right = lastLineEndX + drawableWidth
                bottom = drawableTop + drawableHeight
            }
            it.setBounds(
                lastLineEndX, drawableTop,
                lastLineEndX + drawableWidth, drawableTop + drawableHeight
            )
            it.draw(canvas)
            canvas.drawRect(clickableArea, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            val x = event.x
            val y = event.y
            val area = clickableArea
            if (x >= area.left && x <= area.right && y >= area.top && y <= area.bottom) {
                onClickListener?.invoke()
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}