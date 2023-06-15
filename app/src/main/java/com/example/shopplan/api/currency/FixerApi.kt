package com.example.shopplan.api.currency

import retrofit2.http.GET
import retrofit2.http.Query

interface FixerApi {
    @GET("latest")
    suspend fun getLatestRates(
        @Query("access_key") accessKey: String
    ): CurrencyResponse
}