package com.example.shopplan.viewmodel.shopplan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopplan.model.repository.CurrencyRepository
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.api.currency.FixerApiEndPoint.getExchangeRates
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShopPlanViewModel(
    private val shopPlanRepository: ShopPlanRepository,
    private val currencyRepository: CurrencyRepository) : ViewModel() {
    private val TAG = "ShopPlanViewModel"
    private var exchangeCurrencyRates = MutableLiveData<Map<String, Double>?>()
    private val currentCurrency = MutableLiveData<String>()
    private val baseCurrency = currencyRepository.getBaseCurrency()

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
        if (currency != baseCurrency) {
            updateCurrentCurrency(currency)
            calculateCurrencyChange(currency)
        }
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
        var currentCurrency = getCurrency()
        if (currentCurrency != baseCurrency) {
                    val rate = exchangeCurrencyRates.value!![getCurrency()]
                    shopPlan.products.forEach {
                        it.price = convertCurrency(it.price, rate, true)
                    }
                    shopPlan.totalCost = convertCurrency(shopPlan.totalCost, rate, true)
                }
        shopPlanRepository.addShopPlan(shopPlan)
    }

    fun getShopPlans() = shopPlanRepository.getShopPlans()

    private fun getUpdatedShopPlans(): LiveData<List<ShopPlanModel>> {
        if (exchangeCurrencyRates.value == null) {
            fetchExchangeRates()
        }
        val rate = exchangeCurrencyRates.value!![getCurrency()]
        return Transformations.map(shopPlanRepository.getShopPlans()) { shopPlans ->
            val updatedShopPlans = shopPlans.map { shopPlan ->
                val convertedTotalCost = convertCurrency(shopPlan.totalCost, rate, false)
                shopPlan.products.forEach {
                    it.price = convertCurrency(it.price, rate, false)
                }
                shopPlan.copy(totalCost = convertedTotalCost)
            }
            updatedShopPlans
        }
    }


    fun updateShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.updateShopPlan(shopPlan)
    }

    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.deleteShopPlan(shopPlan)
    }

    fun updateCurrentCurrency(currency: String) {
        currencyRepository.setCurrency(currency)
        currentCurrency.value = currency
    }

    fun getCurrency(): String {
        return currencyRepository.getCurrentCurrency()
    }

    fun getSymbol(): String {
        return currencyRepository.getSymbol()
    }

    private fun convertCurrency(amount: Double, exchangeRate: Double?, isConvert: Boolean): Double {
        if (isConvert) {
            return amount / exchangeRate!!
        }
        return amount * exchangeRate!!
    }
}