package com.sandya.pos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sandya.pos.R

class DataKategoriAdapter(
    private val listKategori: List<String>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DataKategoriAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(kategori: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaKategori: TextView = view.findViewById(R.id.tvNamaKategori)
        val ivIconKategori: ImageView = view.findViewById(R.id.ivIconKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_kategori, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listKategori[position]
        holder.tvNamaKategori.text = item
        holder.ivIconKategori.setImageResource(android.R.drawable.ic_menu_gallery)

        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun getItemCount(): Int = listKategori.size
}