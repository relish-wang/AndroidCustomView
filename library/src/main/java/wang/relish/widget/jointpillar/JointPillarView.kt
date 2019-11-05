package wang.relish.widget.jointpillar

import android.content.Context
import android.util.AttributeSet
import android.view.View

import androidx.annotation.FloatRange

import wang.relish.widget.IView


/**
 * 节柱
 *
 * @author wangxin
 * @since 20191030
 */
class JointPillarView : IView<JointPillarDrawable, Float> {

    var mListener: OnValueUpdateListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs!!)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs!!,
        defStyleAttr
    )

    override fun totalHeightRatio(): Float {
        return JointPillarDrawable.TOTAL_HEIGHT
    }

    override fun newDrawable(data: Float): JointPillarDrawable? {
        return JointPillarDrawable(data)
    }

    override fun update(@FloatRange(from = 0.0, to = 1.0) data: Float) {
        super.update(data)
        if (mListener != null) {
            val percent:Float = data
            mListener!!.onValueUpdated(this, percent, JointPillarDrawable.percentToColor(percent))
        }
    }

    fun setOnValueUpdateListener(l: OnValueUpdateListener) {
        mListener = l
    }

    interface OnValueUpdateListener {
        fun onValueUpdated(v: View, percent: Float, color: Int)
    }
}
