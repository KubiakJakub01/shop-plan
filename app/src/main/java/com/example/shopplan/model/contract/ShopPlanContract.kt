package com.example.shopplan.model.contract

import android.provider.BaseColumns

object ShopPlanContract {
    // Define table names
    object ShopPlanEntry {
        const val TABLE_NAME = "shop_plans"
        const val COLUMN_TITLE = "title"
        const val COLUMN_SHOP_NAME = "shop_name"
        const val COLUMN_TOTAL_COST = "total_cost"
    }

    object ProductEntry {
        const val TABLE_NAME = "products"
        const val COLUMN_SHOP_PLAN_ID = "shop_plan_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
    }

    // Define SQL queries
    const val SQL_CREATE_SHOP_PLAN_TABLE = """
        CREATE TABLE ${ShopPlanEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${ShopPlanEntry.COLUMN_TITLE} TEXT,
            ${ShopPlanEntry.COLUMN_SHOP_NAME} TEXT,
            ${ShopPlanEntry.COLUMN_TOTAL_COST} REAL
        )
    """

    const val SQL_CREATE_PRODUCT_TABLE = """
        CREATE TABLE ${ProductEntry.TABLE_NAME} (
            ${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${ProductEntry.COLUMN_SHOP_PLAN_ID} INTEGER,
            ${ProductEntry.COLUMN_NAME} TEXT,
            ${ProductEntry.COLUMN_PRICE} REAL,
            ${ProductEntry.COLUMN_QUANTITY} INTEGER,
            FOREIGN KEY(${ProductEntry.COLUMN_SHOP_PLAN_ID}) REFERENCES ${ShopPlanEntry.TABLE_NAME}(${BaseColumns._ID}) ON DELETE CASCADE
        )
    """
}