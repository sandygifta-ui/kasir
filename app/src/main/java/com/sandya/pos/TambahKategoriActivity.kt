package com.sandya.pos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase

class TambahKategoriActivity : AppCompatActivity() {

    private lateinit var etNamaKategori: TextInputEditText
    private lateinit var btnSimpanKategori: MaterialButton
    private lateinit var toolbar: Toolbar
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_kategori)

        database = FirebaseDatabase.getInstance()

        toolbar = findViewById(R.id.toolbarTambahKategori)
        etNamaKategori = findViewById(R.id.etNamaKategori)
        btnSimpanKategori = findViewById(R.id.btnSimpanKategori)

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        btnSimpanKategori.setOnClickListener {
            simpanKategoriKeFirebase()
        }
    }

    private fun simpanKategoriKeFirebase() {
        val namaKategori = etNamaKategori.text.toString().trim()

        if (namaKategori.isEmpty()) {
            etNamaKategori.error = "Nama kategori tidak boleh kosong!"
            return
        }

        // Membuat sangkutan/node baru di Firebase dengan nama "Kategori"
        val ref = database.getReference("Kategori")
        val kategoriId = ref.push().key

        if (kategoriId != null) {
            ref.child(kategoriId).setValue(namaKategori)
                .addOnSuccessListener {
                    Toast.makeText(this, "Kategori $namaKategori berhasil ditambah!", Toast.LENGTH_SHORT).show()
                    finish() // Menutup halaman otomatis setelah sukses menyimpan
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menyimpan: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}