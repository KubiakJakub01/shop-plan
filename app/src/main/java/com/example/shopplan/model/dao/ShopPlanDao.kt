package com.example.shopplan.model.dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopplan.model.menager.ShopPlanManager
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanDao(shopPlanDbHelper: ShopPlanDbHelper) {
    private val TAG = "ShopPlanDao"
    private var shopPlanList = mutableListOf<ShopPlanModel>()
    private val shopPlans = MutableLiveData<List<ShopPlanModel>>()
    private var shopPlanManager: ShopPlanManager

    init {
        shopPlanManager = ShopPlanManager(shopPlanDbHelper)
        shopPlanList = shopPlanManager.getShopPlans().toMutableList()
        shopPlans.value = shopPlanList
    }

    fun addShopPlan(shopPlan: ShopPlanModel) {
        shopPlanList.add(shopPlan)
        shopPlans.value = shopPlanList
        shopPlanManager.addShopPlan(shopPlan)
    }

    fun addShopPlan(baseCurrencyShopPlan: ShopPlanModel, shopPlan: ShopPlanModel){
        shopPlanList.add(shopPlan)
        shopPlans.value = shopPlanList
        shopPlanManager.addShopPlan(baseCurrencyShopPlan)
    }

    fun getShopPlans() = shopPlans as LiveData<List<ShopPlanModel>>

    fun updateShopPlan(shopPlan: ShopPlanModel) {
        val index = shopPlanList.indexOfFirst { it.shopPlanID == shopPlan.shopPlanID }
        Log.i(TAG, "updateShopPlan: index = $index. shopPlan = $shopPlan")
        shopPlanList[index] = shopPlan
        shopPlans.value = shopPlanList
        shopPlanManager.updateShopPlan(shopPlan)
    }

    fun updateShopPlan(baseCurrencyShopPlan: ShopPlanModel, shopPlan: ShopPlanModel) {
        val index = shopPlanList.indexOfFirst { it.shopPlanID == shopPlan.shopPlanID }
        Log.i(TAG, "updateShopPlan: index = $index. shopPlan = $shopPlan")
        shopPlanList[index] = shopPlan
        shopPlans.value = shopPlanList
        shopPlanManager.updateShopPlan(baseCurrencyShopPlan)
    }


    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        shopPlanList.remove(shopPlan)
        shopPlans.value = shopPlanList
        shopPlanManager.deleteShopPlan(shopPlan)
    }

    fun getSourceShopPlans() = shopPlanManager.getShopPlans()

    fun setShopPlans(shopPlans: List<ShopPlanModel>) {
        shopPlanList = shopPlans.toMutableList()
        this.shopPlans.value = shopPlanList
    }
}