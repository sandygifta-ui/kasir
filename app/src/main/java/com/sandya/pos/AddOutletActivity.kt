package com.sandya.pos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sandya.pos.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddOutletActivity : AppCompatActivity() {

    private lateinit var etOutletName: TextInputEditText
    private lateinit var etOutletAddress: TextInputEditText
    private lateinit var etOutletPhone: TextInputEditText
    private lateinit var btnSaveOutlet: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_outlet)

        val toolbar = findViewById<Toolbar>(R.id.toolbarOutlet)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        etOutletName = findViewById(R.id.etOutletName)
        etOutletAddress = findViewById(R.id.etOutletAddress)
        etOutletPhone = findViewById(R.id.etOutletPhone)
        btnSaveOutlet = findViewById(R.id.btnSaveOutlet)

        btnSaveOutlet.setOnClickListener {
            val name = etOutletName.text.toString().trim()
            if (name.isEmpty()) {
                etOutletName.error = "Nama outlet wajib diisi!"
            } else {
                Toast.makeText(this, "Outlet $name Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}