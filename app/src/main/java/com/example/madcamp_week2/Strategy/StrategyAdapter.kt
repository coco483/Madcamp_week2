// StrategyAdapter.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.MyLanguage.Strategy
import com.example.madcamp_week2.R
import com.example.madcamp_week2.StockDetailFragment
import com.example.madcamp_week2.Strategy.StrategyFragment
import com.example.madcamp_week2.Strategy.TagAdapter

class StrategyAdapter(private var itemList: MutableList<Strategy>, val onclick: (Int) -> Int) :
    RecyclerView.Adapter<StrategyAdapter.Holder>() {

    interface OnItemClickListener {
        fun onCardViewClick(view: View, strategyItem: Strategy, pos: Int)
    }

    fun updateStrategyList(newStrategyList: List<Strategy>) {
        itemList.clear()
        itemList.addAll(newStrategyList)
        notifyDataSetChanged()
    }

    fun addStrategyItem(strategyItem: Strategy) {
        itemList.add(strategyItem)
        notifyItemInserted(itemList.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.strategy_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val strategyItem = itemList[position]
        holder.bind(strategyItem, position) // position + 1을 넘겨줘서 순번을 설정
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val strategyTitle: TextView = itemView.findViewById(R.id.strategylist_content_TV)
        private val strategyTagRecyclerView: RecyclerView =
            itemView.findViewById(R.id.strategylist_tag_RV)
        private val cardView: CardView = itemView.findViewById(R.id.strategylist_view_CV)
        private val itemNumber: TextView = itemView.findViewById(R.id.strategy_number_TV) // 순번을 표시할 TextView

        fun bind(strategyItem: Strategy, position: Int) {
            strategyTitle.text = strategyItem.title

            // 순번 설정
            val numbering =  (position+1).toString()
            itemNumber.text = numbering

            // 태그 RecyclerView 설정
            strategyTagRecyclerView.apply {
                layoutManager =
                    LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = TagAdapter(strategyItem.related_stockID)
            }

            cardView.setOnClickListener {
                onclick(position)
            }
        }
    }
}
