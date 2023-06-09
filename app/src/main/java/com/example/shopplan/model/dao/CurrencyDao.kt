package com.example.shopplan.model.dao

import androidx.lifecycle.MutableLiveData
import com.example.shopplan.model.menager.CurrencyManager
import com.example.shopplan.model.table.CurrencyModel

class CurrencyDao(shopPlanDbHelper: ShopPlanDbHelper) {
    private val TAG = "CurrencyDao"
    private var currencyManager: CurrencyManager
    private var currencyModel: CurrencyModel
    private val currentCurrency = MutableLiveData<String>()
    private val currencySymbol = MutableLiveData<String>()

    init {
        currencyManager = CurrencyManager(shopPlanDbHelper)
        currencyModel = currencyManager.getCurrentCurrency()!!
        currentCurrency.value = currencyModel.currency
        currencySymbol.value = currencyModel.symbol.toString()
    }

    fun updateCurrency(currencyModel: CurrencyModel) {
        currencyManager.setCurrentCurrency(currencyModel)
        currentCurrency.value = currencyModel.currency
        currencySymbol.value = currencyModel.symbol.toString()
    }

    fun getCurrency(): CurrencyModel {
        return currencyModel
    }

    fun getCurrentCurrency(): String {
        return currentCurrency.value!!
    }

    fun getBaseCurrency(): String {
        return currencyModel.baseCurrency
    }

    fun getSymbol(): String {
        return currencySymbol.value!!
    }
}