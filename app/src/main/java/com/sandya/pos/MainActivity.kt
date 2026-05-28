package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.database.FirebaseDatabase
import com.sandya.pos.R
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
    private lateinit var cardAkun: CardView // 🌟 1. TAMBAHKAN VARIABEL AKUN DI SINI

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()

        initViews()
        setupGreetingAndDate()
        setupEstimasi()
        setupClickListener()
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tvGreeting)
        tvDate = findViewById(R.id.tvDate)
        tvEstimation = findViewById(R.id.tvEstimation)

        cardTambahan = findViewById(R.id.cardTambahan)
        cardPegawai = findViewById(R.id.cardPegawai)
        cardCabang = findViewById(R.id.cardCabang)
        cardPrinter = findViewById(R.id.cardPrinter)
        cardAkun = findViewById(R.id.cardAkun) // 🌟 2. IKAT ID XML CARD AKUN KAMU DI SINI
    }

    private fun setupClickListener() {
        // 🌟 3. TOMBOL AKUN BARU -> Membuka AccountActivity
        cardAkun.setOnClickListener {
            val intent = Intent(this, AccountActivity::class.java)
            startActivity(intent)
        }

        // Tombol Kategori Kotak Pink
        cardTambahan.setOnClickListener {
            val intent = Intent(this, DataKategoriActivity::class.java)
            startActivity(intent)
        }

        // Tombol Tambah Pegawai
        cardPegawai.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

        // Tombol Cabang -> Membuka AddOutletActivity
        cardCabang.setOnClickListener {
            val intent = Intent(this, AddOutletActivity::class.java)
            startActivity(intent)
        }

        // Tombol Printer
        cardPrinter.setOnClickListener {
            val intent = Intent(this, PrinterActivity::class.java)
            startActivity(intent)
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

    private fun setupEstimasi() {
        val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        tvEstimation.text = format.format(0)
    }
}