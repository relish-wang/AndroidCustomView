package wang.relish.widget.sample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * @author wangxin
 * @since 20191030
 */
class WaterFallAdapter : RecyclerView.Adapter<WaterFallAdapter.CardHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardHolder(v)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class CardHolder(v: View) : RecyclerView.ViewHolder(v)
}