package com.example.shopplan.view.shopplanform

interface QuantityChangeListener {
    fun onQuantityChanged(costChange: Double)
    fun setTotalCost(totalCost: Double)
}