package wang.relish.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import kotlin.math.abs

/**
 * @author wangxin
 * @since 20191030
 */
abstract class IView<Drawable : IDrawable<Data>, Data> : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)


    /**
     * View高度占View宽度的比例
     */
    protected abstract fun totalHeightRatio(): Float

    /**
     * 新建自定义Drawable
     * 建议子类重写(反射效率较低)
     */
    open fun newDrawable(data: Data): Drawable? {
        try {
            @Suppress("UNCHECKED_CAST") val entityClass = (javaClass
                .genericSuperclass as ParameterizedType)
                .actualTypeArguments[0] as Class<Drawable>

            @Suppress("UNCHECKED_CAST") val constructor = entityClass
                .declaredConstructors[0] as Constructor<Drawable>
            constructor.isAccessible = true
            return constructor.newInstance(data)
        } catch (e: InstantiationException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            return null
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            return null
        }

    }

    internal var mDrawable: Drawable? = null
    /**
     * 更新数据
     */
    open fun update(data: Data) {
        if (mDrawable != null) {
            mDrawable!!.update(data)
        } else {
            mDrawable = newDrawable(data)
        }
        invalidate()
    }

    internal var width = -1f

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
        mDrawable!!.setBounds(0, 0, width.toInt(), (width * totalHeightRatio()).toInt())
        if (abs(width - 1f) <= ZERO_BIGGER) return
        mDrawable!!.draw(canvas)
    }

    companion object {
        const val ZERO_BIGGER = 0.000_001F
    }
}
