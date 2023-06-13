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
import com.example.shopplan.model.repository.DBRepository
import com.example.shopplan.model.table.ProductModel
import com.example.shopplan.model.table.ShopPlanModel

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private lateinit var adapter: ShopPlanAdapter
    private lateinit var rvShopPlans: RecyclerView
//    private lateinit var shopPlanAdapter: ShopPlanAdapter
    private lateinit var dbRepository: DBRepository
    private lateinit var btnCreateShopPlan: Button
    companion object {
        private const val NEW_SHOP_PLAN_FORM_REQUEST_CODE = 1
        private const val UPDATE_SHOP_PLAN_FORM_REQUEST_CODE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbRepository = DBRepository.getInstance(this)

        adapter = ShopPlanAdapter()
        rvShopPlans = findViewById(R.id.rvShopPlans)
        rvShopPlans.adapter = adapter
        rvShopPlans.layoutManager = LinearLayoutManager(this)
        rvShopPlans.setHasFixedSize(true)
        fillShopPlanList()

        btnCreateShopPlan = findViewById(R.id.btnCreateShopPlan)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(TAG, "onActivityResult. requestCode: $requestCode, resultCode: $resultCode, data: $data")
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is the same as what is passed here
        if (resultCode == Activity.RESULT_OK) {
            // Retrieve the information from the result intent
            val title = data?.getStringExtra("title")
            val shop = data?.getStringExtra("shopName")
            val totalCost = data?.getDoubleExtra("totalCost", 0.0)
            val products = data?.getParcelableArrayListExtra<ProductModel>("products")

            val shopPlan = ShopPlanModel(title.toString(), shop.toString(),
                totalCost.toString().toDouble(), products as ArrayList<ProductModel>)
            Log.i(TAG, "onActivityResult retrieve: $shopPlan")
            if (requestCode == NEW_SHOP_PLAN_FORM_REQUEST_CODE) {
                Log.i(TAG, "onActivityResult NEW_SHOP_PLAN_FORM_REQUEST_CODE")
                adapter.addItem(shopPlan)
                dbRepository.getShopPlanDao().addShopPlan(shopPlan)
            } else if (requestCode == UPDATE_SHOP_PLAN_FORM_REQUEST_CODE) {
                Log.i(TAG, "onActivityResult UPDATE_SHOP_PLAN_FORM_REQUEST_CODE")
                adapter.updateItem(shopPlan)
                dbRepository.getShopPlanDao().updateShopPlan(shopPlan)
            }
        }
    }

    fun openShopPlanForm(view: View) {
        val intent = Intent(this, ShopPlanFormActivity::class.java)
        startActivityForResult(intent, NEW_SHOP_PLAN_FORM_REQUEST_CODE)
    }

    private fun fillShopPlanList() {
        val shopPlans = dbRepository.getShopPlanDao().getShopPlans()
        for (shopPlan in shopPlans) {
            adapter.addItem(shopPlan)
        }
    }
}

