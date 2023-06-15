package com.example.shopplan.api.currency

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("exchangeRate") val exchangeRate: Double
)
