package cat.fib.sharecommunity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewNomAdapter(
    private val cardViewList: List<CardViewNom>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerViewNomAdapter.RecyclerViewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cardview_nom,
            parent, false)
        return RecyclerViewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        val currentItem = cardViewList[position]

        holder.textView.text = currentItem.text
    }

    override fun getItemCount() = cardViewList.size

    inner class RecyclerViewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        val textView: TextView = itemView.findViewById(R.id.text_view)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}