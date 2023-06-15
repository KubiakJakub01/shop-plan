package com.example.shopplan.model.table

data class CurrencyModel(
    val baseCurrency: String = CurrencyConstants.DEFAULT_CURRENCY,
    val currency: String,
    val symbol: String?
) {
}