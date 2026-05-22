package com.sandya.pos.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.sandya.pos.ModelKategori

class DataKategoriViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("kategori")

    // LiveData
    val kategoriList = MutableLiveData<ArrayList<ModelKategori>>()
    val isLoading = MutableLiveData<Boolean>()
    val isSearchEmpty = MutableLiveData<Boolean>()

    private var originalKategoriList = ArrayList<ModelKategori>()
    private val searchQuery = MutableLiveData<String?>()

    init {
        getData()
    }

    // ===============================
    // GET DATA FROM FIREBASE
    // ===============================
    fun getData() {

        isLoading.value = true

        val query = myRef
            .orderByChild("idKategori")
            .limitToLast(100)

        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                isLoading.value = false

                val list = ArrayList<ModelKategori>()

                if (snapshot.exists()) {

                    for (dataSnapshot in snapshot.children) {

                        val kategori =
                            dataSnapshot.getValue(ModelKategori::class.java)

                        if (kategori != null) {
                            list.add(kategori)
                        } else {
                            Log.e(
                                "DataKategoriViewModel",
                                "Parse gagal: ${dataSnapshot.key}"
                            )
                        }
                    }

                    originalKategoriList.clear()
                    originalKategoriList.addAll(list)

                    kategoriList.value = list
                    isSearchEmpty.value = false

                    Log.d(
                        "DataKategoriViewModel",
                        "Loaded ${list.size} kategori"
                    )

                } else {

                    originalKategoriList.clear()
                    kategoriList.value = ArrayList()
                    isSearchEmpty.value = true

                    Log.d(
                        "DataKategoriViewModel",
                        "Data kosong"
                    )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
                Log.e(
                    "FirebaseError",
                    error.message
                )
            }
        })
    }

    // ===============================
    // SEARCH FILTER
    // ===============================
    fun filterList(query: String?) {

        searchQuery.value = query

        if (query.isNullOrEmpty()) {

            kategoriList.value = originalKategoriList
            isSearchEmpty.value = false
            return
        }

        val filteredList = originalKategoriList.filter {

            it.namaKategori
                ?.lowercase()
                ?.contains(query.lowercase()) == true
        }

        kategoriList.value = ArrayList(filteredList)
        isSearchEmpty.value = filteredList.isEmpty()
    }
}