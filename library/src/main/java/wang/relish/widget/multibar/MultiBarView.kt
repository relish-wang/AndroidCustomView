package wang.relish.widget.multibar


import android.content.Context
import android.util.AttributeSet
import wang.relish.widget.IView

/**
 * 多根bar
 *
 * @author wangxin
 * @since 20191028
 */
class MultiBarView : IView<MultiBarDrawable, IMultiPoints> {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    override fun totalHeightRatio(): Float {
        return MultiBarDrawable.TOTAL_HEIGHT
    }

    override fun newDrawable(data: IMultiPoints): MultiBarDrawable {
        return MultiBarDrawable(data)
    }

}
