package com.sandya.pos

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList

class EmployeeActivity : AppCompatActivity() {

    private lateinit var etSearchEmployee: EditText
    private lateinit var rvEmployee: RecyclerView
    private lateinit var fabAddEmployee: FloatingActionButton

    private lateinit var adapter: EmployeeAdapter
    private val displayList = ArrayList<EmployeeItem>()

    companion object {
        class EmployeeItem(
            val nama: String,
            val jabatan: String,
            val email: String,
            val telepon: String,
            val cabang: String,
            var isAktif: Boolean // 🌟 Ada status aktif/tidak
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        etSearchEmployee = findViewById(R.id.et_search_employee)
        rvEmployee = findViewById(R.id.rv_employee)
        fabAddEmployee = findViewById(R.id.fab_add_employee)

        // 🌟 KOSONGAN: Data bawaan (Citra, Dhea, Dawiya) sudah dihapus total dari memori awal!

        displayList.addAll(MainActivity.employeeListGlobal)

        adapter = EmployeeAdapter(displayList)
        rvEmployee.layoutManager = LinearLayoutManager(this)
        rvEmployee.adapter = adapter

        fabAddEmployee.setOnClickListener {
            val intent = Intent(this, AddEmployeeActivity::class.java)
            startActivity(intent)
        }

        etSearchEmployee.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNama(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        filterNama(etSearchEmployee.text.toString())
    }

    private fun filterNama(query: String) {
        displayList.clear()
        if (query.isEmpty()) {
            displayList.addAll(MainActivity.employeeListGlobal)
        } else {
            for (item in MainActivity.employeeListGlobal) {
                if (item.nama.contains(query, ignoreCase = true)) {
                    displayList.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private inner class EmployeeAdapter(private val list: List<EmployeeItem>) :
        RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvEmpName: TextView = view.findViewById(R.id.tvEmpName)
            val tvEmpRole: TextView = view.findViewById(R.id.tvEmpRole)
            val tvEmpEmail: TextView = view.findViewById(R.id.tvEmpEmail)
            val tvEmpPhone: TextView = view.findViewById(R.id.tvEmpPhone)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_employee_card, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = list[position]
            holder.tvEmpName.text = item.nama
            holder.tvEmpRole.text = item.jabatan
            holder.tvEmpEmail.text = item.email
            holder.tvEmpPhone.text = item.telepon

            val btnStatus = holder.itemView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnStatusAktif)

            // 🌟 LOGIKA WARNA TOMBOL DINAMIS SAAT AKTIF / NONAKTIF
            fun aturTampilanTombol(aktif: Boolean) {
                if (aktif) {
                    btnStatus.text = "Aktif"
                    btnStatus.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
                    btnStatus.setIconResource(android.graphics.drawable.checkbox_on_background)
                    btnStatus.setIconTintResource(android.R.color.holo_green_dark)
                    btnStatus.setBackgroundColor(android.graphics.Color.parseColor("#E8F5E9")) // Hijau Pastel
                    btnStatus.setStrokeColorResource(android.R.color.holo_green_light)
                } else {
                    btnStatus.text = "Nonaktif"
                    btnStatus.setTextColor(android.graphics.Color.parseColor("#757575"))
                    btnStatus.setIconResource(android.graphics.drawable.checkbox_off_background)
                    btnStatus.setIconTintResource(android.R.color.darker_gray)
                    btnStatus.setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5")) // Abu-abu pudar
                    btnStatus.setStrokeColorResource(android.R.color.darker_gray)
                }
            }

            aturTampilanTombol(item.isAktif)

            // 🌟 INTERAKTIF: Klik langsung berubah warna di tempat
            btnStatus.setOnClickListener {
                item.isAktif = !item.isAktif
                aturTampilanTombol(item.isAktif)
                val pesan = if (item.isAktif) "${item.nama} diaktifkan" else "${item.nama} dinonaktifkan"
                Toast.makeText(this@EmployeeActivity, pesan, Toast.LENGTH_SHORT).show()
            }
        }

        override fun getItemCount(): Int = list.size
    }
}