package com.sandya.pos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButton

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var spinnerPosition: Spinner
    private lateinit var spinnerOutlet: Spinner
    private lateinit var switchStatus: SwitchCompat
    private lateinit var btnSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_employee)

        initViews()
        setupSpinnerData()

        btnSave.setOnClickListener {
            prosesSimpanData()
        }
    }

    private fun initViews() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        spinnerPosition = findViewById(R.id.spinnerPosition)
        spinnerOutlet = findViewById(R.id.spinnerOutlet)
        switchStatus = findViewById(R.id.switchStatus)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupSpinnerData() {
        val listOutlet = arrayOf("Surakarta", "Bandung", "Jogjakarta")
        val adapterOutlet = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOutlet)
        adapterOutlet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOutlet.adapter = adapterOutlet

        val listJabatan = arrayOf("Manager", "Cashier", "Staff", "Admin", "cleaning service")
        val adapterJabatan = ArrayAdapter(this, android.R.layout.simple_spinner_item, listJabatan)
        adapterJabatan.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPosition.adapter = adapterJabatan
    }

    private fun prosesSimpanData() {
        val namaBaru = etName.text.toString().trim()
        val emailBaru = etEmail.text.toString().trim()
        val phoneBaru = etPhone.text.toString().trim()
        val jabatanBaru = spinnerPosition.selectedItem.toString()
        val cabangBaru = spinnerOutlet.selectedItem.toString()
        val statusAktif = switchStatus.isChecked // 🌟 Ambil kondisi saklar switch

        if (namaBaru.isNotEmpty()) {
            // Masukkan data murni beserta status saklarnya ke list global
            MainActivity.employeeListGlobal.add(
                EmployeeActivity.Companion.EmployeeItem(
                    namaBaru,
                    jabatanBaru,
                    if (emailBaru.isNotEmpty()) emailBaru else "-",
                    if (phoneBaru.isNotEmpty()) phoneBaru else "-",
                    cabangBaru,
                    statusAktif
                )
            )

            Toast.makeText(this, "Pegawai \"$namaBaru\" berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            etName.error = "Nama lengkap tidak boleh kosong!"
        }
    }
}