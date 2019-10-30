package wang.relish.widget.sample

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_chart.*
import wang.relish.widget.graph.ChartData
import java.util.*

class ChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val data = generateData()
        ccv.update(data)

        btn_update.setOnClickListener {
            val newData = generateData()
            ccv.update(newData)
        }

        sb.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val params = ccv.layoutParams
                val screenWidth =
                    getScreenWidth(this@ChartActivity)
                params.width = (screenWidth.toFloat() * progress.toFloat() / 100.0).toInt()
                ccv.layoutParams = params
                tv_percent.text = String.format("%d%%", progress)
            }
        })
    }


    companion object {

        fun generateData(): ChartData {
            val items = arrayListOf<ChartData.Column>()
            for (i in 0..12) {
                items.add(
                    generateItem(
                        String.format(
                            "%02d",
                            i + 1
                        )
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

        fun getScreenWidth(activity: Activity): Int {
            val dm = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }
    }
}
