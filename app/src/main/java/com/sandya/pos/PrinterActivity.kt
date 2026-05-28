package com.sandya.pos

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.io.OutputStream
import java.util.ArrayList
import java.util.UUID

class PrinterActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var viewStatusIndicator: View
    private lateinit var tvStatusPrinter: TextView
    private lateinit var tvBluetoothMati: TextView
    private lateinit var rvBluetoothDevices: RecyclerView
    private lateinit var btnScan: Button
    private lateinit var btnTestPrint: Button

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var targetDevice: BluetoothDevice? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    // 🛠️ AKALAN BERSALURAN: Langsung definisikan list di awal agar receiver tidak kehilangan jejak tipe data
    private val deviceList: ArrayList<BluetoothDevice> = ArrayList()
    private var deviceAdapter: BluetoothDeviceAdapter? = null

    private val PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val bluetoothLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Toast.makeText(this, "Bluetooth berhasil aktif!", Toast.LENGTH_SHORT).show()
            mulaiMencariPrinter()
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val connectGranted = permissions[Manifest.permission.BLUETOOTH_CONNECT] ?: false
        if (connectGranted || Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            eksekusiNyalakanBluetooth()
        } else {
            Toast.makeText(this, "Izin Bluetooth ditolak", Toast.LENGTH_LONG).show()
        }
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context?, intent: Intent?) {
            if (BluetoothDevice.ACTION_FOUND == intent?.action) {
                val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                if (device != null && !deviceList.contains(device)) {
                    deviceList.add(device)
                    // 🛠️ Dipanggil dengan tanda tanya (?) untuk memastikan editor tahu ini aman dari NullPointerException
                    deviceAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.sandya.pos.R.layout.activity_printer)

        initViews()
        setupRecyclerView()
        setupClickListener()
    }

    override fun onResume() {
        super.onResume()
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        try { unregisterReceiver(bluetoothReceiver) } catch (e: Exception) {}
        try { bluetoothSocket?.close() } catch (e: Exception) {}
    }

    private fun initViews() {
        btnBack = findViewById(com.sandya.pos.R.id.btnBack)
        viewStatusIndicator = findViewById(com.sandya.pos.R.id.viewStatusIndicator)
        tvStatusPrinter = findViewById(com.sandya.pos.R.id.tvStatusPrinter)
        tvBluetoothMati = findViewById(com.sandya.pos.R.id.tvBluetoothMati)
        rvBluetoothDevices = findViewById(com.sandya.pos.R.id.rvBluetoothDevices)
        btnScan = findViewById(com.sandya.pos.R.id.btnScan)
        btnTestPrint = findViewById(com.sandya.pos.R.id.btnTestPrint)
    }

    private fun setupRecyclerView() {
        deviceAdapter = BluetoothDeviceAdapter(deviceList) { device ->
            sambungkanKePrinter(device)
        }
        rvBluetoothDevices.layoutManager = LinearLayoutManager(this)
        rvBluetoothDevices.adapter = deviceAdapter
    }

    private fun setupClickListener() {
        btnBack.setOnClickListener { finish() }
        btnScan.setOnClickListener { mintaIzinDanScan() }
        btnTestPrint.setOnClickListener { kirimDataStrukKePrinter() }
    }

    private fun mintaIzinDanScan() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Hardware Bluetooth tidak tersedia", Toast.LENGTH_SHORT).show()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val hasConnect = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            if (!hasConnect) {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                return
            }
        }
        eksekusiNyalakanBluetooth()
    }

    private fun eksekusiNyalakanBluetooth() {
        if (bluetoothAdapter == null) return
        if (!bluetoothAdapter!!.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothLauncher.launch(intent)
        } else {
            mulaiMencariPrinter()
        }
    }

    @SuppressLint("MissingPermission")
    private fun mulaiMencariPrinter() {
        Toast.makeText(this, "Mencari perangkat printer...", Toast.LENGTH_SHORT).show()
        tvBluetoothMati.visibility = View.GONE
        rvBluetoothDevices.visibility = View.VISIBLE

        deviceList.clear()
        deviceAdapter?.notifyDataSetChanged()

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(bluetoothReceiver, filter)
        bluetoothAdapter?.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    private fun sambungkanKePrinter(device: BluetoothDevice) {
        targetDevice = device
        Toast.makeText(this, "Menghubungkan ke: ${device.name ?: "Printer"}", Toast.LENGTH_SHORT).show()

        Thread {
            try {
                bluetoothAdapter?.cancelDiscovery()
                bluetoothSocket = device.createRfcommSocketToServiceRecord(PRINTER_UUID)
                bluetoothSocket?.connect()
                outputStream = bluetoothSocket?.outputStream

                runOnUiThread {
                    tvStatusPrinter.text = "Terhubung ke ${device.name ?: "Printer"}"
                    tvStatusPrinter.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
                    viewStatusIndicator.backgroundTintList = ContextCompat.getColorStateList(this, android.R.color.holo_green_dark)
                    Toast.makeText(this, "Printer Siap Digunakan!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                runOnUiThread {
                    Toast.makeText(this, "Gagal menyambungkan printer", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun kirimDataStrukKePrinter() {
        if (outputStream == null) {
            Toast.makeText(this, "Printer belum terhubung! Silakan scan dahulu.", Toast.LENGTH_LONG).show()
            return
        }

        Thread {
            try {
                val namaToko   = "         BUNNY BLUSH CO.\n"
                val alamat     = "   Jl. Raya Surakarta-Sukoharjo\n"
                val pembatas1  = "================================\n"
                val infoWaktu  = "28 Mei 2026            14:00 WIB\n"
                val kasir      = "Kasir: Yaya\n"
                val pembatas2  = "--------------------------------\n"
                val item1      = "Liptint Pastel Pink\n  2 x Rp 45.000     Rp 90.000\n"
                val item2      = "Skintint Glow Foundation\n  1 x Rp 89.000     Rp 89.000\n"
                val subtotal   = "Subtotal:           Rp 179.000\n"
                val total      = "TOTAL:              Rp 198.690\n"
                val terimaKasih= "\n  Terima kasih telah berbelanja\n"
                val slogan     = "    Cantikmu, Semangat Kami!\n\n\n\n"

                outputStream?.write(namaToko.toByteArray())
                outputStream?.write(alamat.toByteArray())
                outputStream?.write(pembatas1.toByteArray())
                outputStream?.write(infoWaktu.toByteArray())
                outputStream?.write(kasir.toByteArray())
                outputStream?.write(pembatas2.toByteArray())
                outputStream?.write(item1.toByteArray())
                outputStream?.write(item2.toByteArray())
                outputStream?.write(pembatas2.toByteArray())
                outputStream?.write(subtotal.toByteArray())
                outputStream?.write(pembatas1.toByteArray())
                outputStream?.write(total.toByteArray())
                outputStream?.write(terimaKasih.toByteArray())
                outputStream?.write(slogan.toByteArray())

                runOnUiThread {
                    Toast.makeText(this, "Struk Bunny Blush berhasil dicetak!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                runOnUiThread {
                    Toast.makeText(this, "Eror saat mencetak struk", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }
}

class BluetoothDeviceAdapter(
    private val deviceList: List<BluetoothDevice>,
    private val onDeviceClick: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    class DeviceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDeviceName: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return DeviceViewHolder(view)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        val device = deviceList[position]
        holder.tvDeviceName.text = device.name ?: device.address
        holder.itemView.setOnClickListener { onDeviceClick(device) }
    }

    override fun getItemCount(): Int = deviceList.size
}