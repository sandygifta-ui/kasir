package com.sandya.pos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TambahPelangganActivity : AppCompatActivity() {

    private lateinit var btnBackInput: ImageButton
    private lateinit var etNama: EditText
    private lateinit var etHp: EditText
    private lateinit var etAlamat: EditText
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)

        btnBackInput = findViewById(R.id.btnBackInput)
        etNama = findViewById(R.id.etNama)
        etHp = findViewById(R.id.etHp)
        etAlamat = findViewById(R.id.etAlamat)
        btnSimpan = findViewById(R.id.btnSimpan)

        btnBackInput.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hp = etHp.text.toString().trim()
            val alamat = etAlamat.text.toString().trim()

            if (nama.isEmpty()) {
                etNama.error = "Nama wajib diisi"
                return@setOnClickListener
            }
            if (hp.isEmpty()) {
                etHp.error = "Nomor HP wajib diisi"
                return@setOnClickListener
            }
            if (alamat.isEmpty()) {
                etAlamat.error = "Alamat wajib diisi"
                return@setOnClickListener
            }

            PelangganActivity.pelangganList.add(
                PelangganActivity.Pelanggan(nama, hp, alamat)
            )

            setResult(RESULT_OK)
            Toast.makeText(this, "$nama berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}