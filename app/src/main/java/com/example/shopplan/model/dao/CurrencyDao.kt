package com.example.shopplan.model.dao

import com.example.shopplan.model.menager.CurrencyManager
import com.example.shopplan.model.table.CurrencyModel

class CurrencyDao(shopPlanDbHelper: ShopPlanDbHelper) {
    private val TAG = "CurrencyDao"
    private var currencyManager: CurrencyManager
    private var currencyModel: CurrencyModel

    init {
        currencyManager = CurrencyManager(shopPlanDbHelper)
        currencyModel = currencyManager.getCurrentCurrency()!!
    }

    fun updateCurrency(currencyModel: CurrencyModel) {
        currencyManager.setCurrentCurrency(currencyModel)
    }

    fun getCurrency(): CurrencyModel {
        return currencyModel
    }

    fun getCurrentCurrency(): String {
        return currencyModel.currency
    }

    fun getBaseCurrency(): String {
        return currencyModel.baseCurrency
    }

    fun getSymbol(): String {
        return currencyModel.symbol.toString()
    }
}