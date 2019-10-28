package wang.relish.widget.graph

import java.io.Serializable

/**
 * 曲线图数据
 * @author relish
 * @since 20191010
 */
data class ChartData(
    /** 保留小数位数 */
    val round: Int,
    /** 曲线图数据 */
    val items: List<Column>,
    /** 单位  */
    val unit: String = ""
) : Serializable {
    companion object {
        private const val serialVersionUID = 1L
    }

    fun getText(value: Double): String {
        return String.format("%.${round}f", value)
    }

    data class Column(
        val time: String,
        val min: Double,
        val max: Double
    ) : Serializable {
        companion object {
            private const val serialVersionUID = 1L
        }
    }
}