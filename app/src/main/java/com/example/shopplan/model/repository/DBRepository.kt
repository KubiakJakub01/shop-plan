package com.example.shopplan.model.repository

import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.menager.ProductManager
import com.example.shopplan.model.menager.ShopPlanManager

class DBRepository (dbHelper: ShopPlanDbHelper) {
    private var dbHelper: ShopPlanDbHelper
    private var shopPlanManager: ShopPlanManager
    private var productManager: ProductManager

    init {
        this.dbHelper = dbHelper
        shopPlanManager = ShopPlanManager(dbHelper)
        productManager = ProductManager(dbHelper)
    }

    fun getShopPlanManager(): ShopPlanManager {
        return shopPlanManager
    }

    fun getProductManager(): ProductManager {
        return productManager
    }
}