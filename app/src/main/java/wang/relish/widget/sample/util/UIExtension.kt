package wang.relish.widget.sample.util

import android.content.res.Resources
import android.util.TypedValue

/**
 * sp to float value
 */
inline val Float.spF: Float
    get() {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                this,
                Resources.getSystem().displayMetrics)
    }

/**
 * sp to int value
 */
inline val Int.sp: Int get() = toFloat().sp

/**
 * sp to int value
 */
inline val Float.sp: Int get() = spF.toInt()

/**
 * dp to float value
 */
inline val Float.dpF: Float
    get() {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                this,
                Resources.getSystem().displayMetrics)
    }

/**
 * dp to float value
 */
inline val Int.dpF: Float get() = toFloat().dpF

/**
 * dp to int value
 */
inline val Int.dp: Int get() = toFloat().dp

/**
 * dp to int value
 */
inline val Float.dp: Int get() = dpF.toInt()

/**
 *px2dp
 */
inline val Float.px2dpF: Float get() = this / Resources.getSystem().displayMetrics.density + 0.5f

/**
 *px2dp
 */
inline val Float.px2dp: Int get() = px2dpF.toInt()

/**
 *px2dp
 */
inline val Int.px2dpF: Float get() = toFloat().px2dpF

/**
 *px2dp
 */
inline val Int.px2dp: Int get() = px2dpF.toInt()
