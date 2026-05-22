package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvGreeting: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvEstimation: TextView
    private lateinit var cardTambahan: CardView

    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()

        initViews()
        setupGreetingAndDate()
        setupEstimasi()
        setupClickListener() // ⬅ tambah ini
    }

    private fun initViews() {
        tvGreeting = findViewById(R.id.tvGreeting)
        tvDate = findViewById(R.id.tvDate)
        tvEstimation = findViewById(R.id.tvEstimation)

        cardTambahan = findViewById(R.id.cardTambahan)
        // ⚠ pastikan id di XML adalah cardTambahan
    }

    private fun setupClickListener() {
        cardTambahan.setOnClickListener {
            val intent = Intent(this, TambahKategoriActivity::class.java)
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