package wang.relish.widget.graph

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import kotlin.math.abs
import kotlin.math.pow


/**
 * 折线图
 *
 * @author relish
 * @since 20191010
 */
internal open class ChartDrawable constructor(
    private var mData: ChartData,
    /** 自定义字体(可选) */
    private val tf: Typeface? = null
) : Drawable() {

    /**
     * 更新最值
     */
    private fun updateSelection(index: Int = 0) {
        this.index = index
        if (mData.items.isEmpty()) {
            this.min = "0"
            this.max = "0"
            realMax = 0.0
        } else {
            this.min = mData.getText(mData.items[index].min)
            this.max = mData.getText(mData.items[index].max)

            realMax = mData.items[0].max
            for (item in mData.items) {
                if (item.max > realMax) {
                    realMax = item.max
                }
            }
        }
    }

    /**
     * 更新图表数据📈
     */
    fun updateValue(data: ChartData) {
        mData = data
        updateSelection()
    }

    // 选中值索引
    private var index: Int = 0
    // 真正的最大值
    private var realMax: Double = 0.0

    private lateinit var min: String
    private lateinit var max: String

    init {
        updateSelection()
    }

    private var width: Float = 0F

    /**
     * 绘制自定义Drawable的图案
     */
    fun draw(canvas: Canvas, cellSize: Float) {
        width = cellSize
        // 画最值
        drawMostValueInternal(canvas, cellSize, Dimens.TEXT_AREA_HEIGHT * cellSize)
        // 画折线图和日期
        drawChart(canvas, cellSize, cellSize * Dimens.CHART_AREA_HEIGHT)
    }


    /**
     * 画数值
     * @param w 屏幕宽度
     * @param h 数值区域高度
     */
    private fun drawMostValueInternal(canvas: Canvas, w: Float, h: Float) {
        //////////////////////// 最高值 ////////////////////////
        drawMostValueArea(canvas, w, h, -w / 4, Strings.MAX_TEXT, max)
        //////////////////////// 画竖线 ////////////////////////
        val lineHeight = w * Dimens.MOST_VALUE_LINE_HEIGHT
        mPaint.color = Colors.LINE_COLOR
        val preLineWith = mPaint.strokeWidth
        mPaint.strokeWidth = w * Dimens.MOST_VALUE_LINE_WIDTH
        canvas.drawLine(
            w / 2,
            h / 2 - lineHeight / 2,
            w / 2,
            h / 2 + lineHeight / 2,
            mPaint
        )
        mPaint.strokeWidth = preLineWith
        //////////////////////// 最低值 ////////////////////////
        drawMostValueArea(canvas, w, h, w / 4, Strings.MIN_TEXT, min)
    }

    private fun drawMostValueArea(
        canvas: Canvas,
        w: Float,
        h: Float,
        xOffset: Float, // -w/4 | w/4
        mostTxt: String, // Strings.MAX_TEXT | Strings.MIN_TEXT
        mostValue: String // max | min
    ) {
        // 1 画最X"值"
        if (tf != null) {
            mPaint.typeface = tf
        }
        // ① 边距
        val margin = w * Dimens.BETWEEN_MOST_VALUE_AND_MOST_TXT_MARGIN // 边距
        // ② "最X值"字宽 (先算最X值的字宽，防止mPaint上设置的字体大小被覆盖)
        val maxOrMinTxtTextSize = w * Dimens.MAX_OR_MIN_TXT_TEXT_SIZE
        mPaint.textSize = maxOrMinTxtTextSize
        val maxTxtWidth = mPaint.measureText(mostTxt)
        // ③ "最X值"字宽 (因为㎎/m³的宽度小于"最高值"字宽, 故取"最高值"字宽)
        mPaint.textSize = w * Dimens.MAX_OR_MIN_VALUE_TEXT_SIZE // 最值字体大小
        val maxValueWidth = mPaint.measureText(mostValue) // 最值字宽
        // ④ 总长度
        val maxAreaTotalWidth = margin + maxTxtWidth + maxValueWidth

        mPaint.color = Colors.TEXT_COLOR
        val mostValueHeight = mPaint.ascent() + mPaint.descent()
        val maxAreaStartX = xOffset + w / 2 - maxAreaTotalWidth / 2
        canvas.drawText(
            mostValue,
            maxAreaStartX, // 选定开始x坐标(最大值、"最大值"、㎎/m³三者水平居中)
            h / 2 - mostValueHeight / 2,
            mPaint
        )
        //2 画"最X值"
        mPaint.typeface = Typeface.DEFAULT_BOLD
        mPaint.textSize = maxOrMinTxtTextSize
        mPaint.color = Colors.TEXT_COLOR
        val maxTxtTextHeight = mPaint.ascent() + mPaint.descent()
        val maxAreaStartXForRightArea = maxAreaStartX + maxValueWidth + margin
        canvas.drawText(
            mostTxt,
            maxAreaStartXForRightArea,
            h / 2 - maxTxtTextHeight / 2 + w * Dimens.MAX_OR_MIN_TXT_TEXT_Y_OFFSET,
            mPaint
        )
        //3 画"㎎/m³"
        mPaint.typeface = Typeface.DEFAULT
        mPaint.textSize = w * Dimens.UNIT_TXT_TEXT_SIZE
        mPaint.color = Colors.UNIT_TEXT_COLOR
        val unitTxtTextHeight = mPaint.ascent() + mPaint.descent()
        canvas.drawText(
            this.mData.unit,
            maxAreaStartXForRightArea,
            h / 2 - unitTxtTextHeight / 2 + w * Dimens.UNIT_TXT_TEXT_Y_OFFSET,
            mPaint
        )
    }

    private val lowPath = Path()
    private val highPath = Path()
    /**
     * 画折线图和日期
     * @param w 屏幕宽度
     * @param h 绘画区域高度
     */
    private fun drawChart(canvas: Canvas, w: Float, h: Float) {

        val wUnit = w / mData.items.size
        /* y坐标开始位置 */
        val startY = w * (Dimens.TEXT_AREA_HEIGHT + Dimens.CHART_AREA_HEIGHT)
        val timeAreaHeight = w * Dimens.TIME_AREA_HEIGHT
        // 0 画折线图和时间之间的分割线
        mPaint.strokeWidth = 1F
        mPaint.color = Colors.LINE_COLOR
        canvas.drawLine(0F, startY, w, startY, mPaint)
        // 初始化path
        lowPath.reset()
        var lowMax = Int.MIN_VALUE.toDouble()// 所有最低值里的最大值
        var lowMaxY = 0F// 所有最低值里的最大值所处的Y坐标
        var lowMin = Int.MAX_VALUE.toDouble()// 所有最低值里的最小值
        var lowMinY = 0F// 所有最低值里的最小值所处的Y坐标
        highPath.reset()
        var highMax = 0.0// 所有最高值里的最大值
        var highMaxY = 0F// 所有最高值里的最大值所处的Y坐标
        for (i in 0 until mData.items.size) {
            val startX = wUnit / 2 + wUnit * i
            val item = mData.items[i]
            // 1 画日期
            mPaint.textSize = w * Dimens.TIME_TEXT_SIZE
            mPaint.color = Colors.TIME_TEXT_COLOR
            val textWidth = mPaint.measureText(item.time)
            val textHeight = mPaint.ascent() + mPaint.descent()
            canvas.drawText(
                item.time,
                startX - textWidth / 2,
                startY + timeAreaHeight / 2 - textHeight / 2,
                mPaint
            )
            if (realMax <= 0) continue
            // 2 画折线图
            // low线数值准备
            val lowStartY = startY - (item.min.toFloat() / realMax.toFloat()) * h
            // 取最高值1
            if (item.min > lowMax) {
                lowMax = item.min
                lowMaxY = lowStartY
            }
            if (item.min < lowMin) {
                lowMin = item.min
                lowMinY = lowStartY
            }
            // high线数值准备
            val highStartY = startY - (item.max.toFloat() / realMax.toFloat()) * h
            if (item.max > highMax) {
                highMax = item.max
                highMaxY = highStartY
            }

            if (i == mData.items.size - 1) continue
            val endX = startX + wUnit
            // 最低值连线
            val lowEndY = startY - (mData.items[i + 1].min.toFloat() / realMax.toFloat()) * h

            if (i == 0) {
                lowPath.moveTo(startX, startY)
                lowPath.cubicTo(startX, startY, startX, startY, startX, lowStartY)
            } else {

                val px = startX - wUnit
                val py = startY - (mData.items[i - 1].min.toFloat() / realMax.toFloat()) * h
                appendBezier(lowPath, px, py, startX, lowStartY)
            }
            if (i == mData.items.size - 2) {
                appendBezier(lowPath, startX, lowStartY, endX, lowEndY)
                lowPath.cubicTo(endX, startY, endX, startY, endX, startY)
                lowPath.close()
            }
            mPaint.strokeWidth = 1F
            mPaint.color = Color.RED
            // 折线图实现已弃用, 改为贝塞尔曲线实现
            // canvas.drawLine(startX, lowStartY, endX, lowEndY, mPaint)

            // 最高值连线
            val highEndY = startY - (mData.items[i + 1].max.toFloat() / realMax.toFloat()) * h
            if (i == 0) {
                highPath.moveTo(startX, startY)
                highPath.cubicTo(startX, startY, startX, startY, startX, highStartY)
            } else {
                val px = startX - wUnit
                val py = startY - (mData.items[i - 1].max.toFloat() / realMax.toFloat()) * h
                appendBezier(highPath, px, py, startX, highStartY)
            }
            if (i == mData.items.size - 2) { // 最后一个点
                appendBezier(highPath, startX, highStartY, endX, highEndY)
                highPath.cubicTo(endX, startY, endX, startY, endX, startY)
                highPath.close()
            }
            // 折线图实现已弃用, 改为贝塞尔曲线实现
            // canvas.drawLine(startX, highStartY, endX, highEndY, mPaint)
        }

        // 画high线(highMaxY===lowMinY说明两条线重合且都是直线)
        if (abs(highMaxY - lowMinY) >= 10.0.pow(-5.0)) { // highMaxY大于lowMinY的话
            mPaint.shader = LinearGradient(
                0F, highMaxY,
                0F, lowMinY, Colors.BG_START_COLOR, Color.TRANSPARENT, Shader.TileMode.CLAMP
            )
            canvas.drawPath(highPath, mPaint)
        }

        // 画low线
        mPaint.shader = LinearGradient(
            0F, lowMaxY,
            0F, startY, Colors.BG_START_COLOR, Color.TRANSPARENT, Shader.TileMode.CLAMP
        )
        canvas.drawPath(lowPath, mPaint)
        mPaint.shader = null

        // 3 画选中值
        if (index < 0 || index > mData.items.size - 1) return

        val startX = wUnit / 2 + wUnit * index
        val maxStartY = startY - (mData.items[index].max.toFloat() / realMax.toFloat()) * h
        val minStartY = startY - (mData.items[index].min.toFloat() / realMax.toFloat()) * h
        // 画最大值圆
        mPaint.color = Colors.OUTER_CIRCLE_COLOR
        canvas.drawCircle(startX, maxStartY, w * Dimens.OUTER_RADIUS, mPaint)
        mPaint.color = Colors.INNER_CIRCLE_COLOR
        canvas.drawCircle(startX, maxStartY, w * Dimens.INNER_RADIUS, mPaint)
        // 画最小值圆
        mPaint.color = Colors.OUTER_CIRCLE_COLOR
        canvas.drawCircle(startX, minStartY, w * Dimens.OUTER_RADIUS, mPaint)
        mPaint.color = Colors.INNER_CIRCLE_COLOR
        canvas.drawCircle(startX, minStartY, w * Dimens.INNER_RADIUS, mPaint)

        // 画线
        mPaint.color = Colors.LINE_COLOR
        canvas.drawLine(startX, maxStartY, startX, startY, mPaint)
    }

    private fun appendBezier(
        path: Path,
        px: Float, py: Float,
        cx: Float, cy: Float
    ) {
        val diff1x = cx - px
        path.cubicTo(
            px + diff1x / 2f, py,
            px + diff1x / 2f, cy,
            cx, cy
        )
    }

    /**
     * 各尺寸占屏幕宽度的比例
     */
    private annotation class Dimens {
        companion object {

            //////////////// 区域高度 ////////////////
            /** 文字区高度(最高值、最低值)  */
            const val TEXT_AREA_HEIGHT = 183f / 750f
            /** 折线图区高度  */
            const val CHART_AREA_HEIGHT = 301f / 750f
            /** (折线图和日期)分割线高度  */
            const val LINE_AREA_HEIGHT = 1f / 750f
            /** 时间文字高度  */
            const val TIME_AREA_HEIGHT = 70f / 750f

            //////////////// 文字尺寸相关 ////////////////
            /* "最X值"与"㎎/m³"的间距的修正系数 */
            private const val BETWEEN_MOST_TXT_AND_UNIT_CORRECTION_FACTOR = -17f // 绝对值越大越接近
            /* 字体大小修正系数 */
            private const val TEXT_CORRECTION_FACTOR = -10f
            // 数值
            /** 最高/低值数字文字大小 */
            const val MAX_OR_MIN_VALUE_TEXT_SIZE = 100f / 750f
            // 文案
            /** 最高/低值文案文字大小 */
            const val MAX_OR_MIN_TXT_TEXT_SIZE = (37f + TEXT_CORRECTION_FACTOR) / 750f
            /** "最高/低值"文案文字中心距离屏幕中心线的Y轴偏移量 */
            const val MAX_OR_MIN_TXT_TEXT_Y_OFFSET =
                -(37.5f + BETWEEN_MOST_TXT_AND_UNIT_CORRECTION_FACTOR) / 750f
            // 单位
            /** ㎎/m³文案文字大小 */
            const val UNIT_TXT_TEXT_SIZE = (33f + TEXT_CORRECTION_FACTOR) / 750f
            /** ㎎/m³文案文字中心距离屏幕中心线的Y轴偏移量 */
            const val UNIT_TXT_TEXT_Y_OFFSET =
                (32.5f + BETWEEN_MOST_TXT_AND_UNIT_CORRECTION_FACTOR) / 750f
            // 最值分界线
            const val MOST_VALUE_LINE_HEIGHT = 81f / 750f
            const val MOST_VALUE_LINE_WIDTH = 1f / 750f

            // 最值距离右侧"最X值"和"㎎/m³"的间距
            const val BETWEEN_MOST_VALUE_AND_MOST_TXT_MARGIN = 27f / 750f

            // 圆圈
            const val INNER_RADIUS = 8f / 750f
            const val OUTER_RADIUS = 10f / 750f

            /** 时间文字大小 */
            const val TIME_TEXT_SIZE = 22f / 750f
        }
    }

    private annotation class Colors {
        companion object {
            val BG_START_COLOR = Color.parseColor("#95C1FD")
            /** 线条颜色  */
            val LINE_COLOR = Color.parseColor("#FFFFFF")
            /** 日期颜色  */
            val TIME_TEXT_COLOR = Color.parseColor("#CCFFFFFF")
            /** ㎎/m³字体颜色  */
            val UNIT_TEXT_COLOR = Color.parseColor("#CCFFFFFF")
            /** 内圆背景颜色  */
            val INNER_CIRCLE_COLOR = Color.parseColor("#FFFFFF")
            /** 外圆背景颜色  */
            val OUTER_CIRCLE_COLOR = Color.parseColor("#CCFFFFFF")
            /** 普通文字颜色  */
            val TEXT_COLOR = Color.parseColor("#FFFFFF")
        }
    }

    private annotation class Strings {
        companion object {
            const val MAX_TEXT = "最高值"
            const val MIN_TEXT = "最低值"
        }
    }

    companion object {
        /** 总高度 */
        const val TOTAL_HEIGHT = Dimens.TEXT_AREA_HEIGHT +
                Dimens.CHART_AREA_HEIGHT +
                Dimens.LINE_AREA_HEIGHT +
                Dimens.TIME_AREA_HEIGHT
    }

    fun onTouchEvent(
        event: MotionEvent,
        consumer: ChartView.Consumer
    ): Boolean {
        val x = event.x
        val y = event.y
        invokableRect(width)
        if (!invokableArea!!.contains(x, y)) return consumer.accept(event)

        if (event.action == MotionEvent.ACTION_DOWN ||
            event.action == MotionEvent.ACTION_MOVE
        ) {
            updateSelection(locateIndex(x))
            consumer.invalidate()
            return true
        }
        return consumer.accept(event)
    }


    private var invokableArea: RectF? = null

    private fun invokableRect(w: Float) {
        val l = 0F
        val t = w * Dimens.TEXT_AREA_HEIGHT
        val h = w * (Dimens.CHART_AREA_HEIGHT
                + Dimens.LINE_AREA_HEIGHT
                + Dimens.TIME_AREA_HEIGHT
                )
        if (invokableArea == null) {
            invokableArea = RectF(l, t, w, t + h)
        } else {
            invokableArea!!.left = l
            invokableArea!!.top = t
            invokableArea!!.right = w
            invokableArea!!.bottom = t + h
        }
    }

    private fun locateIndex(x: Float): Int {
        val wUnit = width / mData.items.size
        return (x / wUnit).toInt()
    }


    /**
     * @param canvas 画笔
     * @see draw
     */
    override fun draw(canvas: Canvas) {
        // never use it
        throw UnsupportedOperationException(
            "please use IDrawable#draw(@NonNull Canvas canvas, RectF cell)"
        )
    }

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

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
