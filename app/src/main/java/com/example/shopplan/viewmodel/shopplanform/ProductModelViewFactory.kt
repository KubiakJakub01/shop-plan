package com.example.shopplan.viewmodel.shopplanform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopplan.model.repository.ProductRepository
import com.example.shopplan.model.repository.ShopPlanRepository

class ProductModelViewFactory(private val productRepository: ProductRepository) :
    ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(productRepository) as T
    }
}