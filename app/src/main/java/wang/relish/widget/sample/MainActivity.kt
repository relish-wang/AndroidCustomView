package wang.relish.widget.sample

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import wang.relish.widget.sample.chore.Card
import wang.relish.widget.sample.chore.SpaceItemDecoration
import wang.relish.widget.sample.chore.WaterFallAdapter
import wang.relish.widget.sample.chore.dp2px
import wang.relish.widget.sample.sample.chart.ChartActivity
import wang.relish.widget.sample.sample.multibar.MultiBarActivity

class MainActivity : AppCompatActivity() {

    /**
     * 通过获取屏幕宽度来计算出每张图片的宽度。
     *
     * @return 计算后得出的每张图片的宽度。
     */
    private val imageWidth: Int
        get() {
            val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay?.getMetrics(metrics)
            val columnWidth = metrics.widthPixels / COLUMN_COUNT
            return columnWidth - dp2px(this, 18f)
        }

    private lateinit var mAdapter: WaterFallAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "自定义View合集"

        mAdapter = WaterFallAdapter(arrayListOf(), imageWidth)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(COLUMN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(SpaceItemDecoration())
        recyclerView.setHasFixedSize(true)
    }


    override fun onResume() {
        super.onResume()
        /*
         *  Dispatchers.Default
         *  Dispatchers.IO -
         *  Dispatchers.Main - 主线程
         *  Dispatchers.Unconfined - 没指定，就是在当前线程
         */
        runBlocking {
            GlobalScope.launch(Dispatchers.Main) {
                val cards = GlobalScope.async {
                    return@async getCards()
                }.await()
                mAdapter.setNewData(cards)
            }
        }
    }

    companion object {
        private const val COLUMN_COUNT = 2
        fun getCards(): MutableList<Card> {
            return arrayListOf(
                Card(
                    ChartActivity::class.java,
                    R.drawable.ic_cover_chart,
                    "二阶贝塞尔曲线图",
                    "运用二阶贝塞尔曲线创建Path, 结合线性渐变(LinearGradient, 用于绘制曲线图底部渐变背景), 完成曲线图的绘制。",
                    600,
                    556
                ),
                Card(
                    MultiBarActivity::class.java,
                    R.drawable.ic_cover_chart,
                    "多根Bar",
                    "从中心散射开的多根带进度的Bar",
                    600,
                    556
                )
            )
        }
    }
}
