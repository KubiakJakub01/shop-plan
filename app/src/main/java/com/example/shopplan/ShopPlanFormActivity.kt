package com.example.shopplan

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.model.table.ProductModel
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanFormActivity : AppCompatActivity(), QuantityChangeListener  {
    private val TAG = "ShopPlanFormActivity"

    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var buttonAddItem: AppCompatImageButton
    private lateinit var editTextTitle: EditText
    private lateinit var editTextShopName: EditText
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonSave: Button
    private var totalCost: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_plan_form)

        // Find the UI elements
        adapter = ProductAdapter()
        adapter.setQuantityChangeListener(this)
        recyclerViewItems = findViewById(R.id.recyclerViewItems)
        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        recyclerViewItems.adapter = adapter

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextShopName = findViewById(R.id.editTextShopName)

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveShopPlan()
        }

        buttonAddItem = findViewById(R.id.buttonAddItem)
        buttonAddItem.setOnClickListener {
            showItemDialog()
        }

        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)

        val shopPlan = intent.getParcelableExtra<ShopPlanModel>("shopPlan")
        if (shopPlan != null) {
            fillFormWithShopPlan(shopPlan)
        }
    }

    override fun onQuantityChanged(costChange: Double) {
        updateTotalCostText(costChange)
    }

    private fun fillFormWithShopPlan(shopPlan: ShopPlanModel) {
        editTextTitle.setText(shopPlan.title)
        editTextShopName.setText(shopPlan.shopName)

        // Clear the existing items in the adapter
        adapter.clearItems()

        // Add the items from the shop plan to the adapter
        for (product in shopPlan.products) {
            adapter.addItem(product)
        }

        // Update the total cost
        updateTotalCostText(shopPlan.totalCost)
    }

    private fun showItemDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_product)

        // Find the dialog UI elements
        var editTextProductName = dialog.findViewById<EditText>(R.id.editTextProductName)
        var editTextPrice = dialog.findViewById<EditText>(R.id.editTextPrice)
        var editTextQuantity = dialog.findViewById<EditText>(R.id.editTextQuantity)

        val buttonAdd = dialog.findViewById<Button>(R.id.buttonAdd)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)

        // Set click listener for the Add button
        buttonAdd.setOnClickListener {
            val productName = editTextProductName.text.toString()
            val price = editTextPrice.text.toString().toDouble()
            val quantity = editTextQuantity.text.toString().toInt()

            val itemCost = quantity * price
            // Create a new item object with the input values
            val newItem = ProductModel(productName, price, quantity)

            // Add the new item to the adapter
            adapter.addItem(newItem)

            // Update the total cost
            updateTotalCostText(itemCost)

            dialog.dismiss()
        }

        // Set click listener for the Cancel button
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateTotalCostText(costChange: Double) {
        totalCost += costChange
        textViewTotalPrice.text = "Total Price: $${"%.2f".format(totalCost)}"
    }

    private fun saveShopPlan() {
        val title = editTextTitle.text.toString()
        val shopName = editTextShopName.text.toString()
        val products = adapter.getItems()

        Log.i(TAG, "saveShopPlan: $title, $shopName, $totalCost, $products")
        val resultIntent = Intent()
        resultIntent.putExtra("title", title)
        resultIntent.putExtra("shopName", shopName)
        resultIntent.putExtra("totalCost", totalCost)
        resultIntent.putExtra("products", products)

        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
