package wang.relish.widget.sample.sample.rich

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import wang.relish.widget.sample.util.dpF

class DashedUnderlineTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val path = Path()
    private val lineThickness = 1.dpF
    private var isUnderlineEnable = true

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = /*if (SkinManager.getInstance()?.currentSkin == SkinThemeIndex.SKIN_THEME_NIGHT)*/
            0x33000000 /*else 0xD6FFFFFF.toInt()*/
        style = Paint.Style.STROKE
        strokeWidth = lineThickness
        pathEffect = DashPathEffect(floatArrayOf(4.dpF, 2.dpF), 0f)
    }

    fun setUnderlineEnabled(enable: Boolean) {
        isUnderlineEnable = enable
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isUnderlineEnable.not()) {
            return
        }
        layout ?: return
        val lineCount = layout.lineCount
        for (i in 0 until lineCount) {
            val startX = layout.getLineLeft(i) + paddingStart
            val endX = layout.getLineRight(i) - paddingEnd
            val bottom = layout.getLineBottom(i)
            path.reset()
            path.moveTo(startX, bottom - lineThickness)
            path.lineTo(endX, bottom - lineThickness)
            canvas.drawPath(path, paint)
            path.reset()
        }
    }
}