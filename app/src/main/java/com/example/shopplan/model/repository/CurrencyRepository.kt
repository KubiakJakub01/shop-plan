package com.example.shopplan.model.repository

import android.util.Log
import com.example.shopplan.model.dao.CurrencyDao
import com.example.shopplan.model.table.CurrencyConstants
import com.example.shopplan.model.table.CurrencyModel

class CurrencyRepository private constructor(private val currencyDao: CurrencyDao) {
    private val TAG = "CurrencyRepository"
    fun updateCurrency(currencyModel: CurrencyModel) {
        currencyDao.updateCurrency(currencyModel)
    }

    fun setCurrency(currency: String) {
        Log.i(TAG, "setCurrency: $currency")
        val currencyModel = CurrencyModel(
            currency = currency,
            symbol = CurrencyConstants.currencySymbols[currency]
        )
        currencyDao.updateCurrency(currencyModel)
    }

    fun getCurrentCurrency(): String {
        return currencyDao.getCurrentCurrency()
    }

    fun getBaseCurrency(): String {
        return currencyDao.getBaseCurrency()
    }

    fun getSymbol(): String {
        return currencyDao.getSymbol()
    }

    companion object {
        @Volatile
        private var instance: CurrencyRepository? = null

        fun getInstance(currencyDao: CurrencyDao) =
            instance ?: synchronized(this) {
                instance ?: CurrencyRepository(currencyDao).also { instance = it }
            }
    }
}