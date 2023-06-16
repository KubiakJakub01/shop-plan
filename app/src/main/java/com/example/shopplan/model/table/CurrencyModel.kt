package com.example.shopplan.model.table

import com.example.shopplan.utils.CurrencyConstants

data class CurrencyModel(
    val baseCurrency: String = CurrencyConstants.DEFAULT_CURRENCY,
    val currency: String,
    val symbol: String?
)