package com.example.shopplan.view.shopplan.adapter

import com.example.shopplan.model.table.ShopPlanModel

interface ShopPlanActionListener {
    fun openShopPlanFormActivity(shopPlan: ShopPlanModel)
    fun deleteShopPlan(shopPlan: ShopPlanModel)
}