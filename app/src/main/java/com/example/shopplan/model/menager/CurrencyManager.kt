package com.example.shopplan.model.menager

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.shopplan.model.contract.ShopPlanContract
import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.table.CurrencyModel

class CurrencyManager(private val dbHelper: ShopPlanDbHelper) {
    fun addCurrency(currency: CurrencyModel) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(ShopPlanContract.CurrencyEntry.COLUMN_CURRENCY, currency.currency)
            put(ShopPlanContract.CurrencyEntry.COLUMN_BASE_CURRENCY, currency.baseCurrency)
            put(ShopPlanContract.CurrencyEntry.COLUMN_SYMBOL, currency.symbol)
        }

        db.insert(ShopPlanContract.CurrencyEntry.TABLE_NAME, null, values)

        db.close()
    }

    fun getCurrentCurrency(): CurrencyModel? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            ShopPlanContract.CurrencyEntry.COLUMN_BASE_CURRENCY,
            ShopPlanContract.CurrencyEntry.COLUMN_CURRENCY,
            ShopPlanContract.CurrencyEntry.COLUMN_SYMBOL
        )
        val cursor = db.query(
            ShopPlanContract.CurrencyEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        var currencyModel: CurrencyModel? = null
        if (cursor.moveToFirst()) {
            val baseCurrency = cursor.getString(cursor.getColumnIndexOrThrow(ShopPlanContract.CurrencyEntry.COLUMN_BASE_CURRENCY))
            val currency = cursor.getString(cursor.getColumnIndexOrThrow(ShopPlanContract.CurrencyEntry.COLUMN_CURRENCY))
            val symbol = cursor.getString(cursor.getColumnIndexOrThrow(ShopPlanContract.CurrencyEntry.COLUMN_SYMBOL))
            currencyModel = CurrencyModel(baseCurrency, currency, symbol)
        }
        cursor.close()
        db.close()
        return currencyModel
    }

    fun setCurrentCurrency(currency: CurrencyModel) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ShopPlanContract.CurrencyEntry.COLUMN_BASE_CURRENCY, currency.baseCurrency)
            put(ShopPlanContract.CurrencyEntry.COLUMN_CURRENCY, currency.currency)
            put(ShopPlanContract.CurrencyEntry.COLUMN_SYMBOL, currency.symbol)
        }
        db.insertWithOnConflict(
            ShopPlanContract.CurrencyEntry.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
    }
}