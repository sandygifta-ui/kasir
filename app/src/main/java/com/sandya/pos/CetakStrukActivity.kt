package com.sandya.pos

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
        sb.append("TOTAL    : Rp$total\n")
        sb.append("==============================\n")
        sb.append("   Terima Kasih Atas Kunjungan\n")
        sb.append("     Cantikmu, Semangat Kami!\n")

        teksStrukMurni = sb.toString()
        tvTeksStruk.text = teksStrukMurni

        btnCetakMesin.setOnClickListener {
            kirimKeMesinCetak(teksStrukMurni)
        }
    }

    private fun kirimKeMesinCetak(teks: String) {
        val webView = WebView(this)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
                val printAdapter = webView.createPrintDocumentAdapter("Nota_Transaksi")
                printManager.print("Struk_POS", printAdapter, PrintAttributes.Builder().build())
            }
        }
        val htmlFormat = "<html><body><pre>${teks.replace("\n", "<br>")}</pre></body></html>"
        webView.loadDataWithBaseURL(null, htmlFormat, "text/html", "UTF-8", null)
    }
}