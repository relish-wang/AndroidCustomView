package wang.relish.widget.sample.sample.jointpillar

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_joint_pillar.*
import wang.relish.widget.sample.R
import wang.relish.widget.sample.sample.BaseDisplayActivity
import java.util.*

class JointPillarActivity : BaseDisplayActivity() {

    override fun getCustomView(): View {
        return jpv
    }

    override fun update(v: View) {
        val newData = generate()
        jpv.update(newData)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joint_pillar)

        val data = generate()
        jpv.update(data)
    }


    companion object {

        private val RANDOM = Random()

        fun generate(): Float {
            return RANDOM.nextFloat()
        }
    }
}
