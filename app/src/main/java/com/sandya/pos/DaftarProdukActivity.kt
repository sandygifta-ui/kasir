package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DaftarProdukActivity : AppCompatActivity() {

    private lateinit var btnBackProduk: ImageButton
    private lateinit var rvDaftarProduk: RecyclerView
    private lateinit var fabTambahProduk: FloatingActionButton
    private lateinit var adapter: ProdukAdapter
    private lateinit var kategoriFilter: String
    private val listFiltered = ArrayList<PilihProdukActivity.Companion.MenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_produk)

        btnBackProduk = findViewById(R.id.btnBackInput)
        rvDaftarProduk = findViewById(R.id.rvDaftarProduk)
        fabTambahProduk = findViewById(R.id.fabTambahProduk)

        kategoriFilter = intent.getStringExtra("KATEGORI_FILTER") ?: ""

        btnBackProduk.setOnClickListener { finish() }

        fabTambahProduk.setOnClickListener {
            val intent = Intent(this, TambahKategoriActivity::class.java)
            intent.putExtra("KATEGORI_DEFAULT", kategoriFilter)
            startActivity(intent)
        }

        adapter = ProdukAdapter(listFiltered)
        rvDaftarProduk.layoutManager = LinearLayoutManager(this)
        rvDaftarProduk.adapter = adapter

        refreshList()
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        listFiltered.clear()
        if (kategoriFilter.isEmpty()) {
            listFiltered.addAll(MainActivity.menuKosmetikListGlobal)
        } else {
            listFiltered.addAll(
                MainActivity.menuKosmetikListGlobal.filter {
                    it.kategori.trim().equals(kategoriFilter.trim(), ignoreCase = true)
                }
            )
        }
        adapter.notifyDataSetChanged()
    }

    private inner class ProdukAdapter(
        private val list: List<PilihProdukActivity.Companion.MenuItem>
    ) : RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {

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
            holder.tvNama.text = item.namaMenu
            holder.tvDetail.text = "Kategori: ${item.kategori}  •  Rp${"%,d".format(item.harga)}"
        }

        override fun getItemCount(): Int = list.size
    }
}