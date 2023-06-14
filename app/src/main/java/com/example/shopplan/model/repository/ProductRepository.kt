package com.example.shopplan.model.repository

import com.example.shopplan.model.dao.ProductDao
import com.example.shopplan.model.table.ProductModel

class ProductRepository private constructor(private val productDao: ProductDao) {

    fun addProduct(product: ProductModel, shopPlanId: Int) {
        productDao.addProduct(product, shopPlanId)
    }

    fun getProducts() = productDao.getProducts()

    fun updateProduct(product: ProductModel) {
        productDao.updateProduct(product)
    }

    companion object {
        @Volatile
        private var instance: ProductRepository? = null

        fun getInstance(productDao: ProductDao) =
            instance ?: synchronized(this) {
                instance ?: ProductRepository(productDao).also { instance = it }
            }
    }
}
