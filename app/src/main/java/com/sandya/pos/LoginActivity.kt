package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            // Validasi Input Sederhana
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                if (pass.length >= 6) {
                    // 🌟 SAMBUNGAN LANGSUNG: Berpindah lancar ke Dashboard Utama
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // Menutup halaman login agar tidak kembali saat tombol back ditekan
                    finish()
                } else {
                    Toast.makeText(this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Email dan password wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}