package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvEstimation: TextView
    private lateinit var cardTambahan: CardView
    private lateinit var cardPegawai: CardView
    private lateinit var cardCabang: CardView
    private lateinit var cardPrinter: CardView
    private lateinit var cardAkun: CardView
    private lateinit var cardLayanan: CardView
    private lateinit var menuTransaksi: LinearLayout
    private lateinit var menuPelanggan: LinearLayout
    private lateinit var menuLaporan: LinearLayout

    companion object {
        @JvmStatic
        val menuKosmetikListGlobal = ArrayList<PilihProdukActivity.Companion.MenuItem>().apply {
            add(PilihProdukActivity.Companion.MenuItem("MK-001", "Lipstik Velvet", "Lips", 85000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-002", "Lip Tint Cherry", "Lips", 65000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-003", "Foundation Matte", "Face", 120000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-004", "Bedak Loose", "Face", 75000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-005", "Mascara Volume", "Eyes", 90000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-006", "Eyeliner Pen", "Eyes", 55000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-007", "Sunscreen SPF50", "Skincare", 95000, null))
            add(PilihProdukActivity.Companion.MenuItem("MK-008", "Moisturizer Gel", "Skincare", 110000, null))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupGreetingAndDate()
        setupClickListener()
    }

    override fun onResume() {
        super.onResume()
        updateEstimasi()
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tvGreeting)
        tvDate = findViewById(R.id.tvDate)
        tvEstimation = findViewById(R.id.tvEstimation)
        cardTambahan = findViewById(R.id.cardTambahan)
        cardPegawai = findViewById(R.id.cardPegawai)
        cardCabang = findViewById(R.id.cardCabang)
        cardPrinter = findViewById(R.id.cardPrinter)
        cardAkun = findViewById(R.id.cardAkun)
        cardLayanan = findViewById(R.id.cardLayanan)
        menuTransaksi = findViewById(R.id.menuTransaksi)
        menuPelanggan = findViewById(R.id.menuPelanggan)
        menuLaporan = findViewById(R.id.menuLaporan)
    }

    private fun updateEstimasi() {
        var totalHariIni = 0
        val hariIni = SimpleDateFormat("dd/MM/yyyy", Locale("id", "ID")).format(Date())

        for (item in LaporanActivity.listLaporanGlobal) {
            // waktu format: dd/MM/yyyy HH:mm — ambil tanggal saja
            val tanggalTransaksi = item.waktu.take(10)
            if (tanggalTransaksi == hariIni) {
                val angka = item.totalBayar.replace("Rp", "").replace(".", "").replace(",", "").trim()
                totalHariIni += angka.toIntOrNull() ?: 0
            }
        }

        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvEstimation.text = format.format(totalHariIni)
    }

    private fun setupClickListener() {
        menuTransaksi.setOnClickListener {
            startActivity(Intent(this, PilihProdukActivity::class.java))
        }
        menuPelanggan.setOnClickListener {
            startActivity(Intent(this, PelangganActivity::class.java))
        }
        menuLaporan.setOnClickListener {
            startActivity(Intent(this, LaporanActivity::class.java))
        }
        cardLayanan.setOnClickListener {
            startActivity(Intent(this, DaftarProdukActivity::class.java))
        }
        cardAkun.setOnClickListener {
            startActivity(Intent(this, AccountActivity::class.java))
        }
        cardTambahan.setOnClickListener {
            startActivity(Intent(this, DataKategoriActivity::class.java))
        }
        cardPegawai.setOnClickListener {
            startActivity(Intent(this, AddEmployeeActivity::class.java))
        }
        cardCabang.setOnClickListener {
            startActivity(Intent(this, AddOutletActivity::class.java))
        }
        cardPrinter.setOnClickListener {
            startActivity(Intent(this, PrinterActivity::class.java))
        }
    }

    private fun setupGreetingAndDate() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Selamat Pagi"
            hour < 15 -> "Selamat Siang"
            hour < 18 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
        tvGreeting.text = greeting
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        tvDate.text = dateFormat.format(Date())
    }
}