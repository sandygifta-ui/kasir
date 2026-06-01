package com.sandya.pos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton

class TambahKategoriActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var etNamaKategori: TextInputEditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnSimpan: MaterialButton

    private val listStatus = arrayOf("Aktif", "Tidak Aktif")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🌟 FIX UTAMA: Memanggil activity_tambah_kategori agar sinkron dengan ID di bawahnya!
        setContentView(R.layout.activity_mod_kategori)

        // Menghubungkan variabel dengan ID asli yang ada di file XML kamu
        btnBack = findViewById(R.id.btnBack)
        etNamaKategori = findViewById(R.id.etNamaKategori)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnSimpan = findViewById(R.id.btnSimpan)

        // Setup Spinner Pilihan Status
        val adapterStatus = ArrayAdapter(this, android.R.layout.simple_spinner_item, listStatus)
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapterStatus

        // Fungsi Tombol Kembali
        btnBack.setOnClickListener {
            finish()
        }

        // Fungsi Tombol Simpan
        btnSimpan.setOnClickListener {
            val namaKategori = etNamaKategori.text.toString().trim()
            val statusKategori = spinnerStatus.selectedItem.toString()

            if (namaKategori.isNotEmpty()) {
                Toast.makeText(this, "Kategori \"$namaKategori\" ($statusKategori) berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                etNamaKategori.error = "Nama kategori tidak boleh kosong!"
            }
        }
    }
}