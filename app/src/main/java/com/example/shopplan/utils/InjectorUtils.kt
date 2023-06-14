package com.example.shopplan.utils

import android.content.Context
import com.example.shopplan.model.dao.Database
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.viewmodel.shopplan.ShopPlanModelViewFactory

object InjectorUtils {

    fun provideShopPlanViewModelFactory(context: Context): ShopPlanModelViewFactory {
        val shopPlanRepository =
            ShopPlanRepository.getInstance(Database.getInstance(context).getShopPlanDao())
        return ShopPlanModelViewFactory(shopPlanRepository)
    }
}