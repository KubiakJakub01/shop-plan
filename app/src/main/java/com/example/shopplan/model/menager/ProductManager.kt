package com.example.shopplan.model.menager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import com.example.shopplan.model.contract.ShopPlanContract
import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.table.ProductModel

class ProductManager(private val dbHelper: ShopPlanDbHelper) {
    private val TAG = "ProductManager"
    fun addProduct(product: ProductModel, shopPlanId: Int) {
        Log.i(TAG, "addProduct: $product to shopPlanId = $shopPlanId")
        val db = dbHelper.writableDatabase

        val productValues = ContentValues().apply {
            put(ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID, shopPlanId)
            put(ShopPlanContract.ProductEntry.COLUMN_PRODUCT_ID, product.productID)
            put(ShopPlanContract.ProductEntry.COLUMN_NAME, product.name)
            put(ShopPlanContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ShopPlanContract.ProductEntry.COLUMN_QUANTITY, product.quantity)
        }

        db.insert(ShopPlanContract.ProductEntry.TABLE_NAME, null, productValues)

        db.close()
    }

    @SuppressLint("Range")
    fun getProductsForShopPlan(shopPlanId: String): List<ProductModel> {
        val db = dbHelper.readableDatabase
        val products = mutableListOf<ProductModel>()

        val projection = arrayOf(
            ShopPlanContract.ProductEntry.COLUMN_PRODUCT_ID,
            ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID,
            ShopPlanContract.ProductEntry.COLUMN_NAME,
            ShopPlanContract.ProductEntry.COLUMN_PRICE,
            ShopPlanContract.ProductEntry.COLUMN_QUANTITY
        )

        val selection = "${ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID} = ?"
        val selectionArgs = arrayOf(shopPlanId)

        val cursor: Cursor = db.query(
            ShopPlanContract.ProductEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val productId = cursor.getInt(cursor.getColumnIndexOrThrow(ShopPlanContract.ProductEntry.COLUMN_PRODUCT_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(ShopPlanContract.ProductEntry.COLUMN_NAME))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(ShopPlanContract.ProductEntry.COLUMN_PRICE))
            val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ShopPlanContract.ProductEntry.COLUMN_QUANTITY))

            val product = ProductModel(productId, name, price, quantity)
            products.add(product)
        }
        Log.i(TAG, "getProductsForShopPlan: $products")

        cursor.close()
        db.close()

        return products
    }

    fun deleteProduct(shopPlanId: Int, productId: Int) {
        val db = dbHelper.writableDatabase

        val selection = "${ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID} = ? AND ${ShopPlanContract.ProductEntry.COLUMN_PRODUCT_ID} = ?"
        val selectionArgs = arrayOf(shopPlanId.toString(), productId.toString())

        db.delete(ShopPlanContract.ProductEntry.TABLE_NAME, selection, selectionArgs)

        db.close()
    }

    fun replaceProducts(products: List<ProductModel>, shopPlanId: Int) {
        val db = dbHelper.writableDatabase

        val selection = "${ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID} = ?"
        val selectionArgs = arrayOf(shopPlanId.toString())

        db.delete(ShopPlanContract.ProductEntry.TABLE_NAME, selection, selectionArgs)

        for (product in products) {
            addProduct(product, shopPlanId)
        }

        db.close()
    }
    fun updateProduct(product: ProductModel, shopPlanId: Int) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(ShopPlanContract.ProductEntry.COLUMN_NAME, product.name)
            put(ShopPlanContract.ProductEntry.COLUMN_PRICE, product.price)
            put(ShopPlanContract.ProductEntry.COLUMN_QUANTITY, product.quantity)
        }

        val selection = "${ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID} = ? AND ${ShopPlanContract.ProductEntry.COLUMN_PRODUCT_ID} = ?"
        val selectionArgs = arrayOf(shopPlanId.toString(), product.productID.toString())

        db.update(ShopPlanContract.ProductEntry.TABLE_NAME, values, selection, selectionArgs)

        db.close()
    }
}