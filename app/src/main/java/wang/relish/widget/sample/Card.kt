package wang.relish.widget.sample

import androidx.annotation.DrawableRes

/**
 * @author wangxin
 * @since 20191030
 */
data class Card(
    @DrawableRes val img: Int,
    val title: String,
    val subtitle: String = ""
)