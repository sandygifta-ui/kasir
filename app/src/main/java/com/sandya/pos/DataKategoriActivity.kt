package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class DataKategoriActivity : AppCompatActivity() {

    private lateinit var btnBackKategori: ImageButton
    private lateinit var cardLips: CardView
    private lateinit var cardFace: CardView
    private lateinit var cardEyes: CardView
    private lateinit var cardSkincare: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        btnBackKategori = findViewById(R.id.btnBackInput)
        cardLips = findViewById(R.id.cardLips)
        cardFace = findViewById(R.id.cardFace)
        cardEyes = findViewById(R.id.cardEyes)
        cardSkincare = findViewById(R.id.cardSkincare)

        btnBackKategori.setOnClickListener { finish() }

        cardLips.setOnClickListener { bukaKategori("Lips") }
        cardFace.setOnClickListener { bukaKategori("Face") }
        cardEyes.setOnClickListener { bukaKategori("Eyes") }
        cardSkincare.setOnClickListener { bukaKategori("Skincare") }
    }

    private fun bukaKategori(kategori: String) {
        val intent = Intent(this, DaftarProdukActivity::class.java)
        intent.putExtra("KATEGORI_FILTER", kategori)
        startActivity(intent)
    }
}