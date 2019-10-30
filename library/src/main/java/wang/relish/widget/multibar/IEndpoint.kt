package wang.relish.widget.multibar

/**
 * 端点数据
 *
 * @author wangxin
 * @since 20191028
 */
interface IEndpoint {
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