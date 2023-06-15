package com.example.shopplan.api.currency

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.shopplan.utils.BuildConfigValues
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FixerApiEndPoint {
    private val TAG = "FixerApiEndPoint"
    private const val BASE_URL = "http://data.fixer.io/api/"
    private const val ACCESS_KEY = BuildConfigValues.FIXER_API_ACCESS_KEY

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val fixerApi = retrofit.create(FixerApi::class.java)

    suspend fun getExchangeRates(): Map<String, Double> {
        try {
            val response = fixerApi.getLatestRates(ACCESS_KEY)
            Log.i(TAG, "getExchangeRates: $response")
            if (response.success) {
                return response.rates
            } else {
                // Handle error case
                val error = response.error
                error?.let {
                    // Handle the error response
                    val errorCode = error.code
                    val errorType = error.type
                    val errorMessage = error.info
                }
            }
        } catch (e: Exception) {
            // Handle network or API error
            e.printStackTrace()
        }
        return emptyMap()
    }
}