package wang.relish.widget.multibar


import android.content.Context
import android.util.AttributeSet
import wang.relish.widget.IView

/**
 * 三根bar自定义View
 *
 * @author wangxin
 * @since 20191028
 */
class MultiBarView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : IView<MultiBarDrawable<IThreePoints>, IThreePoints>(
    context,
    attrs,
    defStyleAttr
) {

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun totalHeightRatio(): Float {
        return MultiBarDrawable.TOTAL_HEIGHT
    }

    override fun newDrawable(data: IThreePoints): MultiBarDrawable<IThreePoints> {
        return MultiBarDrawable(data)
    }

}
