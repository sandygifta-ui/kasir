package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class AccountActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnLogout: Button
    private lateinit var tvNamaTokoHeader: TextView
    private lateinit var tvNamaTokoDetail: TextView
    private lateinit var tvAlamatToko: TextView
    private lateinit var tvCabangToko: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        initViews()
        setupDataToko()

        btnBack.setOnClickListener { finish() }
        btnLogout.setOnClickListener { tampilkanDialogLogout() }
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        btnLogout = findViewById(R.id.btnLogout)
        tvNamaTokoHeader = findViewById(R.id.tvNamaTokoHeader)
        tvNamaTokoDetail = findViewById(R.id.tvNamaTokoDetail)
        tvAlamatToko = findViewById(R.id.tvAlamatToko)
        tvCabangToko = findViewById(R.id.tvCabangToko)
    }

    private fun setupDataToko() {
        tvNamaTokoHeader.text = "Bunny Blush Co."
        tvNamaTokoDetail.text = "Bunny Blush Co."
        tvAlamatToko.text = "Jl. Raya Surakarta-Sukoharjo"
        tvCabangToko.text = "Surakarta & Sukoharjo Region"
    }

    private fun tampilkanDialogLogout() {
        val bottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_logout, null)
        bottomSheet.setContentView(view)

        view.findViewById<Button>(R.id.btnYesLogout).setOnClickListener {
            bottomSheet.dismiss()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        view.findViewById<Button>(R.id.btnNoLogout).setOnClickListener {
            bottomSheet.dismiss()
        }

        bottomSheet.show()
    }
}