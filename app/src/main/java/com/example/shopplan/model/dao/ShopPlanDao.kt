package com.example.shopplan.model.dao

import androidx.lifecycle.MutableLiveData
import com.example.shopplan.model.menager.ShopPlanManager
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanDao(shopPlanDbHelper: ShopPlanDbHelper) {
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

    fun getShopPlans() = shopPlans as MutableLiveData<List<ShopPlanManager>>
}