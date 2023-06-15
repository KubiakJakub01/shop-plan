package com.example.shopplan.viewmodel.shopplan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopplan.model.repository.CurrencyRepository
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.api.currency.FixerApiEndPoint.getExchangeRates
import kotlinx.coroutines.launch

class ShopPlanViewModel(
    private val shopPlanRepository: ShopPlanRepository,
    private val currencyRepository: CurrencyRepository) : ViewModel() {
    private val TAG = "ShopPlanViewModel"
    private lateinit var exchangeCurrencyRates: Map<String, Double>
    private val baseCurrency = currencyRepository.getBaseCurrency()

    init {
        fetchExchangeRates()
    }
    private fun fetchExchangeRates() {
        viewModelScope.launch {
            try {
                exchangeCurrencyRates = getExchangeRates()!!
                // Process the exchange rates data
                Log.i(TAG, "fetchExchangeRates: $exchangeCurrencyRates")
            } catch (e: Exception) {
                // Handle any errors that occur during the API call
                Log.i(TAG, "fetchExchangeRates: $e")
            }
        }
    }
    fun addShopPlan(shopPlan: ShopPlanModel) {
        var currentCurrency = getCurrency()
        if (currentCurrency != baseCurrency) {
                    val rate = exchangeCurrencyRates[currentCurrency]
                    shopPlan.products.forEach {
                        it.price = convertCurrency(it.price, rate, true)
                    }
                    shopPlan.totalCost = convertCurrency(shopPlan.totalCost, rate, true)
                }
        shopPlanRepository.addShopPlan(shopPlan)
    }

    fun getShopPlans(): LiveData<List<ShopPlanModel>> {
        val shopPlansLiveData = shopPlanRepository.getShopPlans()
        if (getCurrency() != baseCurrency) {
            val rate = exchangeCurrencyRates[getCurrency()]
            val transformedLiveData = Transformations.map(shopPlansLiveData) { shopPlans ->
                shopPlans.map { shopPlan ->
                    val convertedTotalCost = convertCurrency(shopPlan.totalCost, rate, false)
                    shopPlan.products.forEach {
                        it.price = convertCurrency(it.price, rate, false)
                    }
                    shopPlan.copy(totalCost = convertedTotalCost)
                }
            }
            return transformedLiveData
        }
        return shopPlansLiveData
    }


    fun updateShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.updateShopPlan(shopPlan)
    }

    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        shopPlanRepository.deleteShopPlan(shopPlan)
    }

    fun setCurrency(currency: String) {
        currencyRepository.setCurrency(currency)
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