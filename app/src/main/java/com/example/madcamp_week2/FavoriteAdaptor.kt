import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

class FavoriteAdapter(private var itemList: List<String>) :
    RecyclerView.Adapter<FavoriteAdapter.Holder>() {

    interface OnItemClickListener {
        fun onCardViewClick(view: View, favoriteItem: String, pos: Int)
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
        val favoriteItem = itemList[position]
        holder.bind(favoriteItem, position)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val favoriteName: TextView = itemView.findViewById(R.id.favorite_name_TV)
        private val cardView: CardView = itemView.findViewById(R.id.favorite_view_CV)

        fun bind(favoriteItem: String, position: Int) {
            favoriteName.text = favoriteItem

            cardView.setOnClickListener {
                listener?.onCardViewClick(itemView, favoriteItem, position)
            }
        }
    }
}
