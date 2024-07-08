import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R
import com.example.madcamp_week2.Class.Stock

class FavoriteAdapter(private var itemList: List<Stock>) :
    RecyclerView.Adapter<FavoriteAdapter.Holder>() {

    interface OnItemClickListener {
        fun onCardViewClick(view: View, stock: Stock, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_list, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val stock = itemList[position]
        holder.bind(stock, position)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val marketImageView: ImageView = itemView.findViewById(R.id.favorite_market_IV)
        private val favoriteNameTextView: TextView = itemView.findViewById(R.id.favorite_name_TV)
        private val favoriteMarketTextView: TextView = itemView.findViewById(R.id.favorite_market_TV) // Corrected from favorite_market_IV
        private val cardView: CardView = itemView.findViewById(R.id.favorite_view_CV)

        fun bind(stock: Stock, position: Int) {
            favoriteNameTextView.text = stock.name
            favoriteMarketTextView.text = stock.market
            Log.d("FavoriteAdapter", "Market: ${stock.market}")
            Log.d("FavoriteAdapter", "Name: ${stock.name}")

            // Determine which logo to set based on the market
            when (stock.market) {
                "KOSPI" -> marketImageView.setImageResource(R.drawable.kospi_logo)
                "KOSDAQ" -> marketImageView.setImageResource(R.drawable.kosdaq_logo)
                else -> marketImageView.setImageResource(R.drawable.other_logo) // Clear the ImageView
            }

            // Handle click event
            cardView.setOnClickListener {
                listener?.onCardViewClick(itemView, stock, position)
            }
        }
    }
}
