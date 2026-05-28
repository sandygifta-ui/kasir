package com.sandya.pos

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sandya.pos.R

class AccountActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var tvNamaTokoHeader: TextView
    private lateinit var tvNamaTokoDetail: TextView
    private lateinit var tvAlamatToko: TextView
    private lateinit var tvCabangToko: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initViews()
        setupDataToko()

        // Fungsi klik tombol kembali ke halaman utama
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        tvNamaTokoHeader = findViewById(R.id.tvNamaTokoHeader)
        tvNamaTokoDetail = findViewById(R.id.tvNamaTokoDetail)
        tvAlamatToko = findViewById(R.id.tvAlamatToko)
        tvCabangToko = findViewById(R.id.tvCabangToko)
    }

    private fun setupDataToko() {
        // Mengisi data identitas toko kosmetik lucumu
        tvNamaTokoHeader.text = "Bunny Blush Co."
        tvNamaTokoDetail.text = "Bunny Blush Co."
        tvAlamatToko.text = "Jl. Raya Surakarta-Sukoharjo"
        tvCabangToko.text = "Surakarta & Sukoharjo Region"
    }
}