package wang.relish.widget.multibar

import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * @author wangxin
 * @since 20191030
 */
interface IMultiPoints : Parcelable{

    /**
     * 最小值
     */
    @FloatRange(from = 0.0)
    fun min(): Float

    /**
     * 最大值
     */
    fun max(): Float

    /**
     * 中心颜色
     */
    @ColorInt
    fun centerColor(): Int

    /**
     * 数据
     */
    fun endPoints(): List<IEndpoint>
}
