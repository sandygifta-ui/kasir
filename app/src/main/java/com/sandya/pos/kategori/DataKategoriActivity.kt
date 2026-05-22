package com.sandya.pos.viewmodel

import adapter.DetailKategoriAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sandya.pos.ModelKategori
import com.sandya.pos.R
import com.sandya.pos.viewmodel.DataKategoriViewModel

class DataKategoriActivity : AppCompatActivity() {

    private val viewModel: DataKategoriViewModel by viewModels()

    private lateinit var rvKategori: RecyclerView
    private lateinit var fabTambah: FloatingActionButton
    private lateinit var etSearch: EditText
    private lateinit var btnClear: ImageButton

    private lateinit var adapter: DetailKategoriAdapter
    private var listKategori: List<ModelKategori> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_kategori)

        init()

        rvKategori.layoutManager = LinearLayoutManager(this)

        // Observe ViewModel
        viewModel.kategoriList.observe(this) { list ->
            listKategori = list
            setAdapter(list)
        }

        // SEARCH
        etSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().lowercase()

                btnClear.visibility =
                    if (keyword.isEmpty()) View.GONE else View.VISIBLE

                val filteredList = listKategori.filter {
                    it.namaKategori?.lowercase()?.contains(keyword) == true
                }

                setAdapter(filteredList)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnClear.setOnClickListener {
            etSearch.text.clear()
        }

        fabTambah.setOnClickListener {
            Toast.makeText(this, "Tambah Kategori", Toast.LENGTH_SHORT).show()
        }
    }

    private fun init() {
        rvKategori = findViewById(R.id.rvKategori)
        fabTambah = findViewById(R.id.fabTambah)
        etSearch = findViewById(R.id.etSearch)
        btnClear = findViewById(R.id.btnClearSearch)
    }

    private fun setAdapter(list: List<ModelKategori>) {
        adapter = DetailKategoriAdapter(list)
        rvKategori.adapter = adapter

        adapter.setOnClickListener(object : DetailKategoriAdapter.OnClickListener {
            override fun onItemClick(kategori: ModelKategori) {
                Toast.makeText(
                    this@DataKategoriActivity,
                    "Klik: ${kategori.namaKategori}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}