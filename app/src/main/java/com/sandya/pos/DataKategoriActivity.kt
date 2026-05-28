package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sandya.pos.adapter.DataKategoriAdapter

class DataKategoriActivity : AppCompatActivity(), DataKategoriAdapter.OnItemClickListener {

    private lateinit var rvKategori: RecyclerView
    private lateinit var adapterKategori: DataKategoriAdapter
    private lateinit var fabAddKategori: FloatingActionButton
    private val listKategori = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        // 1. Inisialisasi RecyclerView Grid Pink
        rvKategori = findViewById(R.id.rvKategori)
        rvKategori.layoutManager = GridLayoutManager(this, 2)

        tampilkanKategoriSimulasi()

        adapterKategori = DataKategoriAdapter(listKategori, this)
        rvKategori.adapter = adapterKategori

        // 2. Inisialisasi Tombol Plus (+) Ungu Bulat
        fabAddKategori = findViewById(R.id.fabAddKategori)

        // 3. Aksi ketika tombol Plus diklik -> Membuka form tambah kategori baru
        fabAddKategori.setOnClickListener {
            val intent = Intent(this, TambahKategoriActivity::class.java)
            startActivity(intent)
        }
    }

    private fun tampilkanKategoriSimulasi() {
        listKategori.clear()
        listKategori.add("Makeup Wajah")
        listKategori.add("Lip Products")
        listKategori.add("Skincare Glow")
        listKategori.add("Eye Cosmetics")
    }

    // Aksi ketika salah satu kotak kategori pink diklik -> Membuka wadah produk
    override fun onItemClick(kategori: String) {
        val intent = Intent(this, DaftarProdukActivity::class.java)
        intent.putExtra("NAMA_KATEGORI", kategori)
        startActivity(intent)
    }
}