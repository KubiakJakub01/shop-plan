package com.example.shopplan.viewmodel.shopplanform

import androidx.lifecycle.ViewModel
import com.example.shopplan.model.repository.ProductRepository
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.model.table.ProductModel

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    fun addProduct(product: ProductModel, shopPlanId: Int) {
        productRepository.addProduct(product, shopPlanId)
    }

    fun getProducts() = productRepository.getProducts()

    fun updateProduct(product: ProductModel) {
        productRepository.updateProduct(product)
    }
}