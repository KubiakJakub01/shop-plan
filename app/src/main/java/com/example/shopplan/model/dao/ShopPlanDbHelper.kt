package com.example.shopplan.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopplan.model.contract.ShopPlanContract

class ShopPlanDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "shop_plan.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ShopPlanContract.SQL_CREATE_SHOP_PLAN_TABLE)
        db.execSQL(ShopPlanContract.SQL_CREATE_PRODUCT_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ShopPlanContract.SQL_DELETE_SHOP_PLAN_TABLE)
        db.execSQL(ShopPlanContract.SQL_DELETE_PRODUCT_TABLE)
        onCreate(db)
    }
}