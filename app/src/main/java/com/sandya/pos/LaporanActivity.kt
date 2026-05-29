package com.sandya.pos

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class LaporanActivity : AppCompatActivity() {

    private lateinit var btnBackLaporan: ImageButton
    private lateinit var rvLaporan: RecyclerView
    private lateinit var tvTotalTransaksi: TextView
    private lateinit var tvTotalPendapatan: TextView
    private lateinit var adapter: LaporanAdapter

    companion object {
        // 🌟 LIST GLOBAL LAPORAN: Dimulai dari kosong, terisi otomatis tiap kali sukses bayar di HP!
        val listLaporanGlobal = ArrayList<ItemTransaksi>()

        class ItemTransaksi(
            val noNota: String,
            val waktu: String,
            val totalBayar: String,
            val detailProduk: String
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan)

        btnBackLaporan = findViewById(R.id.btnBackLaporan)
        rvLaporan = findViewById(R.id.rvLaporan)
        tvTotalTransaksi = findViewById(R.id.tvTotalTransaksi)
        tvTotalPendapatan = findViewById(R.id.tvTotalPendapatan)

        btnBackLaporan.setOnClickListener { finish() }

        // Setup RecyclerView
        adapter = LaporanAdapter(listLaporanGlobal)
        rvLaporan.layoutManager = LinearLayoutManager(this)
        rvLaporan.adapter = adapter

        updateRingkasanLaporan()
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
        updateRingkasanLaporan()
    }

    // Fungsi menghitung total transaksi dan total uang masuk secara otomatis di HP
    private fun updateRingkasanLaporan() {
        tvTotalTransaksi.text = listLaporanGlobal.size.toString()

        var totalUang = 0
        for (item in listLaporanGlobal) {
            val angkaMurni = item.totalBayar.replace("Rp", "").replace(".", "").trim()
            val hargaInt = angkaMurni.toIntOrNull() ?: 0
            totalUang += hargaInt
        }
        tvTotalPendapatan.text = "Rp$totalUang"
    }

    private inner class LaporanAdapter(private val list: List<ItemTransaksi>) :
        RecyclerView.Adapter<LaporanAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvNama: TextView = view.findViewById(android.R.id.text1)
            val tvDetail: TextView = view.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.tvNama.text = "Nota ${item.noNota} — ${item.waktu}"
            holder.tvNama.setTextColor(Color.parseColor("#2D3142"))
            holder.tvNama.textSize = 14f

            holder.tvDetail.text = "${item.detailProduk}\nTOTAL: ${item.totalBayar}"
            holder.tvDetail.setTextColor(Color.parseColor("#9AA7B5"))
            holder.tvDetail.textSize = 13f
        }

        override fun getItemCount(): Int = list.size
    }
}