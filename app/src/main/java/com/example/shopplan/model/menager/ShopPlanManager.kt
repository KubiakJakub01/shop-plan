package com.example.shopplan.model.menager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.shopplan.model.contract.ShopPlanContract
import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.table.ProductModel
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanManager(private val dbHelper: ShopPlanDbHelper) {

    fun addShopPlan(shopPlan: ShopPlanModel) {
        val db = dbHelper.writableDatabase

        val shopPlanValues = ContentValues().apply {
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TITLE, shopPlan.title)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME, shopPlan.shopName)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST, shopPlan.totalCost)
        }

        val shopPlanId = db.insert(ShopPlanContract.ShopPlanEntry.TABLE_NAME, null, shopPlanValues)

        for (product in shopPlan.products) {
            val productValues = ContentValues().apply {
                put(ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID, shopPlanId)
                put(ShopPlanContract.ProductEntry.COLUMN_NAME, product.name)
                put(ShopPlanContract.ProductEntry.COLUMN_PRICE, product.price)
                put(ShopPlanContract.ProductEntry.COLUMN_QUANTITY, product.quantity)
            }
            db.insert(ShopPlanContract.ProductEntry.TABLE_NAME, null, productValues)
        }

        db.close()
    }

    @SuppressLint("Range")
    fun getShopPlans(): List<ShopPlanModel> {
        val db = dbHelper.readableDatabase
        val shopPlanList = mutableListOf<ShopPlanModel>()

        val projection = arrayOf(
            BaseColumns._ID,
            ShopPlanContract.ShopPlanEntry.COLUMN_TITLE,
            ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME,
            ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST
        )

        val cursor = db.query(
            ShopPlanContract.ShopPlanEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val shopPlanId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
            val title =
                cursor.getString(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_TITLE))
            val shopName =
                cursor.getString(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME))
            val totalCost =
                cursor.getDouble(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST))

            val products = getProductsForShopPlan(db, shopPlanId)

            val shopPlan = ShopPlanModel(title, shopName, totalCost, products)
            shopPlanList.add(shopPlan)
        }

        cursor.close()
        db.close()

        return shopPlanList
    }

    @SuppressLint("Range")
    private fun getProductsForShopPlan(db: SQLiteDatabase, shopPlanId: Long): List<ProductModel> {
        val projection = arrayOf(
            BaseColumns._ID,
            ShopPlanContract.ProductEntry.COLUMN_NAME,
            ShopPlanContract.ProductEntry.COLUMN_PRICE,
            ShopPlanContract.ProductEntry.COLUMN_QUANTITY
        )

        val selection = "${ShopPlanContract.ProductEntry.COLUMN_SHOP_PLAN_ID} = ?"
        val selectionArgs = arrayOf(shopPlanId.toString())

        val cursor = db.query(
            ShopPlanContract.ProductEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val productList = mutableListOf<ProductModel>()

        while (cursor.moveToNext()) {
            val name =
                cursor.getString(cursor.getColumnIndex(ShopPlanContract.ProductEntry.COLUMN_NAME))
            val price =
                cursor.getDouble(cursor.getColumnIndex(ShopPlanContract.ProductEntry.COLUMN_PRICE))
            val quantity =
                cursor.getInt(cursor.getColumnIndex(ShopPlanContract.ProductEntry.COLUMN_QUANTITY))

            val product = ProductModel(name, price, quantity)
            productList.add(product)
        }

        cursor.close()

        return productList
    }

    // TODO: Implement other CRUD operations (update, delete) as needed

    public fun deleteShopPlan(shopPlan: ShopPlanModel) {
        val db = dbHelper.writableDatabase

        val selection = "${ShopPlanContract.ShopPlanEntry.COLUMN_TITLE} LIKE ?"
        val selectionArgs = arrayOf(shopPlan.title)

        db.delete(ShopPlanContract.ShopPlanEntry.TABLE_NAME, selection, selectionArgs)

        db.close()
    }

    fun updateShopPlan(shopPlan: ShopPlanModel) {
        val db = dbHelper.writableDatabase

        val selection = "${ShopPlanContract.ShopPlanEntry.COLUMN_TITLE} LIKE ?"
        val selectionArgs = arrayOf(shopPlan.title)

        val shopPlanValues = ContentValues().apply {
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TITLE, shopPlan.title)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME, shopPlan.shopName)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST, shopPlan.totalCost)
        }

        db.update(ShopPlanContract.ShopPlanEntry.TABLE_NAME, shopPlanValues, selection, selectionArgs)

        db.close()
    }
}