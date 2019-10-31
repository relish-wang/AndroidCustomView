package wang.relish.widget.sample.sample

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_base_display.*
import wang.relish.widget.sample.R

/**
 * @author wangxin
 * @since 20191031
 */
abstract class BaseDisplayActivity : AppCompatActivity() {

    /**
     * 自定义View
     */
    abstract fun getCustomView(): View

    /**
     * 刷新自定义View的数据(重新随机生成数据)
     */
    abstract fun update(v: View)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base_display)

        ivUpdate.setOnClickListener { update(it) }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val screenWidth = getScreenWidth()
                val params = getCustomView().layoutParams
                params.width =
                    (screenWidth.toFloat() * progress.toFloat() / 100F).toInt()
                getCustomView().layoutParams = params
                tvPercent.text = String.format("%d%%", progress)
            }
        })
    }

    override fun setContentView(layoutResID: Int) {
        val v = LayoutInflater.from(this).inflate(layoutResID, sv, false)
        sv.addView(v)
    }

    override fun setContentView(view: View?) {
        sv.addView(view)
    }


    fun getScreenWidth(): Int {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }
}