package wang.relish.widget.sample.chore

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class SpaceItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val context = view.context
        val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        val lineLast = (parent.layoutManager as StaggeredGridLayoutManager).spanCount - 1
        outRect.left =
            dp2px(context, if (spanIndex == 0) 12f else 6f)
        outRect.right =
            dp2px(context, if (spanIndex == lineLast) 12f else 6f)
    }
}