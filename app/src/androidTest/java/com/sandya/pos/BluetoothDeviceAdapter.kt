package com.sandya.pos

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BluetoothDeviceAdapter(
    private val deviceList: List<BluetoothDevice>,
    private val onDeviceClick: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    // Menghubungkan komponen TextView bawaan Android untuk teks nama device
    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDeviceName: TextView = view.findViewById(android.R.id.text1)
    }

    // Mengatur layout baris list (kita pakai layout bawaan Android yang simpel saja)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return DeviceViewHolder(view)
    }

    // Memasukkan nama/alamat bluetooth printer ke dalam baris list dan mengatur aksi klik
    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position]

        // Tampilkan nama bluetooth, kalau namanya kosong tampilkan alamat MAC Address-nya
        holder.tvDeviceName.text = device.name ?: device.address

        // Saat salah satu nama printer di list kamu klik, dia langsung menjalankan fungsi connect
        holder.itemView.setOnClickListener {
            onDeviceClick(device)
        }
    }

    // Menghitung jumlah total perangkat bluetooth yang didapat
    override fun getItemCount(): Int = deviceList.size
}