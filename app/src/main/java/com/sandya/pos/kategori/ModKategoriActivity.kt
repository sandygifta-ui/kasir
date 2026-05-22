package com.sandya.pos

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class ModKategoriActivity : AppCompatActivity() {

    // Deklarasi Views
    private lateinit var btnBack: ImageButton
    private lateinit var etNamaKategori: TextInputEditText
    private lateinit var spinnerStatus: Spinner
    private lateinit var btnSimpan: MaterialButton
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("kategori")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mod_kategori)

        initViews()

        setupSpinner()

        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        etNamaKategori = findViewById(R.id.etNamaKategori)
        spinnerStatus = findViewById(R.id.spinnerStatus)
        btnSimpan = findViewById(R.id.btnSimpan)
    }

    private fun setupSpinner() {
        val statusList = arrayOf("Aktif", "Tidak Aktif")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            statusList
        )

        // Set dropdown layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapter ke spinner
        spinnerStatus.adapter = adapter

        // Set default selection (Aktif)
        spinnerStatus.setSelection(0)

        // Optional: Listener ketika item dipilih
        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedStatus = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun setupListeners() {
        btnBack.setOnClickListener {
            finish()
        }

        // Simpan button click listener
        btnSimpan.setOnClickListener {
            simpanKategori()
        }
    }

    private fun simpanKategori() {
        // Ambil nilai dari EditText
        val namaKategori = etNamaKategori.text.toString().trim()

        // Ambil nilai dari Spinner
        val statusKategori = spinnerStatus.selectedItem.toString()

        // Validasi input
        if (namaKategori.isEmpty()) {
            etNamaKategori.error = "Nama kategori harus diisi"
            etNamaKategori.requestFocus()
            return
        }

        // Proses penyimpanan data
        // TODO: Simpan ke database atau kirim ke API

        // Tampilkan pesan sukses
        Toast.makeText(
            this,
            "Kategori berhasil disimpan!\n\nNama: $namaKategori\nStatus: $statusKategori",
            Toast.LENGTH_LONG
        ).show()

        // Clear form setelah berhasil
        clearForm()

        // Optional: Kembali ke halaman sebelumnya
        // finish()
    }

    private fun clearForm() {
        // Kosongkan EditText
        etNamaKategori.text?.clear()

        // Reset Spinner ke posisi awal
        spinnerStatus.setSelection(0)

        // Clear focus
        etNamaKategori.clearFocus()
    }
}