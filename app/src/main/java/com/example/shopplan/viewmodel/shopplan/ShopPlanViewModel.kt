package com.example.shopplan.viewmodel.shopplan

import androidx.lifecycle.ViewModel
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanViewModel(private val shopPlanRepository: ShopPlanRepository) : ViewModel() {

    fun addShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.addShopPlan(shopPlan)
    }

    fun getShopPlans() = shopPlanRepository.getShopPlans()

    fun updateShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.updateShopPlan(shopPlan)
    }

    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.deleteShopPlan(shopPlan)
    }
}