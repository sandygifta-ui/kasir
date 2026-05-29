package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PembayaranActivity : AppCompatActivity() {

    private lateinit var tvTagihanFinal: TextView
    private lateinit var rgMetode: RadioGroup
    private lateinit var btnBayarSekarang: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        tvTagihanFinal = findViewById(R.id.tvTagihanFinal)
        rgMetode = findViewById(R.id.rgMetode)
        btnBayarSekarang = findViewById(R.id.btnBayarSekarang)

        val total = intent.getIntExtra("TOTAL_HARGA", 0)
        val keranjang = intent.getStringArrayListExtra("KERANJANG")

        tvTagihanFinal.text = "Rp$total"

        // Pilih metode pertama secara default agar tidak crash
        if (rgMetode.checkedRadioButtonId == -1) {
            val firstRadio = rgMetode.getChildAt(0)
            if (firstRadio is RadioButton) firstRadio.isChecked = true
        }

        btnBayarSekarang.setOnClickListener {
            val idTerpilih = rgMetode.checkedRadioButtonId
            if (idTerpilih == -1) {
                Toast.makeText(this, "Pilih metode pembayaran dahulu!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rbTerpilih = findViewById<RadioButton>(idTerpilih)
            val metode = rbTerpilih?.text?.toString() ?: "Tunai"

            // Simpan ke laporan global
            val noNota = "TRX-" + (1000..9999).random()
            val waktuSekarang = java.text.SimpleDateFormat(
                "dd/MM/yyyy HH:mm", java.util.Locale("id", "ID")
            ).format(java.util.Date())
            val detailProduk = keranjang?.joinToString(", ") ?: "-"

            LaporanActivity.listLaporanGlobal.add(
                LaporanActivity.Companion.ItemTransaksi(
                    noNota, waktuSekarang, "Rp$total", detailProduk
                )
            )

            // Reset keranjang setelah bayar
            for (item in MainActivity.menuKosmetikListGlobal) {
                item.jumlahBeli = 0
            }

            val intentCetak = Intent(this, CetakStrukActivity::class.java)
            intentCetak.putExtra("TOTAL", total)
            intentCetak.putExtra("METODE", metode)
            intentCetak.putStringArrayListExtra("KERANJANG", keranjang)
            startActivity(intentCetak)
            finish()
        }
    }
}