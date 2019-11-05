package wang.relish.widget.multibar


import android.graphics.*
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import wang.relish.widget.IDrawable
import java.util.*
import kotlin.math.pow

/**
 * @author wangxin
 * @since 20191028
 */
class MultiBarDrawable(
    data: IMultiPoints
) : IDrawable<IMultiPoints>(data) {

    private val path = Path()

    override fun draw(canvas: Canvas) {
        val w = bounds.width().toFloat()
        ///////////////////////////////////// 1 画三条bar /////////////////////////////////////
        val xCenter = w / 2.0f
        val yCenter = (w * (Dimens.ENDPOINT_TEXT_SIZE
                + Dimens.ENDPOINT_TXT_MARGIN_CLOSE + Dimens.BAR_LENGTH))
        val endPoints = mData.endPoints()
        val mMin = mData.min()
        val dataCount = endPoints.size
        val angle = 360.0f / dataCount
        mPaint.strokeWidth = Dimens.BAR_WIDTH * w
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.maskFilter = null
        for (i in 0 until dataCount) { // 第0根指向正北方, 顺时针旋转
            // 准备数据
            val endpoint = endPoints[i]
            // 开始绘制
            canvas.save()
            canvas.rotate(i * angle, xCenter, yCenter)
            canvas.translate(0f, 0f)
            // 画bar背景
            mPaint.shader = null
            mPaint.color = Colors.BAR_BG_COLOR
            canvas.drawLine(
                xCenter,
                yCenter,
                xCenter,
                yCenter - Dimens.BAR_LENGTH * w,
                mPaint
            )

            // 画bar值
            // 出头长度占出头总长度的比例 rate = (value-min)/(max-min)
            // l = w * (rate*(barLength-innerRadius) + innerRadius）
            @FloatRange(from = 0.0, to = 1.0) val rate = Math.min(
                1.0,
                Math.max(0.0, endpoint.value() - mMin) / (Dimens.MAX_VALUE - mMin)
            )
            val length =
                (w * (rate * (Dimens.BAR_LENGTH - Dimens.INNER_RADIUS) + Dimens.INNER_RADIUS)).toFloat()
            val gradient = LinearGradient(
                xCenter,
                yCenter,
                xCenter,
                yCenter - length,
                Colors.BAR_START_COLOR,
                Colors.BAR_END_COLOR,
                Shader.TileMode.CLAMP
            )
            mPaint.shader = gradient
            mPaint.strokeWidth = Dimens.BAR_WIDTH * w
            mPaint.strokeCap = Paint.Cap.ROUND
            canvas.drawLine(
                xCenter,
                yCenter,
                xCenter,
                yCenter - length,
                mPaint
            )
            // 保存画布
            canvas.restore()
        }
        ///////////////////////////////////// 2 画文字 /////////////////////////////////////
        mPaint.shader = null
        for (i in 0 until dataCount) { // 第0根指向正北方, 顺时针旋转
            val endpoint = endPoints[i]
            // 画端点文字
            val radiusOfDegrees = i.toDouble() * angle.toDouble() * Math.PI / 180f
            val sin = Math.sin(radiusOfDegrees)
            val cos = Math.cos(radiusOfDegrees)
            val xOffset =
                (sin * (Dimens.ENDPOINT_TXT_MARGIN_CLOSE + Dimens.ENDPOINT_TEXT_SIZE / 2).toDouble() * w.toDouble()).toFloat()
            val yOffset =
                (if (cos > 0) -1 else 1).toFloat() * (Dimens.ENDPOINT_TXT_MARGIN_CLOSE + Dimens.ENDPOINT_TEXT_SIZE / 2) * w
            val xEndPoint =
                (xCenter.toDouble() + w.toDouble() * Dimens.BAR_LENGTH.toDouble() * sin + xOffset.toDouble()).toFloat()
            val yEndPoint =
                (yCenter - w.toDouble() * Dimens.BAR_LENGTH.toDouble() * cos + yOffset).toFloat()
            mPaint.color = Colors.ENDPOINT_TEXT_COLOR
            mPaint.strokeWidth = 0f
            mPaint.textSize = Dimens.ENDPOINT_TEXT_SIZE * w
            mPaint.strokeCap = Paint.Cap.BUTT
            val text = endpoint.name()
            val textWidth = mPaint.measureText(text)
            val baseline = mPaint.ascent() + mPaint.descent()
            canvas.drawText(text, xEndPoint - textWidth / 2, yEndPoint - baseline / 2, mPaint)

            // 画值窗口文字
            // 计算值端点坐标(计算公式):
            // x1 = x0 + l * sin(θ);
            // y1 = y0 + l * cos(θ);
            val value = endpoint.value()
            @FloatRange(from = 0.0, to = 1.0) val rate = Math.min(
                1.0,
                Math.max(0.0, value - mMin) / (Dimens.MAX_VALUE - mMin)
            )
            val length = (w * (rate * (Dimens.BAR_LENGTH - Dimens.INNER_RADIUS)
                    + Dimens.INNER_RADIUS.toDouble() + Dimens.BAR_END_RADIUS.toDouble())).toFloat()
            val xTemp = xCenter + length * sin
            val flag = if (xTemp + Math.pow(10.0, -5.0) >= w / 2) 1 else -1
            val xValuePoint = (xTemp + flag.toFloat() * 1.0f * w * Dimens.BAR_END_RADIUS).toFloat()
            val yValuePoint = (yCenter - length * cos).toFloat()
            val valueText = String.format(
                Locale.CHINA, "%.0f%s", value, Strings.UNIT_TEXT
            )
            drawWindow(canvas, w, valueText, xValuePoint, yValuePoint)
        }
        ///////////////////////////////////// 3 画中心两圆 /////////////////////////////////////
        drawCircle(canvas, w, xCenter, yCenter)
    }

    /**
     * 绘制悬浮窗
     *
     * @param canvas      画布
     * @param w           view宽度
     * @param text        文字
     * @param xValuePoint 端点x坐标
     * @param yValuePoint 端点y坐标
     */
    private fun drawWindow(
        canvas: Canvas,
        w: Float,
        text: String,
        xValuePoint: Float,
        yValuePoint: Float
    ) {
        // -1代表左边;1代表右边
        @IntRange(from = -1, to = 1) val dir = if (xValuePoint + Dimens.ZERO_BIGGER >= w / 2)
            1
        else
            -1
        mPaint.textSize = Dimens.POPUP_TEXT_SIZE * w
        mPaint.strokeWidth = 1f
        mPaint.color = Colors.POPUP_SHADOW_COLOR
        mPaint.style = Paint.Style.FILL
        val blurMaskFilter = BlurMaskFilter(
            Dimens.POPUP_BLUR_RADIUS * w, BlurMaskFilter.Blur.OUTER
        )
        mPaint.maskFilter = blurMaskFilter

        /* 尖角开始的地方 */
        val xStart = xValuePoint + dir.toFloat() * w * Dimens.POPUP_MARGIN_START
        val textHeight = mPaint.descent() - mPaint.ascent()
        val baseLine = mPaint.descent() + mPaint.ascent()
        val textWidth = mPaint.measureText(text)

        val bubbleHeight = Dimens.POPUP_MARGIN_VERTICAL * w + textHeight
        val bubbleWidth = Dimens.POPUP_MARGIN_HORIZONTAL * w + textWidth
        val cr = Dimens.POPUP_CORNER_RADIUS * w

        // 画悬浮窗
        path.reset()
        // p0
        path.moveTo(xStart, yValuePoint)
        // p1
        val angleDx = w * Dimens.POPUP_ANGLE_DX
        val angleDy = w * Dimens.POPUP_ANGLE_DY
        val l = xStart + (if (dir == -1) -bubbleWidth else 0F) + dir * angleDx
        val t = yValuePoint - bubbleHeight / 2
        val radii = floatArrayOf(cr, cr, cr, cr, cr, cr, cr, cr)
        path.addRoundRect(l, t, l + bubbleWidth, t + bubbleHeight, radii, Path.Direction.CW)
        path.moveTo(xStart + dir * angleDx, yValuePoint + angleDy)
        path.lineTo(xStart, yValuePoint)
        path.lineTo(xStart + dir * angleDx, yValuePoint - angleDy)
        path.close()
        canvas.drawPath(path, mPaint)
        mPaint.maskFilter = null
        mPaint.shader = null
        mPaint.color = Color.WHITE
        mPaint.strokeWidth = 1f
        mPaint.style = Paint.Style.FILL
        canvas.drawPath(path, mPaint)

        // 绘制文字
        mPaint.maskFilter = null
        mPaint.shader = null
        mPaint.style = Paint.Style.FILL
        mPaint.color = Colors.POPUP_TEXT_COLOR
        val xTextCenter = l + (if (dir == 1) angleDx else 0F) + (bubbleWidth - angleDx) / 2f
        canvas.drawText(text, xTextCenter - textWidth / 2f, yValuePoint - baseLine / 2f, mPaint)
    }

    /**
     * 画中心两圆
     */
    private fun drawCircle(canvas: Canvas, w: Float, xCenter: Float, yCenter: Float) {
        // 还原画笔配置
        mPaint.shader = null
        mPaint.strokeCap = Paint.Cap.BUTT
        mPaint.strokeWidth = 0f
        // 画中心黄色双层圆
        mPaint.color = Colors.OUTER_CIRCLE_COLOR
        canvas.drawCircle(xCenter, yCenter, Dimens.OUTER_RADIUS * w, mPaint)
        mPaint.color = Colors.INNER_CIRCLE_COLOR
        canvas.drawCircle(xCenter, yCenter, Dimens.INNER_RADIUS * w, mPaint)
    }

    /** 各尺寸占屏幕宽度的比例  */
    private annotation class Dimens {
        companion object {
            ///////////////// 各种长度尺寸 /////////////////

            // POPUP
            val POPUP_BLUR_RADIUS = 24.0f / 750.0f

            // 14+24
            val POPUP_MARGIN_HORIZONTAL = 34.0f / 750.0f// 不包含阴影(101F),仅气泡内
            val POPUP_MARGIN_VERTICAL = 30.0f / 750.0f// 不包含阴影(84F),仅气泡内

            /** 三端点浮窗距端点的距离  */
            val POPUP_MARGIN_START = 11.0f / 750.0f
            /** 浮窗尖角宽度  */
            val POPUP_ANGLE_WIDTH = 8.0f / 750.0f
            val POPUP_ANGLE_DY = 8.0f / 750.0f
            val POPUP_ANGLE_DX = 8.0f / 750.0f
            val POPUP_CORNER_RADIUS = 5.0f / 750.0f

            /** 各端点距离端点文字的距离  */
            val ENDPOINT_TXT_MARGIN_CLOSE = 24.0f / 750.0f
            val POPUP_WINDOW_MARGIN_RIGHT = (30.0f / 750.0f).toDouble()
            /** 三端点浮窗最大宽度  */
            val POPUP_WINDOW_MAX_WIDTH = (117.0f / 750.0f).toDouble()
            /** 三端点浮窗高度  */
            val POPUP_WINDOW_HEIGHT = (44.0f / 750.0f).toDouble()
            /** 条状bar粗细  */
            val BAR_WIDTH = 20.0f / 750.0f
            /** 条状bar尖端圆角高度  */
            val BAR_END_RADIUS = 8.0f / 750.0f
            /** 条状bar长度  */
            val BAR_LENGTH = ((1 - (POPUP_WINDOW_MAX_WIDTH
                    + POPUP_MARGIN_START.toDouble()
                    + POPUP_WINDOW_MARGIN_RIGHT) * 2) / 2F).toFloat()
            /** bar最大值  */
            val MAX_VALUE = 1000f

            val OUTER_RADIUS = 44.0f / 750.0f
            val INNER_RADIUS = 30.0f / 750.0f

            ///////////////// 文字尺寸 /////////////////
            /** 各端点距离端点文字大小  */
            val ENDPOINT_TEXT_SIZE = 28.0f / 750.0f
            /** 三端点浮窗文字大小  */
            val POPUP_TEXT_SIZE = 18.0f / 750.0f

            val ZERO_BIGGER = 10.0.pow(-5.0)
        }
    }

    private annotation class Colors {
        companion object {
            /** 三条Bar的背景颜色  */
            val BAR_BG_COLOR = Color.parseColor("#F1F1F1")
            /** 三条Bar的开始颜色  */
            val BAR_START_COLOR = Color.parseColor("#F1F1F1")
            /** 三条Bar的结束颜色  */
            val BAR_END_COLOR = Color.parseColor("#8DC652")

            /** 内圈颜色  */
            val INNER_CIRCLE_COLOR = Color.parseColor("#FFC53B")
            /** 外圈颜色  */
            val OUTER_CIRCLE_COLOR = Color.parseColor("#88FFC53B")


            /** 端点文字字体颜色值  */
            val ENDPOINT_TEXT_COLOR = Color.parseColor("#999999")
            /** 悬浮窗文字字体颜色值  */
            val POPUP_TEXT_COLOR = Color.parseColor("#8DC652")
            /** 悬浮窗的阴影颜色  */
            val POPUP_SHADOW_COLOR = Color.parseColor("#E2E2E2")
        }
    }

    private annotation class Strings {
        companion object {
            const val UNIT_TEXT = "mm"
        }
    }

    companion object {
        //////////////////////////////////// 尺寸、颜色、常量字符串相关 ////////////////////////////////////
        internal val TOTAL_HEIGHT = 2 * Dimens.BAR_LENGTH +
                (Dimens.ENDPOINT_TEXT_SIZE + Dimens.ENDPOINT_TXT_MARGIN_CLOSE) * 2F
    }
}
