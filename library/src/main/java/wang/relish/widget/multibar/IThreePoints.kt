package wang.relish.widget.multibar

/**
 * @author wangxin
 * @since 20191030
 */
interface IThreePoints {

    /**
     * 最小值
     */
    fun min(): Double

    /**
     * 最大值
     */
    fun max(): Double

    /**
     * 中心颜色
     */
    fun centerColor(): Int

    /**
     * 数据
     */
    fun endPoints(): List<IEndpoint>
}
