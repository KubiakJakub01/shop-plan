package com.example.shopplan.api.currency

data class CurrencyResponse(
    val success: Boolean,
    val rates: Map<String, Double>,
    val error: ErrorResponse?
)

data class ErrorResponse(
    val code: Int,
    val type: String,
    val info: String
)
