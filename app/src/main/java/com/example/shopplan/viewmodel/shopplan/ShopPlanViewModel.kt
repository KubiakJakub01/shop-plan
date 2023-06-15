package com.example.shopplan.viewmodel.shopplan

import android.util.Log
import androidx.compose.ui.layout.LookaheadLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopplan.model.repository.CurrencyRepository
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.api.currency.FixerApiEndPoint.getExchangeRates
import com.example.shopplan.model.table.CurrencyConstants
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShopPlanViewModel(
    private val shopPlanRepository: ShopPlanRepository,
    private val currencyRepository: CurrencyRepository) : ViewModel() {
    private val TAG = "ShopPlanViewModel"
    private var exchangeCurrencyRates = MutableLiveData<Map<String, Double>?>()
    private val currentCurrency = MutableLiveData<String>()

    private fun fetchExchangeRates() {
        Log.i(TAG, "Fetching exchange rates")
        viewModelScope.launch {
            try {
                val rates  = runBlocking { getExchangeRates() }
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
        shopPlans.forEach { it ->
            it.totalCost = convertCurrency(it.totalCost, rate, false)
            it.products.forEach { it ->
                it.price = convertCurrency(it.price, rate, false)
            }
        }
        shopPlanRepository.setShopPlans(shopPlans)
    }

    fun addShopPlan(shopPlan: ShopPlanModel) {
        var originalShopPlan = shopPlan.copy()
        val rate = exchangeCurrencyRates.value!![getCurrency()]
        shopPlan.products.forEach {
            it.price = convertCurrency(it.price, rate, true)
        }
        shopPlan.totalCost = convertCurrency(shopPlan.totalCost, rate, true)
        shopPlanRepository.addShopPlan(shopPlan, originalShopPlan)
    }

    fun getShopPlans() = shopPlanRepository.getShopPlans()

    fun updateShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.updateShopPlan(shopPlan)
    }

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

    private fun convertCurrency(amount: Double, exchangeRate: Double?, isConvert: Boolean): Double {
        if (isConvert) {
            return amount / exchangeRate!!
        }
        return amount * exchangeRate!!
    }
}