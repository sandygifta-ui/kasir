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
    private val displayList = ArrayList<AddEmployeeActivity.Employee>()

    companion object {
        class EmployeeItem(
            val nama: String,
            val jabatan: String,
            val email: String,
            val telepon: String,
            val cabang: String,
            var isAktif: Boolean
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        etSearchEmployee = findViewById(R.id.et_search_employee)
        rvEmployee = findViewById(R.id.rv_employee)
        fabAddEmployee = findViewById(R.id.fab_add_employee)

        displayList.addAll(AddEmployeeActivity.employeeList)

        adapter = EmployeeAdapter(displayList)
        rvEmployee.layoutManager = LinearLayoutManager(this)
        rvEmployee.adapter = adapter

        fabAddEmployee.setOnClickListener {
            startActivity(Intent(this, AddEmployeeActivity::class.java))
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
            displayList.addAll(AddEmployeeActivity.employeeList)
        } else {
            for (item in AddEmployeeActivity.employeeList) {
                if (item.nama.contains(query, ignoreCase = true)) {
                    displayList.add(item)
                }
            }
        }
        adapter.notifyDataSetChanged()
    }

    private inner class EmployeeAdapter(
        private val list: List<AddEmployeeActivity.Employee>
    ) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

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
            holder.tvEmpPhone.text = item.phone

            val btnStatus = holder.itemView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnStatusAktif)

            fun aturTampilanTombol(aktif: Boolean) {
                if (aktif) {
                    btnStatus.text = "Aktif"
                    btnStatus.setTextColor(android.graphics.Color.parseColor("#2E7D32"))
                    btnStatus.setBackgroundColor(android.graphics.Color.parseColor("#E8F5E9"))
                } else {
                    btnStatus.text = "Nonaktif"
                    btnStatus.setTextColor(android.graphics.Color.parseColor("#757575"))
                    btnStatus.setBackgroundColor(android.graphics.Color.parseColor("#F5F5F5"))
                }
            }

            aturTampilanTombol(item.aktif)
        }

        override fun getItemCount(): Int = list.size
    }
}