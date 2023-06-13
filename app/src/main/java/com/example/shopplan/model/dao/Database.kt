package com.example.shopplan.model.dao

import android.content.Context

class Database private constructor(context: Context){
    private var dbHelper: ShopPlanDbHelper
    private var shopPlanDao: ShopPlanDao

    init {
        this.dbHelper = ShopPlanDbHelper(context)
        this.shopPlanDao = ShopPlanDao(dbHelper)
    }

    companion object {
        @Volatile
        private var instance: Database? = null

        fun getInstance(context: Context): Database {
            return instance ?: synchronized(this) {
                instance ?: Database(context.applicationContext).also { instance = it }
            }
        }
    }

    fun getShopPlanDao() = shopPlanDao
}