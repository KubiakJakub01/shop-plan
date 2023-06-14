package com.example.shopplan.view.shopplanform

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.R
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.utils.InjectorUtils
import com.example.shopplan.view.shopplanform.product.ProductItemDialog
import com.example.shopplan.viewmodel.shopplanform.ProductModelViewFactory
import com.example.shopplan.viewmodel.shopplanform.ProductViewModel

class ShopPlanFormActivity : AppCompatActivity(), QuantityChangeListener {
    private val TAG = "ShopPlanFormActivity"

    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerViewItems: RecyclerView
    private lateinit var buttonDeleteItem: AppCompatImageButton
    private lateinit var buttonAddItem: AppCompatImageButton
    private lateinit var editTextTitle: EditText
    private lateinit var editTextShopName: EditText
    private lateinit var textViewTotalPrice: TextView
    private lateinit var buttonSave: Button
    private lateinit var productModelViewFactory: ProductModelViewFactory
    private lateinit var productViewModel: ProductViewModel
    private var shopPlanID: Int = 0
    private var totalCost: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_plan_form)

        initializeUI()
    }

    private fun initializeUI() {
        initViewModel()
        initializeRecyclerView()
        initComponents()
        val shopPlan = intent.getParcelableExtra<ShopPlanModel>("shopPlan")
        if (shopPlan != null) {
            fillFormWithShopPlan(shopPlan)
            shopPlanID = shopPlan.shopPlanID
        }

    }

    private fun initViewModel() {
        productModelViewFactory = InjectorUtils.provideProductViewModelFactory(this)
        productViewModel = ViewModelProvider(this, productModelViewFactory)[ProductViewModel::class.java]
    }

    private fun initializeRecyclerView() {
        adapter = ProductAdapter()
        adapter.setQuantityChangeListener(this)
        recyclerViewItems = findViewById(R.id.recyclerViewItems)
        recyclerViewItems.layoutManager = LinearLayoutManager(this)
        recyclerViewItems.adapter = adapter
    }

    private fun initComponents() {
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextShopName = findViewById(R.id.editTextShopName)

        buttonSave = findViewById(R.id.buttonSave)
        buttonSave.setOnClickListener {
            saveShopPlan()
        }

        buttonAddItem = findViewById(R.id.buttonAddItem)
        buttonAddItem.setOnClickListener {
            ProductItemDialog(this).showItemDialog()
        }

        buttonDeleteItem = findViewById(R.id.buttonDeleteItem)
        buttonDeleteItem.setOnClickListener {
            adapter.deleteCheckedItems()
        }

        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
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


    fun updateTotalCostText(costChange: Double) {
        totalCost += costChange
        textViewTotalPrice.text = "Total Price: $${"%.2f".format(totalCost)}"
    }

    private fun saveShopPlan() {
        val title = editTextTitle.text.toString()
        val shopName = editTextShopName.text.toString()
        val products = adapter.getItems()

        Log.i(TAG, "saveShopPlan: $title, $shopName, $totalCost, $products")
        val resultIntent = Intent()
        resultIntent.putExtra("shopPlanID", shopPlanID)
        resultIntent.putExtra("title", title)
        resultIntent.putExtra("shopName", shopName)
        resultIntent.putExtra("totalCost", totalCost)
        resultIntent.putExtra("products", products)

        setResult(RESULT_OK, resultIntent)
        finish()
    }

    fun getAdapter(): ProductAdapter {
        return adapter
    }
}
