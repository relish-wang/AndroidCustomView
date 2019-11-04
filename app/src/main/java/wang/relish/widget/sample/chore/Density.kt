package wang.relish.widget.sample.chore

import android.content.Context


/**
 * 单位转换工具类，会根据手机的分辨率来进行单位转换。
 *
 * @author wangxin
 * @since 20191031
 */

/**
 * 根据手机的分辨率将dp转成为px
 */
fun dp2px(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

/**
 * 根据手机的分辨率将px转成dp
 */
fun px2dp(context: Context,px: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}
