package com.example.shopplan.model.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shopplan.model.contract.ShopPlanContract
import com.example.shopplan.model.menager.CurrencyManager
import com.example.shopplan.model.table.CurrencyConstants
import com.example.shopplan.model.table.CurrencyModel

class ShopPlanDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private var baseCurrency = CurrencyConstants.EUR
    private var symbol = CurrencyConstants.currencySymbols[baseCurrency]

    companion object {
        const val DATABASE_NAME = "shop_plan.db"
        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ShopPlanContract.SQL_CREATE_SHOP_PLAN_TABLE)
        db.execSQL(ShopPlanContract.SQL_CREATE_PRODUCT_TABLE)
        db.execSQL(ShopPlanContract.SQL_CREATE_CURRENCY_TABLE)

        insertInitialCurrencyData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(ShopPlanContract.SQL_DELETE_SHOP_PLAN_TABLE)
        db.execSQL(ShopPlanContract.SQL_DELETE_PRODUCT_TABLE)
        onCreate(db)
    }

    private fun insertInitialCurrencyData(db: SQLiteDatabase) {
        val currencyValues = ContentValues().apply {
            put(ShopPlanContract.CurrencyEntry.COLUMN_BASE_CURRENCY, baseCurrency)
            put(ShopPlanContract.CurrencyEntry.COLUMN_CURRENCY, baseCurrency)
            put(ShopPlanContract.CurrencyEntry.COLUMN_SYMBOL, symbol)
        }

        db.insert(ShopPlanContract.CurrencyEntry.TABLE_NAME, null, currencyValues)
    }
}