package com.example.shopplan.model.table

data class CurrencyModel(
    val baseCurrency: String = "EUR",
    val currency: String,
    val symbol: String?
) {
}