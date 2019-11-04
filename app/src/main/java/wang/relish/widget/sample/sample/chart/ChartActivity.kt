package wang.relish.widget.sample.sample.chart

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_chart.*
import wang.relish.widget.chart.ChartData
import wang.relish.widget.sample.R
import wang.relish.widget.sample.sample.BaseDisplayActivity
import java.util.*

class ChartActivity : BaseDisplayActivity() {

    override fun getCustomView(): View {
        return ccv
    }

    override fun update(v: View) {
        val newData = generateData()
        ccv.update(newData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val data = generateData()
        ccv.update(data)
    }


    companion object {
        fun generateData(): ChartData {
            val items = arrayListOf<ChartData.Column>()
            for (i in 0..12) {
                items.add(
                    generateItem(
                        String.format("%02d", i + 1)
                    )
                )
            }
            return generateItem(items)
        }

        private val r = Random()

        private fun generateItem(time: String): ChartData.Column {
            val min = 150.0 * r.nextDouble()
            val max = min + 250.0 * r.nextDouble()
            return ChartData.Column(time, min, max)
        }

        private val UNITS = arrayListOf(
            "", "㎎/m³", "ppm", "μg/m³", "℃", "%"
        )

        private fun generateItem(items: List<ChartData.Column>): ChartData {
            val i = r.nextInt(UNITS.size)
            return ChartData(0, items, UNITS[i])
        }
    }
}
