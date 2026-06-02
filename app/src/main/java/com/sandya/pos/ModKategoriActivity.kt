package com.sandya.pos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class ModKategoriActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var etNamaKategori: TextInputEditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnSimpan: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_kategori)

        btnBack = findViewById(R.id.btnBack)
        etNamaKategori = findViewById(R.id.etNamaKategori)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnSimpan = findViewById(R.id.btnSimpan)

        val adapterStatus = ArrayAdapter(this,
            android.R.layout.simple_spinner_item,
            arrayOf("Aktif", "Tidak Aktif"))
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapterStatus

        btnBack.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            val namaKategori = etNamaKategori.text.toString().trim()
            val statusKategori = spinnerStatus.selectedItem.toString()

            if (namaKategori.isEmpty()) {
                etNamaKategori.error = "Nama kategori tidak boleh kosong"
                return@setOnClickListener
            }

            if (KategoriManager.daftarKategori.any {
                    it.nama.equals(namaKategori, ignoreCase = true)
                }) {
                etNamaKategori.error = "Kategori sudah ada"
                return@setOnClickListener
            }

            KategoriManager.daftarKategori.add(
                KategoriManager.Kategori(namaKategori, statusKategori)
            )

            Toast.makeText(this,
                "Kategori \"$namaKategori\" berhasil ditambahkan!",
                Toast.LENGTH_SHORT).show()

            setResult(RESULT_OK)
            finish()
        }
    }
}