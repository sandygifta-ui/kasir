package com.sandya.pos // Sesuaikan dengan nama package aslimu

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EmployeeListActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageView
    private lateinit var tvCount: TextView
    private lateinit var etSearch: EditText
    private lateinit var rvEmployee: RecyclerView
    private lateinit var fabAddEmployee: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Memanggil file layout baru yang serasi
        setContentView(R.layout.activity_employee_list)

        // Inisialisasi View
        btnBack = findViewById(R.id.btnBack)
        tvCount = findViewById(R.id.tvCount)
        etSearch = findViewById(R.id.etSearch)
        rvEmployee = findViewById(R.id.rvEmployee)
        fabAddEmployee = findViewById(R.id.fabAddEmployee)

        // Aksi tombol kembali
        btnBack.setOnClickListener {
            finish()
        }

        // Aksi ketika tombol plus (+) ditekan akan membuka AddEmployeeActivity
        fabAddEmployee.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

        // Mengatur susunan RecyclerView
        rvEmployee.layoutManager = LinearLayoutManager(this)
    }
}