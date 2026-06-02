package com.sandya.pos

object KategoriManager {
    data class Kategori(val nama: String, val status: String)

    val daftarKategori = mutableListOf(
        Kategori("Lips", "Aktif"),
        Kategori("Face", "Aktif"),
        Kategori("Eyes", "Aktif"),
        Kategori("Skincare", "Aktif")
    )
}