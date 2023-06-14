package com.example.shopplan.model.repository

import com.example.shopplan.model.dao.ShopPlanDao
import com.example.shopplan.model.table.ShopPlanModel

/**
 * Repository singleton class for managing database access
 */
class ShopPlanRepository private constructor(private val shopPlanDao: ShopPlanDao) {

    fun addShopPlan(shopPlan: ShopPlanModel) {
        shopPlanDao.addShopPlan(shopPlan)
    }

    fun getShopPlans() = shopPlanDao.getShopPlans()
    fun updateShopPlan(shopPlan: ShopPlanModel) {
        shopPlanDao.updateShopPlan(shopPlan)
    }

    companion object {
        @Volatile
        private var instance: ShopPlanRepository? = null

        fun getInstance(shopPlanDao: ShopPlanDao): ShopPlanRepository {
            return instance ?: synchronized(this) {
                instance ?: ShopPlanRepository(shopPlanDao).also { instance = it }
            }
        }
    }
}