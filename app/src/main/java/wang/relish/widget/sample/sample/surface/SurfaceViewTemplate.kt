package wang.relish.widget.sample.sample.surface

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * @author relish
 * @since 20191117
 */
class SurfaceViewTemplate : SurfaceView, SurfaceHolder.Callback, Runnable {

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
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = true
        // TODO thread pool？
        ThreadPool.DRAW.execute(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mIsDrawing = false
    }


    override fun run() {
        while (mIsDrawing) {
            draw()
        }
    }

    private fun draw() {
        try {
            mCanvas = mHolder.lockCanvas()
            // draw sth.
        } catch (e: Exception) {
        } finally {
            if (mCanvas != null) {
                mHolder.unlockCanvasAndPost(mCanvas)
            }
        }
    }

}