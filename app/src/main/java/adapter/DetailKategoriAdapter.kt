package adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.sandya.pos.ModelKategori
import com.sandya.pos.R

class DetailKategoriAdapter(private val kategoriList: List<ModelKategori>) :
    RecyclerView.Adapter<DetailKategoriAdapter.KategoriViewHolder>() {

    lateinit var appContext: Context

    interface OnClickListener {
        fun onItemClick(kategori: ModelKategori)
    }

    private var listener: OnClickListener? = null

    fun setOnClickListener(listener: Any) {
        this.listener = listener as OnClickListener?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_kategori, parent, false)
        appContext = parent.context
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        holder.bind(kategoriList[position])
    }

    override fun getItemCount(): Int = kategoriList.size

    inner class KategoriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val etNamaKategori: TextView = itemView.findViewById(R.id.etNamaKategori)
        val btnStatus: Chip = itemView.findViewById(R.id.spinnerStatus) // ← tambah ini

        fun bind(kategori: ModelKategori) {
            etNamaKategori.text = kategori.namaKategori
            val status = kategori.statusKategori ?: "0"

            if (status == "1" || status == "Aktif") {
                btnStatus.text = itemView.context.getString(R.string.status_aktif)
                btnStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_active_bg)
                )
                btnStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_active_text)
                )
                btnStatus.chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_active_text)
                )
            } else {
                btnStatus.text = itemView.context.getString(R.string.status_nonaktif)
                btnStatus.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_inactive_bg)
                )
                btnStatus.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.status_inactive_text)
                )
                btnStatus.chipStrokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.status_inactive_text)
                )
            }

            itemView.setOnClickListener {
                listener?.onItemClick(kategori)
            }
        }
    }
}