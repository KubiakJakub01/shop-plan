package com.example.shopplan.model.menager

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.example.shopplan.model.contract.ShopPlanContract
import com.example.shopplan.model.dao.ShopPlanDbHelper
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanManager(private val dbHelper: ShopPlanDbHelper) {
    private val TAG = "ShopPlanManager"
    private val productManager = ProductManager(dbHelper)
    fun addShopPlan(shopPlan: ShopPlanModel) {
        Log.i(TAG, "addShopPlan: $shopPlan")
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_PLAN_ID, shopPlan.shopPlanID)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TITLE, shopPlan.title)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME, shopPlan.shopName)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST, shopPlan.totalCost)
        }

        db.insert(ShopPlanContract.ShopPlanEntry.TABLE_NAME, null, values)

        shopPlan.products.forEach {
            productManager.addProduct(it, shopPlan.shopPlanID)
        }

        db.close()
    }

    @SuppressLint("Range")
    fun getShopPlans(): List<ShopPlanModel> {
        val db = dbHelper.readableDatabase

        val projection = arrayOf(
            ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_PLAN_ID,
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

        val shopPlans = mutableListOf<ShopPlanModel>()

        while (cursor.moveToNext()) {
            val shopPlanId =
                cursor.getInt(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_PLAN_ID))
            val title =
                cursor.getString(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_TITLE))
            val shopName =
                cursor.getString(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME))
            val totalCost =
                cursor.getDouble(cursor.getColumnIndex(ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST))

            val products = productManager.getProductsForShopPlan(shopPlanId.toString())

            shopPlans.add(ShopPlanModel(shopPlanId, title, shopName, totalCost, products))
        }
        Log.i(TAG, "getShopPlans: $shopPlans")

        cursor.close()
        db.close()

        return shopPlans
    }

    fun updateShopPlan(shopPlan: ShopPlanModel) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TITLE, shopPlan.title)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_NAME, shopPlan.shopName)
            put(ShopPlanContract.ShopPlanEntry.COLUMN_TOTAL_COST, shopPlan.totalCost)
        }

        val selection = "${ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_PLAN_ID} LIKE ?"
        val selectionArgs = arrayOf(shopPlan.shopPlanID.toString())

        db.update(ShopPlanContract.ShopPlanEntry.TABLE_NAME, values, selection, selectionArgs)

        productManager.replaceProducts(shopPlan.products, shopPlan.shopPlanID)

        db.close()
    }

    fun deleteShopPlan(shopPlan: ShopPlanModel) {
        val db = dbHelper.writableDatabase

        val selection = "${ShopPlanContract.ShopPlanEntry.COLUMN_SHOP_PLAN_ID} LIKE ?"
        val selectionArgs = arrayOf(shopPlan.shopPlanID.toString())

        db.delete(ShopPlanContract.ShopPlanEntry.TABLE_NAME, selection, selectionArgs)

        db.close()
    }
}