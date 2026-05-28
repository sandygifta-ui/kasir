package com.sandya.pos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DaftarProdukActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var rvDaftarProduk: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_produk)

        // Inisialisasi komponen view dari XML
        toolbar = findViewById(R.id.toolbarDaftarProduk)
        rvDaftarProduk = findViewById(R.id.rvDaftarProduk)

        // Set aksi tombol kembali di toolbar atas
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        // Mengambil nama kategori yang dilempar saat kotak dipencet
        val namaKategori = intent.getStringExtra("NAMA_KATEGORI") ?: "Produk"

        // Mengubah judul toolbar secara otomatis sesuai kategori yang diklik
        supportActionBar?.title = namaKategori

        // Mengatur tampilan list produk menjadi bentuk Grid 2 kolom ke samping
        rvDaftarProduk.layoutManager = GridLayoutManager(this, 2)

        // Memunculkan notifikasi teks singkat di bawah layar
        Toast.makeText(this, "Membuat produk untuk: $namaKategori", Toast.LENGTH_SHORT).show()
    }
}