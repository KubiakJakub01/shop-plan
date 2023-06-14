package com.example.shopplan.model.dao

import androidx.lifecycle.MutableLiveData
import com.example.shopplan.model.menager.ProductManager
import com.example.shopplan.model.table.ProductModel

class ProductDao(shopPlanDbHelper: ShopPlanDbHelper) {
    private val TAG = "ProductDao"
    private var productList = mutableListOf<ProductModel>()
    private var products = MutableLiveData<List<ProductModel>>()
    private var productManager: ProductManager

    init {
        productManager = ProductManager(shopPlanDbHelper)
        productList = productManager.getProducts().toMutableList()
        products.value = productList
    }

    fun addProduct(product: ProductModel, shopPlanId: Int) {
        productList.add(product)
        productManager.addProduct(product, shopPlanId)
    }

    fun getProducts() = productList

    fun updateProduct(product: ProductModel) {
        val index = productList.indexOfFirst { it.productID == product.productID }
        productList[index] = product
        productManager.updateProduct(product)
    }
}