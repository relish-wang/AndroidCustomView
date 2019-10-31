package wang.relish.widget.sample.chore

/**
 * @author wangxin
 * @since 20191030
 */
data class Card(
    val clazz: Class<*>,
    val cover: Any,
    val title: String,
    val subtitle: String = "",
    val imgWidth:Int = 0,
    val imgHeight:Int = 0
)