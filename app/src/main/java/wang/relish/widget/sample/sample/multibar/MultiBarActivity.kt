package wang.relish.widget.sample.sample.multibar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_multi_bar.*
import wang.relish.widget.multibar.IEndpoint
import wang.relish.widget.sample.R
import wang.relish.widget.sample.sample.BaseDisplayActivity
import java.util.*

class MultiBarActivity : BaseDisplayActivity() {

    override fun getCustomView(): View {
        return multiBarView
    }

    override fun update(v: View) {
        val newData = generate()
        multiBarView.update(newData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_bar)

        val data = generate()
        multiBarView.update(data)
    }


    companion object {

        private val RANDOM = Random()

        fun generate(value: Double = -1.0): MultiPoints {
            val count = RANDOM.nextInt(10) + 3
            val list = arrayListOf<IEndpoint>()
            for (i in 0..count) {
                list.add(generate("第${i + 1}根", value))
            }
            return MultiPoints(
                200F,
                1000F,
                Color.parseColor("#FF0000"),
                list
            )
        }

        private fun generate(name: String, value: Double = -1.0): Endpoint {
            return Endpoint(if (value <= 0) RANDOM.nextDouble() * 1000 else value, name)
        }
    }
}
