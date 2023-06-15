package com.example.shopplan.viewmodel.shopplan

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopplan.api.currency.FixerApiEndPoint.getExchangeRates
import com.example.shopplan.model.repository.CurrencyRepository
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.model.table.ShopPlanModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShopPlanViewModel(
    private val shopPlanRepository: ShopPlanRepository,
    private val currencyRepository: CurrencyRepository
) : ViewModel() {
    private val TAG = "ShopPlanViewModel"
    private var exchangeCurrencyRates = MutableLiveData<Map<String, Double>?>()
    private val currentCurrency = MutableLiveData<String>()

    private fun fetchExchangeRates() {
        Log.i(TAG, "Fetching exchange rates")
        viewModelScope.launch {
            try {
                val rates = runBlocking { getExchangeRates() }
                exchangeCurrencyRates.value = rates
                Log.i(TAG, "fetchExchangeRates: $exchangeCurrencyRates")
            } catch (e: Exception) {
                // Handle any errors that occur during the API call
                Log.i(TAG, "fetchExchangeRates: $e")
            }
        }
    }

    fun onCurrencyChanged(currency: String) {
        Log.i(TAG, "onCurrencyChange: $currency")
        updateCurrentCurrency(currency)
        calculateCurrencyChange(currency)
    }

    private fun calculateCurrencyChange(currency: String) {
        if (exchangeCurrencyRates.value == null) {
            fetchExchangeRates()
        }
        val rate = exchangeCurrencyRates.value!![currency]
        val shopPlans = shopPlanRepository.getSourceShopPlans()
        shopPlans.forEach { recalculateShopPlan(it, rate!!, false) }
        shopPlanRepository.setShopPlans(shopPlans)
    }

    fun addShopPlan(shopPlan: ShopPlanModel) {
        if (exchangeCurrencyRates.value == null) {
            fetchExchangeRates()
        }
        val rate = exchangeCurrencyRates.value!![getCurrency()]
        var baseCurrencyShopPlan: ShopPlanModel = recalculateShopPlan(shopPlan.copy(), rate!!, true)
        Log.i(TAG, "Add baseCurrencyShopPlan: $baseCurrencyShopPlan")
        Log.i(TAG, "Add shopPlan: $shopPlan")
        shopPlanRepository.addShopPlan(baseCurrencyShopPlan, shopPlan)
    }

    fun updateShopPlan(shopPlan: ShopPlanModel) {
        val rate = exchangeCurrencyRates.value!![getCurrency()]
        var baseCurrencyShopPlan: ShopPlanModel = recalculateShopPlan(shopPlan.copy(), rate!!, true)
        shopPlanRepository.updateShopPlan(baseCurrencyShopPlan, shopPlan)
    }

    fun getShopPlans() = shopPlanRepository.getShopPlans()

    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.deleteShopPlan(shopPlan)
    }

    private fun updateCurrentCurrency(currency: String) {
        currencyRepository.setCurrency(currency)
        currentCurrency.value = currency
    }

    fun getCurrency(): String {
        return currencyRepository.getCurrentCurrency()
    }

    fun getSymbol(): String {
        Log.i(TAG, "getCurrentCurrency: ${currencyRepository.getCurrentCurrency()}")
        Log.i(TAG, "getSymbol: ${currencyRepository.getSymbol()}")
        return currencyRepository.getSymbol()
    }

    private fun recalculateShopPlan(
        shopPlan: ShopPlanModel,
        rate: Double,
        isConvert: Boolean
    ): ShopPlanModel {
        shopPlan.products.forEach {
            it.price = convertCurrency(it.price, rate, isConvert)
        }
        shopPlan.totalCost = convertCurrency(shopPlan.totalCost, rate, isConvert)
        return shopPlan
    }

    private fun convertCurrency(amount: Double, exchangeRate: Double?, isConvert: Boolean): Double {
        if (isConvert) {
            return amount / exchangeRate!!
        }
        return amount * exchangeRate!!
    }
}