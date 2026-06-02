package com.sandya.pos

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DataKategoriActivity : AppCompatActivity() {

    private lateinit var btnBackKategori: ImageButton
    private lateinit var fabTambahKategori: FloatingActionButton
    private lateinit var gridKategori: GridLayout

    private val warnaPastel = listOf(
        "#FFD1DC", "#FAF0FF", "#E8AEFF", "#A0C4FF",
        "#B5EAD7", "#FFDAC1", "#FF9AA2", "#C7CEEA"
    )

    private val tambahKategoriLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            renderKategori()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        btnBackKategori = findViewById(R.id.btnBackInput)
        fabTambahKategori = findViewById(R.id.fabTambahKategori)
        gridKategori = findViewById(R.id.gridKategori)

        btnBackKategori.setOnClickListener { finish() }

        fabTambahKategori.setOnClickListener {
            tambahKategoriLauncher.launch(
                Intent(this, ModKategoriActivity::class.java)
            )
        }

        renderKategori()
    }

    override fun onResume() {
        super.onResume()
        renderKategori()
    }

    private fun renderKategori() {
        gridKategori.removeAllViews()

        val cardHeight = resources.getDimensionPixelSize(android.R.dimen.app_icon_size) * 3

        KategoriManager.daftarKategori.forEachIndexed { index, kategori ->
            val card = CardView(this)
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = cardHeight
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(16, 16, 16, 16)
            card.layoutParams = params
            card.radius = 32f
            card.cardElevation = 8f
            card.setCardBackgroundColor(
                Color.parseColor(warnaPastel[index % warnaPastel.size])
            )

            val tv = TextView(this)
            tv.text = kategori.nama
            tv.textSize = 18f
            tv.setTextColor(Color.parseColor("#2D3142"))
            tv.setTypeface(null, Typeface.BOLD)
            tv.gravity = Gravity.CENTER
            tv.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )

            card.addView(tv)

            card.setOnClickListener {
                val intent = Intent(this, DaftarProdukActivity::class.java)
                intent.putExtra("KATEGORI_FILTER", kategori.nama)
                startActivity(intent)
            }

            gridKategori.addView(card)
        }
    }
}