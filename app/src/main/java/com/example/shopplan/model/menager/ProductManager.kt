package com.example.shopplan.model.menager

import android.content.ContentValues
import com.example.shopplan.model.contract.ShopPlanContract
import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.table.ProductModel

class ProductManager(private val dbHelper: ShopPlanDbHelper) {

    fun addProduct(shopPlanId: Long, product: ProductModel) {
        val db = dbHelper.writableDatabase

        val productValues = ContentValues().apply {
            put(ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID, shopPlanId)
            put(ShopPlanContract.ProductEntry.COLUMN_NAME, product.name)
            put(ShopPlanContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ShopPlanContract.ProductEntry.COLUMN_QUANTITY, product.quantity)
        }

        db.insert(ShopPlanContract.ProductEntry.TABLE_NAME, null, productValues)

        db.close()
    }

    // TODO: Implement other CRUD operations (read, update, delete) as needed

    // ...
}