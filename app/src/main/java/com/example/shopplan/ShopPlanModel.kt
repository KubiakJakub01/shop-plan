package com.example.shopplan

import android.os.Parcel
import android.os.Parcelable

data class ShopPlanModel(
    val title: String,
    val shopName: String,
    val totalCost: Double,
    val products: List<ProductModel>
)
