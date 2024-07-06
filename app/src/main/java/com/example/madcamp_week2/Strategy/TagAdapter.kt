// TagAdapter.kt
package com.example.madcamp_week2.Strategy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_week2.R

class TagAdapter(private val tagList: List<String>) : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    var onTagClickListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tag_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tagList[position])
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tagTextView: TextView = itemView.findViewById(R.id.tag_content_TV)

        fun bind(tag: String) {
            tagTextView.text = tag
            itemView.setOnClickListener {
                onTagClickListener?.invoke(tag)
            }
        }
    }
}
