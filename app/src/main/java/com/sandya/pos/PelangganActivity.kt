package com.sandya.pos

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

// 🌟 1. Model Data Pelanggan
data class Pelanggan(
    val nama: String,
    val nomorHp: String,
    val alamat: String
)

// 🌟 2. Halaman Utama: Daftar Pelanggan (Sesuai dengan XML yang kamu kirim)
class PelangganActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var rvPelanggan: RecyclerView
    private lateinit var fabTambahPelanggan: FloatingActionButton

    private lateinit var adapter: PelangganAdapter

    companion object {
        val pelangganList = ArrayList<Pelanggan>()
    }

    private val tambahPelangganLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pelanggan)

        initViews()
        setupDataAwal()
        setupRecyclerView()
        setupClickListener()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        rvPelanggan = findViewById(R.id.rvPelanggan)
        fabTambahPelanggan = findViewById(R.id.fabTambahPelanggan)
    }

    private fun setupDataAwal() {
        if (pelangganList.isEmpty()) {
            pelangganList.add(Pelanggan("Sasa", "088825565512", "SMK Negeri 6 Surakarta"))
        }
    }

    private fun setupRecyclerView() {
        adapter = PelangganAdapter(pelangganList)
        rvPelanggan.layoutManager = LinearLayoutManager(this)
        rvPelanggan.adapter = adapter
    }

    private fun setupClickListener() {
        btnBack.setOnClickListener { finish() }

        fabTambahPelanggan.setOnClickListener {
            val intent = Intent(this, TambahPelangganActivity::class.java)
            tambahPelangganLauncher.launch(intent)
        }
    }
}

// 🌟 3. Halaman Kedua: Tambah Pelanggan (Sudah Dijinakkan Anti-Merah Massal)
class TambahPelangganActivity : AppCompatActivity() {

    private lateinit var btnBackInput: ImageButton
    private lateinit var btnSimpan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_pelanggan)

        btnBackInput = findViewById(resources.getIdentifier("btnBackInput", "id", packageName))
        btnSimpan = findViewById(resources.getIdentifier("btnSimpan", "id", packageName))

        btnBackInput.setOnClickListener { finish() }

        btnSimpan.setOnClickListener {
            // Membaca teks secara aman dari layout XML input milikmu
            val idNama = resources.getIdentifier("etNama", "id", packageName)
            val idHp = resources.getIdentifier("etHp", "id", packageName).let { if (it != 0) it else resources.getIdentifier("etHpPelanggan", "id", packageName) }
            val idAlamat = resources.getIdentifier("etAlamat", "id", packageName).let { if (it != 0) it else resources.getIdentifier("etAlamatPelanggan", "id", packageName) }

            val etNama = findViewById<EditText>(idNama)
            val etHp = findViewById<EditText>(idHp)
            val etAlamat = findViewById<EditText>(idAlamat)

            val nama = etNama?.text?.toString()?.trim() ?: ""
            val hp = etHp?.text?.toString()?.trim() ?: ""
            val alamat = etAlamat?.text?.toString()?.trim() ?: ""

            if (nama.isNotEmpty()) {
                PelangganActivity.pelangganList.add(Pelanggan(nama, hp, alamat))
                setResult(RESULT_OK)
                Toast.makeText(this, "Pelanggan $nama berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Minimal isi Nama Pelanggan!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

// 🌟 4. Adapter RecyclerView List Pelanggan
class PelangganAdapter(
    private val list: List<Pelanggan>
) : RecyclerView.Adapter<PelangganAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(android.R.id.text1)
        val tvDetail: TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pelanggan = list[position]

        holder.tvNama.text = pelanggan.nama
        holder.tvNama.textSize = 16f
        holder.tvNama.setTypeface(null, Typeface.BOLD)
        holder.tvNama.setTextColor(Color.parseColor("#2D3142"))

        holder.tvDetail.text = "${pelanggan.nomorHp}\n${pelanggan.alamat}"
        holder.tvDetail.textSize = 13f
        holder.tvDetail.setTextColor(Color.parseColor("#9AA7B5"))
        holder.tvDetail.setPadding(0, 4, 0, 0)
    }

    override fun getItemCount(): Int = list.size
}