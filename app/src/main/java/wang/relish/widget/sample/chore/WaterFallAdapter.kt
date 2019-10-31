package wang.relish.widget.sample.chore

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import kotlinx.android.synthetic.main.item_card.view.*
import wang.relish.widget.sample.R


/**
 * @author wangxin
 * @since 20191030
 */
class WaterFallAdapter(
    private val mData: MutableList<Card> = arrayListOf(),
    private val imageWidth: Int = 0
) : RecyclerView.Adapter<WaterFallAdapter.CardHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardHolder(v)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val card = mData[position]
        val imageHeight = calculateImageHeight(card)
        holder.itemView.iv_cover.layoutParams.width = imageWidth
        holder.itemView.iv_cover.layoutParams.height = imageHeight
        Glide.with(holder.itemView.context)
            .load(card.cover)
            .override(imageWidth, imageHeight)
            .placeholder(android.R.mipmap.sym_def_app_icon)
            .priority(Priority.IMMEDIATE)
            .into(holder.itemView.iv_cover)
        holder.itemView.tv_title.text = card.title
        holder.itemView.tv_subtitle.text = card.subtitle

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, card.clazz)
            it.context.startActivity(intent)
        }
    }

    fun setNewData(cards: MutableList<Card>) {
        if (mData != cards) {
            mData.clear()
            mData.addAll(cards)
        }
        notifyDataSetChanged()
    }

    class CardHolder(v: View) : RecyclerView.ViewHolder(v)

    private fun calculateImageHeight(feed: Card): Int {
        val originalWidth = feed.imgWidth
        val originalHeight = feed.imgHeight
        return imageWidth * originalHeight / originalWidth
    }
}