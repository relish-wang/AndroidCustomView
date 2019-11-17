package wang.relish.widget.sample.sample.surface

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.math.PI
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * @author relish
 * @since 20191117
 */
class SinSurfaceView : SurfaceView, SurfaceHolder.Callback, Runnable {

    private lateinit var mHolder: SurfaceHolder
    private var mCanvas: Canvas? = null
    // 子线程标志位
    private var mIsDrawing: Boolean = false

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initView()
    }

    private fun initView() {
        mHolder = holder
        mHolder.addCallback(this)
        isFocusable = true
        isFocusableInTouchMode = true
        keepScreenOn = true
        // mHolder.setFormat(PixelFormat.OPAQUE)
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10F
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = true
        ThreadPool.DRAW.execute(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mIsDrawing = false
    }


    private var x = 0
    private var y = 0
    private val mPath: Path = Path()
    private val mPaint: Paint = Paint()

    override fun run() {
        while (mIsDrawing) {
            draw()
            x++
            y = (100 * sin(x * 2 * PI / 180F) + 400).roundToInt()
            mPath.lineTo(x.toFloat(), y.toFloat())
        }
    }

    private fun draw() {
        try {
            mCanvas = mHolder.lockCanvas()
            // draw sth.
            mCanvas!!.drawColor(Color.WHITE)
            mCanvas!!.drawPath(mPath, mPaint)
        } catch (e: Exception) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas)
            }
        }
    }

}