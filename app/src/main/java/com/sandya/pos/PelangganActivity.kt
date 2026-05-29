package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class PelangganActivity : AppCompatActivity() {

    private lateinit var btnBackInput: ImageButton
    private lateinit var rvPelanggan: RecyclerView
    private lateinit var fabTambahPelanggan: FloatingActionButton
    private lateinit var adapter: PelangganAdapter

    class Pelanggan(val nama: String, val hp: String, val alamat: String)

    companion object {
        val pelangganList = ArrayList<Pelanggan>()
    }

    private val tambahPelangganLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggan)

        btnBackInput = findViewById(R.id.btnBackInput)
        rvPelanggan = findViewById(R.id.rvPelanggan)
        fabTambahPelanggan = findViewById(R.id.fabTambahPelanggan)

        btnBackInput.setOnClickListener { finish() }

        fabTambahPelanggan.setOnClickListener {
            tambahPelangganLauncher.launch(
                Intent(this, TambahPelangganActivity::class.java)
            )
        }

        adapter = PelangganAdapter(pelangganList)
        rvPelanggan.layoutManager = LinearLayoutManager(this)
        rvPelanggan.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    private inner class PelangganAdapter(
        private val list: List<Pelanggan>
    ) : RecyclerView.Adapter<PelangganAdapter.ViewHolder>() {

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
            holder.tvNama.text = item.nama
            holder.tvDetail.text = "HP: ${item.hp}  •  ${item.alamat}"
        }

        override fun getItemCount(): Int = list.size
    }
}