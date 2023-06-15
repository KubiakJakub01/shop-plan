package com.example.shopplan.utils

import android.content.Context
import com.example.shopplan.model.dao.Database
import com.example.shopplan.model.repository.CurrencyRepository
import com.example.shopplan.model.repository.ProductRepository
import com.example.shopplan.model.repository.ShopPlanRepository
import com.example.shopplan.viewmodel.shopplan.ShopPlanModelViewFactory
import com.example.shopplan.viewmodel.shopplanform.ProductModelViewFactory

object InjectorUtils {

    fun provideShopPlanViewModelFactory(context: Context): ShopPlanModelViewFactory {
        val shopPlanRepository =
            ShopPlanRepository.getInstance(Database.getInstance(context).getShopPlanDao())
        val currencyRepository =
            CurrencyRepository.getInstance(Database.getInstance(context).getCurrencyDao())
        return ShopPlanModelViewFactory(shopPlanRepository, currencyRepository)
    }

    fun provideProductViewModelFactory(context: Context): ProductModelViewFactory {
        val productRepository =
            ProductRepository.getInstance(Database.getInstance(context).getProductDao())
        return ProductModelViewFactory(productRepository)
    }
}