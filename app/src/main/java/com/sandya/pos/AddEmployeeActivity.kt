package com.sandya.pos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import com.sandya.pos.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var etName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var spinnerOutlet: Spinner
    private lateinit var switchStatus: SwitchCompat
    private lateinit var etPosition: TextInputEditText
    private lateinit var etSalary: TextInputEditText
    private lateinit var btnSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        spinnerOutlet = findViewById(R.id.spinnerOutlet)
        switchStatus = findViewById(R.id.switchStatus) // Inisialisasi switch status
        etPosition = findViewById(R.id.etPosition)
        etSalary = findViewById(R.id.etSalary)
        btnSave = findViewById(R.id.btnSave)

        val outletOptions = arrayOf("ums", "uns", "laweyan", "colomadu", "slamet riyadi")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, outletOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOutlet.adapter = adapter

        // Aksi ketika tombol simpan diklik
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val selectedOutlet = spinnerOutlet.selectedItem.toString()

            // Membaca status keaktifan (true jika digeser aktif, false jika mati)
            val isActive = switchStatus.isChecked
            val statusText = if (isActive) "Aktif" else "Tidak Aktif"

            // Validasi sederhana: Nama tidak boleh kosong
            if (name.isEmpty()) {
                etName.error = "Nama lengkap harus diisi!"
            } else {
                // Menampilkan toast sukses dengan rangkuman data termasuk status aktifnya
                Toast.makeText(this, "Data $name ($selectedOutlet) Status: $statusText Berhasil Disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}