package com.sandya.pos

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TambahKategoriActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var btnKamera: Button
    private lateinit var btnGaleri: Button
    private lateinit var etNama: EditText
    private lateinit var etHarga: EditText
    private lateinit var spKategori: Spinner
    private lateinit var spCabang: Spinner
    private lateinit var etStok: EditText
    private lateinit var cbTakTerbatas: CheckBox
    private lateinit var btnSimpan: Button

    private var gambarUriTerpilih: Uri? = null
    private var uriKamera: Uri? = null

    private val bukaGaleriLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            gambarUriTerpilih = uri
            imgPreview.setImageURI(uri)
        }
    }

    private val bukaKameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { berhasil: Boolean ->
        if (berhasil && uriKamera != null) {
            gambarUriTerpilih = uriKamera
            imgPreview.setImageURI(uriKamera)
        }
    }

    private val requestKameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            bukaKamera()
        } else {
            Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.model_produk_activity)

        imgPreview = findViewById(R.id.imgPreview)
        btnKamera = findViewById(R.id.btnKamera)
        btnGaleri = findViewById(R.id.btnGaleri)
        etNama = findViewById(R.id.etNama)
        etHarga = findViewById(R.id.etHarga)
        spKategori = findViewById(R.id.spKategori)
        spCabang = findViewById(R.id.spCabang)
        etStok = findViewById(R.id.etStok)
        cbTakTerbatas = findViewById(R.id.cbTakTerbatas)
        btnSimpan = findViewById(R.id.btnSimpan)

        val kategoriDefault = intent.getStringExtra("KATEGORI_DEFAULT") ?: ""

        val semuaKategori = KategoriManager.daftarKategori
            .filter { it.status == "Aktif" }
            .map { it.nama }
            .toTypedArray()

        spKategori.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, semuaKategori).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        if (kategoriDefault.isNotEmpty()) {
            val index = semuaKategori.indexOf(kategoriDefault)
            if (index >= 0) spKategori.setSelection(index)
        }

        val listCabang = arrayOf("Pusat", "Cabang Surakarta", "Cabang Sukoharjo")
        spCabang.adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_item, listCabang).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        cbTakTerbatas.setOnCheckedChangeListener { _, isChecked ->
            etStok.isEnabled = !isChecked
            if (isChecked) etStok.setText("")
        }

        btnKamera.setOnClickListener {
            val izinKamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if (izinKamera == PackageManager.PERMISSION_GRANTED) {
                bukaKamera()
            } else {
                requestKameraPermission.launch(Manifest.permission.CAMERA)
            }
        }

        btnGaleri.setOnClickListener {
            bukaGaleriLauncher.launch("image/*")
        }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hargaStr = etHarga.text.toString().trim()
            val kategori = spKategori.selectedItem?.toString() ?: ""

            if (nama.isEmpty()) {
                etNama.error = "Nama produk wajib diisi"
                return@setOnClickListener
            }
            if (hargaStr.isEmpty()) {
                etHarga.error = "Harga wajib diisi"
                return@setOnClickListener
            }
            val harga = hargaStr.toIntOrNull()
            if (harga == null || harga <= 0) {
                etHarga.error = "Harga tidak valid"
                return@setOnClickListener
            }

            val idBarang = "MK-" + (100..999).random()

            MainActivity.menuKosmetikListGlobal.add(
                PilihProdukActivity.Companion.MenuItem(
                    idBarang, nama, kategori, harga, gambarUriTerpilih
                )
            )

            Toast.makeText(this,
                "\"$nama\" berhasil ditambahkan ke $kategori!",
                Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun bukaKamera() {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val file = File(
            getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "PRODUK_$timestamp.jpg"
        )
        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.fileprovider",
            file
        )
        uriKamera = uri
        bukaKameraLauncher.launch(uri)
    }
}