package com.example.shopplan.model.contract

object ShopPlanContract {
    // Define table names
    object ShopPlanEntry {
        const val TABLE_NAME = "shop_plans"
        const val COLUMN_SHOP_PLAN_ID = "shop_plan_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_SHOP_NAME = "shop_name"
        const val COLUMN_TOTAL_COST = "total_cost"
    }

    object ProductEntry {
        const val TABLE_NAME = "products"
        const val COLUMN_PRODUCT_ID = "product_id"
        const val COLUMN_SHOP_PLAN_ID = "shop_plan_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_QUANTITY = "quantity"
    }

    object CurrencyEntry {
        const val TABLE_NAME = "currencies"
        const val COLUMN_CURRENCY = "currency"
        const val COLUMN_BASE_CURRENCY = "base_currency"
        const val COLUMN_SYMBOL = "symbol"
    }

    val SQL_DELETE_PRODUCT_TABLE: String
        get() = "DROP TABLE IF EXISTS ${ProductEntry.TABLE_NAME}"
    val SQL_DELETE_SHOP_PLAN_TABLE: String
        get() = "DROP TABLE IF EXISTS ${ShopPlanEntry.TABLE_NAME}"


    // Define SQL queries
    const val SQL_CREATE_SHOP_PLAN_TABLE = """
        CREATE TABLE ${ShopPlanEntry.TABLE_NAME} (
            ${ShopPlanEntry.COLUMN_SHOP_PLAN_ID} TEXT PRIMARY KEY,
            ${ShopPlanEntry.COLUMN_TITLE} TEXT,
            ${ShopPlanEntry.COLUMN_SHOP_NAME} TEXT,
            ${ShopPlanEntry.COLUMN_TOTAL_COST} REAL
        )
    """

    const val SQL_CREATE_PRODUCT_TABLE = """
        CREATE TABLE ${ProductEntry.TABLE_NAME} (
            ${ProductEntry.COLUMN_PRODUCT_ID} TEXT PRIMARY KEY,
            ${ProductEntry.COLUMN_SHOP_PLAN_ID} TEXT,
            ${ProductEntry.COLUMN_NAME} TEXT,
            ${ProductEntry.COLUMN_PRICE} REAL,
            ${ProductEntry.COLUMN_QUANTITY} INTEGER,
            FOREIGN KEY(${ProductEntry.COLUMN_SHOP_PLAN_ID}) REFERENCES ${ShopPlanEntry.TABLE_NAME}(${ShopPlanEntry.COLUMN_SHOP_PLAN_ID}) ON DELETE CASCADE
        )
    """

    const val SQL_CREATE_CURRENCY_TABLE = """
        CREATE TABLE ${CurrencyEntry.TABLE_NAME} (
            ${CurrencyEntry.COLUMN_CURRENCY} TEXT PRIMARY KEY,
            ${CurrencyEntry.COLUMN_BASE_CURRENCY} TEXT,
            ${CurrencyEntry.COLUMN_SYMBOL} TEXT
        )
    """
}