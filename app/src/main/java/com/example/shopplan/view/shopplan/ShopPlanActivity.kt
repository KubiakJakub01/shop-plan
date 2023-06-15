package com.example.shopplan.view.shopplan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.R
import com.example.shopplan.model.table.CurrencyConstants
import com.example.shopplan.model.table.ProductModel
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.view.shopplanform.ShopPlanFormActivity
import com.example.shopplan.utils.InjectorUtils
import com.example.shopplan.view.shopplan.adapter.ShopPlanActionListener
import com.example.shopplan.view.shopplan.adapter.ShopPlanAdapter
import com.example.shopplan.viewmodel.shopplan.ShopPlanModelViewFactory
import com.example.shopplan.viewmodel.shopplan.ShopPlanViewModel

class ShopPlanActivity : ComponentActivity() {

    private val TAG = "ShopPlanActivity"
    private lateinit var adapter: ShopPlanAdapter
    private lateinit var rvShopPlans: RecyclerView
    private lateinit var shopPlanFactory: ShopPlanModelViewFactory
    private lateinit var shopPlanViewModel: ShopPlanViewModel
    private lateinit var btnCreateShopPlan: Button
    private lateinit var spinnerCurrency: Spinner

    companion object {
        private const val NEW_SHOP_PLAN_FORM_REQUEST_CODE = 1
        private const val UPDATE_SHOP_PLAN_FORM_REQUEST_CODE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeUI()
    }

    private fun initializeUI() {
        initViewModel()
        initRecycleView()
        initComponents()
    }

    private fun initViewModel() {
        shopPlanFactory = InjectorUtils.provideShopPlanViewModelFactory(this)
        shopPlanViewModel = ViewModelProvider(this, shopPlanFactory)[ShopPlanViewModel::class.java]
        shopPlanViewModel.getShopPlans().observe(this) { shopPlans ->
            Log.i(TAG, "shopPlans: $shopPlans")
            adapter.setShopPlans(shopPlans)
        }
    }

    private fun initRecycleView() {
        adapter = ShopPlanAdapter(object : ShopPlanActionListener {
            override fun openShopPlanFormActivity(shopPlan: ShopPlanModel) {
                Log.i(TAG, "onShopPlanClicked. shopPlan: $shopPlan")
                val intent = Intent(this@ShopPlanActivity, ShopPlanFormActivity::class.java)
                intent.putExtra("shopPlan", shopPlan)
                startActivityForResult(intent, UPDATE_SHOP_PLAN_FORM_REQUEST_CODE)
            }

            override fun deleteShopPlan(shopPlan: ShopPlanModel) {
                Log.i(TAG, "onShopPlanDeleteClicked. shopPlan: $shopPlan")
                shopPlanViewModel.deleteShopPlan(shopPlan)
            }
        })
        rvShopPlans = findViewById(R.id.rvShopPlans)
        rvShopPlans.adapter = adapter
        rvShopPlans.layoutManager = LinearLayoutManager(this)
        rvShopPlans.setHasFixedSize(true)
    }

    private fun initComponents() {
        btnCreateShopPlan = findViewById(R.id.btnCreateShopPlan)
        initSpinner()
    }

    private fun initSpinner(){
        spinnerCurrency = findViewById(R.id.spinnerCurrency)

        // Define a list of currencies
        val currencies = CurrencyConstants.currencyList

        // Create an ArrayAdapter using a simple spinner layout and currencies list
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)

        // Set layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the adapter to the spinner
        spinnerCurrency.adapter = spinnerAdapter

        // Set the default currency
        var defaultCurrency = shopPlanViewModel.getCurrency()
        Log.i(TAG, "initSpinner. defaultCurrency: $defaultCurrency")
        spinnerCurrency.setSelection(spinnerAdapter.getPosition(defaultCurrency))

        // Set an on item selected listener for spinner object
        spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long){
                var currency = parent?.getItemAtPosition(position).toString()
                Log.i(TAG, "onItemSelected. currency: $currency")
                shopPlanViewModel.onCurrencyChanged(currency)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.i(TAG, "onNothingSelected")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i(
            TAG,
            "onActivityResult. requestCode: $requestCode, resultCode: $resultCode, data: $data"
        )
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the request code is the same as what is passed here
        if (resultCode == Activity.RESULT_OK) {
            // Retrieve the information from the result intent
            val shopPlanID = data?.getIntExtra("shopPlanID", 0)
            val title = data?.getStringExtra("title")
            val shop = data?.getStringExtra("shopName")
            val totalCost = data?.getDoubleExtra("totalCost", 0.0)
            val products = data?.getParcelableArrayListExtra<ProductModel>("products")

            if (requestCode == NEW_SHOP_PLAN_FORM_REQUEST_CODE) {
                val shopPlan = ShopPlanModel(
                    title = title.toString(),
                    shopName = shop.toString(),
                    totalCost = totalCost.toString().toDouble(),
                    products = products as ArrayList<ProductModel>
                )
                Log.i(TAG, "onActivityResult NEW_SHOP_PLAN_FORM_REQUEST_CODE: $shopPlan")
                adapter.addItem(shopPlan)
                shopPlanViewModel.addShopPlan(shopPlan)
            } else if (requestCode == UPDATE_SHOP_PLAN_FORM_REQUEST_CODE) {
                val shopPlan = ShopPlanModel(
                    shopPlanID = shopPlanID!!,
                    title = title.toString(),
                    shopName = shop.toString(),
                    totalCost = totalCost.toString().toDouble(),
                    products = products as ArrayList<ProductModel>
                )
                Log.i(TAG, "onActivityResult UPDATE_SHOP_PLAN_FORM_REQUEST_CODE $shopPlan")
                adapter.updateItem(shopPlan)
                shopPlanViewModel.updateShopPlan(shopPlan)
            }
        }
    }

    fun openShopPlanForm(view: View) {
        val intent = Intent(this, ShopPlanFormActivity::class.java)
        startActivityForResult(intent, NEW_SHOP_PLAN_FORM_REQUEST_CODE)
    }
}

