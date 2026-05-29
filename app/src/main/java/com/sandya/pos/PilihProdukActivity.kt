package com.sandya.pos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class PilihProdukActivity : AppCompatActivity() {

    private lateinit var rvPilihProduk: RecyclerView
    private lateinit var tvTotalBayar: TextView
    private lateinit var btnLanjutTransaksi: Button

    private var subtotal = 0
    private lateinit var adapter: MenuAdapter
    private val listProdukDifilter = ArrayList<MenuItem>()

    companion object {
        class MenuItem(
            val idBarang: String,
            val namaMenu: String,
            val kategori: String,
            val harga: Int,
            val imageUri: Uri?
        ) {
            var jumlahBeli: Int = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_produk)

        rvPilihProduk = findViewById(R.id.rvPilihProduk)
        tvTotalBayar = findViewById(R.id.tvTotalBayar)
        btnLanjutTransaksi = findViewById(R.id.btnLanjutTransaksi)

        refreshDataPenyaringan()

        adapter = MenuAdapter(listProdukDifilter)
        rvPilihProduk.layoutManager = LinearLayoutManager(this)
        rvPilihProduk.adapter = adapter

        hitungTotalBelanja()

        btnLanjutTransaksi.setOnClickListener {
            if (subtotal > 0) {
                val keranjangBelanja = ArrayList<String>()
                for (item in MainActivity.menuKosmetikListGlobal) {
                    if (item.jumlahBeli > 0) {
                        keranjangBelanja.add("${item.namaMenu} (${item.jumlahBeli}x) - Rp${item.harga * item.jumlahBeli}")
                    }
                }
                val intent = Intent(this, PembayaranActivity::class.java)
                intent.putExtra("TOTAL_HARGA", subtotal)
                intent.putStringArrayListExtra("KERANJANG", keranjangBelanja)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Keranjang belanja masih kosong!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshDataPenyaringan()
        adapter.notifyDataSetChanged()
        hitungTotalBelanja()
    }

    private fun refreshDataPenyaringan() {
        val kategoriIntent = intent.getStringExtra("KATEGORI_TERPILIH")
        listProdukDifilter.clear()
        if (kategoriIntent.isNullOrEmpty()) {
            listProdukDifilter.addAll(MainActivity.menuKosmetikListGlobal)
        } else {
            for (item in MainActivity.menuKosmetikListGlobal) {
                if (item.kategori.trim().equals(kategoriIntent.trim(), ignoreCase = true)) {
                    listProdukDifilter.add(item)
                }
            }
        }
    }

    private fun hitungTotalBelanja() {
        subtotal = 0
        for (item in MainActivity.menuKosmetikListGlobal) {
            subtotal += (item.harga * item.jumlahBeli)
        }
        tvTotalBayar.text = "Rp$subtotal"
    }

    private inner class MenuAdapter(private val list: List<MenuItem>) :
        RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ivGambar: ImageView = view.findViewById(R.id.ivProductImage)
            val tvNama: TextView = view.findViewById(R.id.tvCartProductName)
            val tvHarga: TextView = view.findViewById(R.id.tvCartProductPrice)
            val tvJumlah: TextView = view.findViewById(R.id.tvProductQuantity)
            val btnMinus: ImageView = view.findViewById(R.id.btnMinusQuantity)
            val btnPlus: ImageView = view.findViewById(R.id.btnPlusQuantity)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_card_product, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]

            holder.tvNama.text = item.namaMenu
            holder.tvHarga.text = "Rp${"%,d".format(item.harga)}"
            holder.tvJumlah.text = item.jumlahBeli.toString()

            if (item.imageUri != null) {
                holder.ivGambar.setImageURI(item.imageUri)
            } else {
                holder.ivGambar.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            holder.btnPlus.setOnClickListener {
                item.jumlahBeli++
                holder.tvJumlah.text = item.jumlahBeli.toString()
                hitungTotalBelanja()
            }

            holder.btnMinus.setOnClickListener {
                if (item.jumlahBeli > 0) {
                    item.jumlahBeli--
                    holder.tvJumlah.text = item.jumlahBeli.toString()
                    hitungTotalBelanja()
                }
            }
        }

        override fun getItemCount(): Int = list.size
    }
}