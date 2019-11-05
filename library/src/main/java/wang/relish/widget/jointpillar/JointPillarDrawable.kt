package wang.relish.widget.jointpillar

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

import wang.relish.widget.IDrawable


/**
 * @author wangxin
 * @since 20191104
 */
class JointPillarDrawable(percent: Float) : IDrawable<Float>(percent) {

    override fun draw(canvas: Canvas) {
        val w = bounds.width().toFloat()
        val xStart = (Dimens.WINDOW_WIDTH + Dimens.WINDOW_LINE_LENGTH) * w
        val yStart =
            (Dimens.BAR_TOTAL_HEIGHT + Dimens.BAR_TOP_END_HEIGHT + Dimens.WINDOW_HEIGHT / 2) * w
        val singleBarHeight = Dimens.SINGLE_BAR_HEIGHT * w

        mPaint.strokeWidth = Dimens.BAR_WIDTH * w
        val barCount = Colors.COLORS.size
        for (i in 0 until barCount) {
            mPaint.color = Colors.COLORS[i]
            if (i == 0) {
                mPaint.strokeCap = Paint.Cap.ROUND
            } else {
                mPaint.strokeCap = Paint.Cap.BUTT
            }
            val startY = yStart - i * singleBarHeight
            canvas.drawLine(
                xStart,
                startY,
                xStart,
                startY - singleBarHeight,
                mPaint
            )
            // 画字体
            mPaint.textSize = Dimens.AXIS_TEXT_SIZE * w
            mPaint.color = Colors.AXIS_TEXT_COLOR
            val baseline = mPaint.descent() + mPaint.ascent()
            canvas.drawText(
                Strings.AXIS_TEXT[i],
                xStart + Dimens.BAR_MARGIN_RIGHT * w,
                startY - baseline / 2,
                mPaint
            )
            if (i == barCount - 1) {
                canvas.drawText(
                    Strings.AXIS_TEXT[i + 1],
                    xStart + Dimens.BAR_MARGIN_RIGHT * w,
                    startY - singleBarHeight - baseline / 2,
                    mPaint
                )
            }
        }
        mPaint.color = Colors.COLORS[barCount - 1]
        mPaint.strokeCap = Paint.Cap.ROUND
        canvas.drawLine(
            xStart,
            yStart - (barCount - 0.5f) * singleBarHeight,
            xStart,
            yStart - barCount * singleBarHeight,
            mPaint
        )
        mPaint.strokeCap = Paint.Cap.BUTT

        val percent:Float = mData
        val height = percent * Dimens.BAR_TOTAL_HEIGHT * w
        //Math.min(Dimens.CIRCLE_MAX_Y * w, Math.max(Dimens.CIRCLE_MIN_Y * w, percent * Dimens.BAR_TOTAL_HEIGHT * w));
        drawWindow(canvas, w, xStart, yStart - height)
    }

    private fun drawWindow(canvas: Canvas, w: Float, xCenter: Float, yCenter: Float) {
        mPaint.color = Colors.COLOR_WINDOW
        mPaint.strokeWidth = Math.max(1.0f, Dimens.WINDOW_LINE_WIDTH * w)
        // 划线
        canvas.drawLine(xCenter, yCenter, xCenter - Dimens.WINDOW_LINE_LENGTH * w, yCenter, mPaint)
        // 画球
        mPaint.color = Colors.CIRCLE_COLOR
        canvas.drawCircle(xCenter, yCenter, Dimens.CIRCLE_RADIUS * w, mPaint)
        // 画矩形框
        mPaint.color = Colors.COLOR_WINDOW
        mPaint.textSize = Dimens.WINDOW_TEXT_SIZE * w
        val textWidth = mPaint.measureText(Strings.TEXT)
        val textHeight = mPaint.descent() - mPaint.ascent()
        val rectWidth = textWidth + Dimens.TEXT_PADDING_HORIZONTAL * w
        val rectHeight = textHeight + Dimens.TEXT_PADDING_VERTICAL * w
        val left = xCenter - (Dimens.WINDOW_LINE_LENGTH * w + rectWidth)
        val top = yCenter - rectHeight / 2
        val cornerRadius = Dimens.RECT_CORNER_RADIUS * w
        canvas.drawRoundRect(
            left, top, left + rectWidth, top + rectHeight,
            cornerRadius, cornerRadius,
            mPaint
        )
        // 画文字
        mPaint.color = Colors.TEXT_COLOR
        val xTextStart = left + rectWidth / 2 - textWidth / 2
        val yTextStart = top + textHeight / 2 - (mPaint.descent() + mPaint.ascent())
        canvas.drawText(Strings.TEXT, xTextStart, yTextStart, mPaint)
    }

    /**
     * 这是一个高度为基准的自定义View
     */
    private annotation class Dimens {
        companion object {

            /** 高度基准(类似宽度基准的750px)  */
            val W = 162.0f

            val BAR_TOTAL_HEIGHT = 250.0f / W

            val BAR_TOP_END_HEIGHT = 5.0f / W

            val SINGLE_BAR_HEIGHT = 50.0f / W

            val BAR_WIDTH = 12.0f / W

            val BAR_MARGIN_RIGHT = 8.0f / W

            val CIRCLE_RADIUS = 5.0f / W

            val WINDOW_HEIGHT = 32.0f / W

            val WINDOW_WIDTH = 100.0f / W

            val WINDOW_LINE_LENGTH = (20.0f + CIRCLE_RADIUS) / W
            val WINDOW_LINE_WIDTH = 1.0f / W

            val CIRCLE_MIN_Y = 6.0f / W
            val CIRCLE_MAX_Y = BAR_TOTAL_HEIGHT - CIRCLE_MIN_Y

            val AXIS_TEXT_SIZE = 20.0f / W

            val TEXT_PADDING_VERTICAL = 12.0f / W

            val TEXT_PADDING_HORIZONTAL = 20.0f / W

            val WINDOW_TEXT_SIZE = 20.0f / W

            val RECT_CORNER_RADIUS = 8.0f / W
        }
    }

    private annotation class Colors {
        companion object {
            val COLORS = intArrayOf(
                Color.parseColor("#7BEAF3"),
                Color.parseColor("#50E3C2"),
                Color.parseColor("#FCC800"),
                Color.parseColor("#FF7F7F"),
                Color.parseColor("#D348B5")
            )
            val COLOR_WINDOW = Color.parseColor("#6F6F6F")
            val CIRCLE_COLOR = Color.WHITE
            val TEXT_COLOR = Color.WHITE
            val AXIS_TEXT_COLOR = Color.parseColor("#999999")
        }
    }

    private annotation class Strings {
        companion object {
            val TEXT = "实时压力"
            val AXIS_TEXT = arrayOf("0", "20", "40", "60", "80", "100")
        }
    }

    companion object {


        val TOTAL_HEIGHT = 292.0f / Dimens.W

        @ColorInt
        fun percentToColor(@FloatRange(from = 0.0, to = 1.0) percent: Float): Int {
            val index = (percent * 5f).toInt()
            return Colors.COLORS[Math.min(4, index)]
        }
    }
}
