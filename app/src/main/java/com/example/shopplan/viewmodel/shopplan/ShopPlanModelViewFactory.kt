package com.example.shopplan.viewmodel.shopplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopplan.model.repository.CurrencyRepository
import com.example.shopplan.model.repository.ShopPlanRepository

class ShopPlanModelViewFactory(
    private val shopPlanRepository: ShopPlanRepository,
    private val currencyRepository: CurrencyRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShopPlanViewModel(shopPlanRepository, currencyRepository) as T
    }
}