package com.example.shopplan.ui.shopplanform

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.R
import com.example.shopplan.model.table.ProductModel

class ProductAdapter :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private val productList = ArrayList<ProductModel>()
    private val checkedPositions = ArrayList<Int>()
    private var quantityChangeListener: QuantityChangeListener? = null

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewProductName: TextView = itemView.findViewById(R.id.textViewProductName)
        private val textViewPrice: TextView = itemView.findViewById(R.id.textViewPrice)
        private val textViewQuantity: TextView = itemView.findViewById(R.id.textViewQuantity)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val buttonMinus: ImageButton = itemView.findViewById(R.id.buttonMinus)
        val buttonPlus: ImageButton = itemView.findViewById(R.id.buttonPlus)

        fun bind(product: ProductModel) {
            textViewProductName.text = product.name
            textViewPrice.text = "Price: $${product.price}"
            textViewQuantity.text = product.quantity.toString()

            checkBox.isChecked = checkedPositions.contains(position)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    checkedPositions.add(position)
                } else {
                    checkedPositions.remove(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.bind(product)

        holder.buttonMinus.setOnClickListener {
            val updatedQuantity = product.quantity - 1
            if (updatedQuantity >= 0) {
                product.quantity = updatedQuantity
                quantityChangeListener?.onQuantityChanged((-1) * product.price)
                notifyItemChanged(position)
            }
        }

        holder.buttonPlus.setOnClickListener {
            val updatedQuantity = product.quantity + 1
            product.quantity = updatedQuantity
            quantityChangeListener?.onQuantityChanged(product.price)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun addItem(product: ProductModel) {
        productList.add(product)
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<ProductModel> {
        return productList
    }

    fun setQuantityChangeListener(listener: QuantityChangeListener) {
        quantityChangeListener = listener
    }

    fun clearItems() {
        productList.clear()
        notifyDataSetChanged()
    }

    fun deleteCheckedItems() {
        val checkedItems = getCheckedItems()
        productList.removeAll(checkedItems.toSet())
        checkedPositions.clear()
        notifyDataSetChanged()
    }

    private fun getCheckedItems(): ArrayList<ProductModel> {
        val checkedItems = ArrayList<ProductModel>()
        for (position in checkedPositions) {
            checkedItems.add(productList[position])
        }
        return checkedItems
    }
}
