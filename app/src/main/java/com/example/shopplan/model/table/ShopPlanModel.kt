package com.example.shopplan.model.table

import android.os.Parcel
import android.os.Parcelable
import java.util.Random

data class ShopPlanModel(
    val shopPlanID: Int = getAutoId(),
    val title: String,
    val shopName: String,
    var totalCost: Double,
    val products: List<ProductModel>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readDouble(),
        parcel.createTypedArrayList(ProductModel) as List<ProductModel>
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(shopPlanID)
        parcel.writeString(title)
        parcel.writeString(shopName)
        parcel.writeDouble(totalCost)
        parcel.writeTypedList(products)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShopPlanModel> {
        fun getAutoId(): Int {
            val random = Random()
            return random.nextInt(10000)
        }

        override fun createFromParcel(parcel: Parcel): ShopPlanModel {
            return ShopPlanModel(parcel)
        }

        override fun newArray(size: Int): Array<ShopPlanModel?> {
            return arrayOfNulls(size)
        }

    }
}
