package com.example.shopplan.model.repository

import android.content.Context
import com.example.shopplan.model.dao.ShopPlanDao
import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.menager.ProductManager
import com.example.shopplan.model.menager.ShopPlanManager

/**
 * Repository singleton class for managing database access
 */
class DBRepository private constructor(context: Context) {
    private var dbHelper: ShopPlanDbHelper
    private var shopPlanDao: ShopPlanDao

    init {
        this.dbHelper = ShopPlanDbHelper(context)
        this.shopPlanDao = ShopPlanDao(dbHelper)
    }

    companion object {
        @Volatile
        private var instance: DBRepository? = null

        fun getInstance(context: Context): DBRepository {
            return instance ?: synchronized(this) {
                instance ?: DBRepository(context.applicationContext).also { instance = it }
            }
        }
    }

    fun getShopPlanDao(): ShopPlanDao {
        return shopPlanDao
    }
}