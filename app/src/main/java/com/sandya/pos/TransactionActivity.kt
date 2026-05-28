package com.sandya.pos

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
import com.sandya.pos.R

class TransactionActivity : AppCompatActivity() {

    // 🌟 SEKARANG DATA CLASS NYA MASUK KE DALAM BIAR STRUKTUR CLASS-NYA TUNGGAL
    data class CartItem(val name: String, val price: Long, var quantity: Int)

    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvSubtotalPrice: TextView
    private lateinit var tvTaxPrice: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnCheckout: Button

    private val cartList = mutableListOf(
        CartItem("Liptint Pastel Pink", 45000, 2),
        CartItem("Skintint Glow Foundation", 89000, 1),
        CartItem("Compact Powder Matte", 65000, 1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        rvCartItems = findViewById(R.id.rvCartItems)

        tvSubtotalPrice = findViewById(android.R.id.text1) ?: TextView(this)
        tvTaxPrice = findViewById(android.R.id.text2) ?: TextView(this)
        tvTotalPrice = findViewById(android.R.id.summary) ?: TextView(this)

        btnCheckout = findViewById(R.id.btnProcessPayment)

        rvCartItems.layoutManager = LinearLayoutManager(this)
        updateTransactionSummary()

        val adapter = CartAdapter(cartList) {
            updateTransactionSummary()
        }
        rvCartItems.adapter = adapter

        btnCheckout.setOnClickListener {
            Toast.makeText(this, "Transaksi Berhasil Disimpan & Dicetak!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updateTransactionSummary() {
        var subtotal = 0L
        for (item in cartList) {
            subtotal += item.price * item.quantity
        }

        val tax = (subtotal * 0.11).toLong()
        val total = subtotal + tax

        tvSubtotalPrice.text = "Subtotal: Rp %,d".format(subtotal).replace(",", ".")
        tvTaxPrice.text = "PPN (11%): Rp %,d".format(tax).replace(",", ".")
        tvTotalPrice.text = "Total: Rp %,d".format(total).replace(",", ".")
    }

    // --- INNER CLASS ADAPTER ---
    private inner class CartAdapter(
        private val items: List<CartItem>,
        private val onQuantityChanged: () -> Unit
    ) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvCartProductName)
            val tvPrice: TextView = view.findViewById(R.id.tvCartProductPrice)
            val tvQuantity: TextView = view.findViewById(R.id.tvProductQuantity)
            val btnMinus: ImageView = view.findViewById(R.id.btnMinusQuantity)
            val btnPlus: ImageView = view.findViewById(R.id.btnPlusQuantity)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_product, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.tvName.text = item.name
            holder.tvPrice.text = "Rp %,d".format(item.price * item.quantity).replace(",", ".")
            holder.tvQuantity.text = item.quantity.toString()

            holder.btnPlus.setOnClickListener {
                item.quantity++
                holder.tvQuantity.text = item.quantity.toString()
                holder.tvPrice.text = "Rp %,d".format(item.price * item.quantity).replace(",", ".")
                onQuantityChanged()
            }

            holder.btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    holder.tvQuantity.text = item.quantity.toString()
                    holder.tvPrice.text = "Rp %,d".format(item.price * item.quantity).replace(",", ".")
                    onQuantityChanged()
                }
            }
        }

        override fun getItemCount(): Int = items.size
    }
} // 🌟 Akhir dari kelas utama;