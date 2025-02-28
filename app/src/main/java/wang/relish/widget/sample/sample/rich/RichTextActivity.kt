package wang.relish.widget.sample.sample.rich

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_rich_text.text
import wang.relish.widget.sample.R
import wang.relish.widget.sample.util.dp

class RichTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rich_text)
        text.text = generateTitleSpan("giao", "1")
    }

    private fun generateTitleSpan(title: String, link: String): CharSequence {
        return if (link == null || TextUtils.isEmpty(link.trim())) {
            title
        } else {
            val linkColor = ContextCompat.getColor(this, R.color.link_color)
            val drawable =
                (ContextCompat.getDrawable(this, R.drawable.ic_search)
                    ?: ColorDrawable(linkColor)).apply {
                    setBounds(2.dp, 4.dp, 12.dp, 14.dp)
                }
            SpannableStringBuilder().apply {
                append(title)
                setSpan(
                    ForegroundColorSpan(linkColor),
                    0,
                    title.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                append(" ") // 添加一个空格作为图片占位符
                val imageSpan = TopAlignedImageSpan(drawable)
                setSpan(
                    imageSpan,
                    length - 1,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }
}