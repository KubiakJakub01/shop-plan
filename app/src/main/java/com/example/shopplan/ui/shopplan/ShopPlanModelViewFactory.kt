package com.example.shopplan.ui.shopplan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopplan.model.repository.ShopPlanRepository

class ShopPlanModelViewFactory(private val shopPlanRepository: ShopPlanRepository)
    : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShopPlanViewModel(shopPlanRepository) as T
    }
}