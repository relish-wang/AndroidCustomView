package wang.relish.widget.multibar

import android.os.Parcelable

/**
 * 端点数据
 *
 * @author wangxin
 * @since 20191028
 */
interface IEndpoint :Parcelable {
    /**
     * 端点数值(单位: mm)
     */
    fun value(): Double

    /**
     * 端点名称
     *
     * @return eg: "前侧","后中侧","后外侧"
     */
    fun name(): String
}