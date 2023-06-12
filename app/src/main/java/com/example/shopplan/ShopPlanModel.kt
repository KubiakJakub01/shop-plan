package com.example.shopplan

import android.os.Parcel
import android.os.Parcelable

data class ShopPlanModel(
    val title: String,
    val shopName: String,
    val totalCost: Double,
    val products: List<ProductModel>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.createTypedArrayList(ProductModel.CREATOR) as List<ProductModel>
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(shopName)
        parcel.writeDouble(totalCost)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopPlanModel> {
        override fun createFromParcel(parcel: Parcel): ShopPlanModel {
            return ShopPlanModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopPlanModel?> {
            return arrayOfNulls(size)
        }
    }
}
