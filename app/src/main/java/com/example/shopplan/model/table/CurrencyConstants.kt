package com.example.shopplan.model.table

class CurrencyConstants {
    companion object {
        const val EUR = "EUR"
        const val USD = "USD"
        const val GBP = "GBP"
        const val JPY = "JPY"
        const val PLN = "PLN"
        const val DEFAULT_CURRENCY = EUR

        val currencyList = listOf(EUR, USD, GBP, JPY, PLN)

        val currencySymbols = mapOf(
            EUR to "€",
            USD to "$",
            GBP to "£",
            JPY to "¥",
            PLN to "zł"
        )
    }
}