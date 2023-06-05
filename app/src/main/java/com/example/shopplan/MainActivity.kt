package com.example.shopplan

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private lateinit var adapter: ShopPlanAdapter
    private lateinit var rvShopPlans: RecyclerView
//    private lateinit var shopPlanAdapter: ShopPlanAdapter
    private lateinit var btnCreateShopPlan: Button
    companion object {
        private const val SHOP_PLAN_FORM_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ShopPlanAdapter()
        rvShopPlans = findViewById(R.id.rvShopPlans)
        rvShopPlans.adapter = adapter
        rvShopPlans.layoutManager = LinearLayoutManager(this)
        rvShopPlans.setHasFixedSize(true)

        btnCreateShopPlan = findViewById(R.id.btnCreateShopPlan)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult")
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is the same as what is passed here
        if (requestCode == SHOP_PLAN_FORM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Retrieve the information from the result intent
            val title = data?.getStringExtra("title")
            val shop = data?.getStringExtra("shopName")
            val totalCost = data?.getDoubleExtra("totalCost", 0.0)
            val products = data?.getParcelableArrayListExtra<ProductModel>("products")

            // Do something with the retrieved information
            val shopPlan = ShopPlanModel(title.toString(), shop.toString(),
                totalCost.toString().toDouble(), products as ArrayList<ProductModel>)
            Log.i(TAG, "onActivityResult retrieve: $shopPlan")
            adapter.addItem(shopPlan)
        }
    }

    fun openShopPlanForm(view: View) {
        val intent = Intent(this, ShopPlanFormActivity::class.java)
        startActivityForResult(intent, SHOP_PLAN_FORM_REQUEST_CODE)
    }
}

