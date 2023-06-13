package com.example.shopplan.ui.shopplan

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.R
import com.example.shopplan.ShopPlanFormActivity
import com.example.shopplan.model.table.ProductModel
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.utils.InjectorUtils

class ShopPlanActivity : ComponentActivity() {

    private val TAG = "ShopPlanActivity"
    private lateinit var adapter: ShopPlanAdapter
    private lateinit var rvShopPlans: RecyclerView
    private lateinit var shopPlanFactory: ShopPlanModelViewFactory
    private lateinit var shopPlanViewModel: ShopPlanViewModel
    private lateinit var btnCreateShopPlan: Button
    companion object {
        private const val NEW_SHOP_PLAN_FORM_REQUEST_CODE = 1
        private const val UPDATE_SHOP_PLAN_FORM_REQUEST_CODE = 2
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUI()
    }

    private fun initializeUI(){
        initViewModel()
        initRecycleView()
        initComponents()
    }

    private fun initViewModel(){
        shopPlanFactory = InjectorUtils.provideShopPlanViewModelFactory(this)
        shopPlanViewModel = ViewModelProvider(this, shopPlanFactory)[ShopPlanViewModel::class.java]
        shopPlanViewModel.getShopPlans().observe(this) { shopPlans ->
            adapter.setShopPlans(shopPlans)
        }
    }

    private fun initRecycleView(){
        adapter = ShopPlanAdapter()
        rvShopPlans = findViewById(R.id.rvShopPlans)
        rvShopPlans.adapter = adapter
        rvShopPlans.layoutManager = LinearLayoutManager(this)
        rvShopPlans.setHasFixedSize(true)
    }

    private fun initComponents(){
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

            val shopPlan = ShopPlanModel(title=title.toString(), shopName=shop.toString(),
                totalCost=totalCost.toString().toDouble(), products=products as ArrayList<ProductModel>)
            Log.i(TAG, "onActivityResult retrieve: $shopPlan")
            if (requestCode == NEW_SHOP_PLAN_FORM_REQUEST_CODE) {
                Log.i(TAG, "onActivityResult NEW_SHOP_PLAN_FORM_REQUEST_CODE")
                adapter.addItem(shopPlan)
                shopPlanViewModel.addShopPlan(shopPlan)
            } else if (requestCode == UPDATE_SHOP_PLAN_FORM_REQUEST_CODE) {
                Log.i(TAG, "onActivityResult UPDATE_SHOP_PLAN_FORM_REQUEST_CODE")
                adapter.updateItem(shopPlan)
            }
        }
    }

    fun openShopPlanForm(view: View) {
        val intent = Intent(this, ShopPlanFormActivity::class.java)
        startActivityForResult(intent, NEW_SHOP_PLAN_FORM_REQUEST_CODE)
    }
}

