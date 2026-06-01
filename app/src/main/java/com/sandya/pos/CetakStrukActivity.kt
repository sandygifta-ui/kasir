package com.sandya.pos

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CetakStrukActivity : AppCompatActivity() {

    private lateinit var tvTeksStruk: TextView
    private lateinit var btnCetakMesin: Button
    private var teksStrukMurni = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cetak_struk)

        tvTeksStruk = findViewById(R.id.tvTeksStruk)
        btnCetakMesin = findViewById(R.id.btnCetakMesin)

        val total = intent.getIntExtra("TOTAL", 0)
        val metode = intent.getStringExtra("METODE") ?: "Tunai"
        val keranjang = intent.getStringArrayListExtra("KERANJANG") ?: arrayListOf()
        val waktu = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("id", "ID")).format(Date())

        val sb = StringBuilder()
        sb.append("        BUNNY BLUSH CO.\n")
        sb.append("  Jl. Raya Surakarta-Sukoharjo\n")
        sb.append("==============================\n")
        sb.append("Waktu    : $waktu\n")
        sb.append("------------------------------\n")
        for (item in keranjang) {
            sb.append("$item\n")
        }
        sb.append("==============================\n")
        sb.append("Metode   : $metode\n")
        sb.append("TOTAL    : Rp${"%,d".format(total)}\n")
        sb.append("==============================\n")
        sb.append("   Terima Kasih Atas Kunjungan\n")
        sb.append("     Cantikmu, Semangat Kami!\n")

        teksStrukMurni = sb.toString()
        tvTeksStruk.text = teksStrukMurni

        btnCetakMesin.setOnClickListener {
            val outputStream = BluetoothPrinterManager.outputStream
            if (outputStream != null) {
                Thread {
                    try {
                        outputStream.write(teksStrukMurni.toByteArray(Charsets.UTF_8))
                        outputStream.write("\n\n\n\n".toByteArray())
                        runOnUiThread {
                            Toast.makeText(this, "Struk berhasil dicetak!", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: IOException) {
                        runOnUiThread {
                            Toast.makeText(this, "Gagal mencetak, cek koneksi printer", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.start()
            } else {
                Toast.makeText(this,
                    "Printer belum terhubung! Hubungkan dulu di menu Printer",
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}