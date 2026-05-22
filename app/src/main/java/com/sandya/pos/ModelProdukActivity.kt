package com.sandya.pos

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

@Suppress("SpellCheckingInspection")
class ModelProdukActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var btnKamera: Button
    private lateinit var btnGaleri: Button
    private lateinit var btnSimpan: Button
    private lateinit var etNama: EditText
    private lateinit var etHarga: EditText
    private lateinit var etStok: EditText
    private lateinit var cbTakTerbatas: CheckBox
    private lateinit var spKategori: Spinner
    private lateinit var spCabang: Spinner

    private var selectedImageUri: Uri? = null

    // CAMERA RESULT
    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                @Suppress("DEPRECATION")
                val photo = result.data?.extras?.get("data") as? Bitmap
                photo?.let {
                    selectedImageUri = null
                    imgPreview.setImageBitmap(it)
                }
            }
        }

    // GALLERY RESULT
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    imgPreview.setImageURI(uri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.model_produk_activity)

        initViews()

        // SPINNER DATA
        val categories = arrayOf("Makanan", "Minuman", "Snack")
        val branches = arrayOf("Cabang 1", "Cabang 2")

        spKategori.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spCabang.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, branches)

        // BUTTON ACTIONS
        btnKamera.setOnClickListener {
            cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }

        btnGaleri.setOnClickListener {
            galleryLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        }

        // CHECKBOX STOCK
        cbTakTerbatas.setOnCheckedChangeListener { _, isChecked ->
            etStok.isEnabled = !isChecked
            if (isChecked) {
                etStok.setText("")
                etStok.error = null
            }
        }

        // SAVE BUTTON
        btnSimpan.setOnClickListener {
            validateAndSave()
        }
    }

    private fun initViews() {
        imgPreview = findViewById(R.id.imgPreview)
        btnKamera = findViewById(R.id.btnKamera)
        btnGaleri = findViewById(R.id.btnGaleri)
        btnSimpan = findViewById(R.id.btnSimpan)
        etNama = findViewById(R.id.etNama)
        etHarga = findViewById(R.id.etHarga)
        etStok = findViewById(R.id.etStok)
        cbTakTerbatas = findViewById(R.id.cbTakTerbatas)
        spKategori = findViewById(R.id.spKategori)
        spCabang = findViewById(R.id.spCabang)
    }

    private fun validateAndSave() {
        val name = etNama.text.toString().trim()
        val price = etHarga.text.toString().trim()
        val inputStock = etStok.text.toString().trim()

        if (name.isEmpty()) {
            etNama.error = "Required"
            return
        }

        if (price.isEmpty()) {
            etHarga.error = "Required"
            return
        }

        val stock = if (cbTakTerbatas.isChecked) {
            "Unlimited"
        } else {
            if (inputStock.isEmpty()) {
                etStok.error = "Required"
                return
            }
            inputStock
        }

        Toast.makeText(this, "Success: $name $stock", Toast.LENGTH_SHORT).show()
    }
}