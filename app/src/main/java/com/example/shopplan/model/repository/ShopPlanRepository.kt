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

    fun addShopPlan(shopPlan: ShopPlanModel, originalShopPlan: ShopPlanModel){
        shopPlanDao.addShopPlan(shopPlan, originalShopPlan)
    }

    fun getShopPlans() = shopPlanDao.getShopPlans()
    fun updateShopPlan(shopPlan: ShopPlanModel) {
        shopPlanDao.updateShopPlan(shopPlan)
    }

    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        shopPlanDao.deleteShopPlan(shopPlan)
    }

    fun getSourceShopPlans() = shopPlanDao.getSourceShopPlans()
    fun setShopPlans(shopPlans: List<ShopPlanModel>) {
        shopPlanDao.setShopPlans(shopPlans)
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