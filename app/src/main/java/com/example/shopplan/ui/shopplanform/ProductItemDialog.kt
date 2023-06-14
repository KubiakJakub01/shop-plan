package com.example.shopplan.ui.shopplanform

import android.app.Dialog
import android.content.Context
import android.widget.Button
import android.widget.EditText
import com.example.shopplan.R
import com.example.shopplan.model.table.ProductModel

class ProductItemDialog (private val shopPlanFormActivity: ShopPlanFormActivity){
    fun showItemDialog() {
        val dialog = Dialog(shopPlanFormActivity)
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
            val newItem = ProductModel(name=productName, price=price, quantity=quantity)

            // Add the new item to the adapter
            shopPlanFormActivity.getAdapter().addItem(newItem)

            // Update the total cost
            shopPlanFormActivity.updateTotalCostText(itemCost)

            dialog.dismiss()
        }

        // Set click listener for the Cancel button
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}