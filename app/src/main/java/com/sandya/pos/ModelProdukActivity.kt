package com.sandya.pos

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ModelProdukActivity : AppCompatActivity() {

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

    private val bukaGaleriLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            gambarUriTerpilih = uri
            imgPreview.setImageURI(uri)
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

        val listKategori = arrayOf("Lips", "Face", "Eyes", "Skincare")
        val adapterKategori = ArrayAdapter(this, android.R.layout.simple_spinner_item, listKategori)
        adapterKategori.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKategori.adapter = adapterKategori

        val listCabang = arrayOf("Pusat", "Cabang Surakarta", "Cabang Sukoharjo")
        val adapterCabang = ArrayAdapter(this, android.R.layout.simple_spinner_item, listCabang)
        adapterCabang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCabang.adapter = adapterCabang

        btnGaleri.setOnClickListener {
            bukaGaleriLauncher.launch("image/*")
        }

        btnKamera.setOnClickListener {
            Toast.makeText(this, "Fitur kamera aktif!", Toast.LENGTH_SHORT).show()
        }

        btnSimpan.setOnClickListener {
            val nama = etNama.text.toString().trim()
            val hargaStr = etHarga.text.toString().trim()
            val kategoriTerpilih = spKategori.selectedItem.toString()

            if (nama.isNotEmpty() && hargaStr.isNotEmpty()) {
                val harga = hargaStr.toInt()
                val idBarangAcak = "MK-" + (100..999).random().toString()

                val produkBaru = PilihProdukActivity.Companion.MenuItem(
                    idBarangAcak,
                    nama,
                    kategoriTerpilih,
                    harga,
                    gambarUriTerpilih
                )

                // 🌟 FIX UTAMA: Disamakan menyetor data ke list global MainActivity
                MainActivity.menuKosmetikListGlobal.add(produkBaru)

                Toast.makeText(this, "$nama berhasil disimpan!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Nama produk dan harga wajib diisi!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}