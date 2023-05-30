package com.example.shopplan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ShopPlanAdapter() :
    RecyclerView.Adapter<ShopPlanAdapter.ShopPlanViewHolder>() {

    private val shopPlanList = ArrayList<ShopPlanModel>()

    inner class ShopPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val shopNameTextView: TextView = itemView.findViewById(R.id.shopNameTextView)
        private val productCountTextView: TextView = itemView.findViewById(R.id.productCountTextView)
        private val totalCostTextView: TextView = itemView.findViewById(R.id.totalCostTextView)

        fun bind(shopPlan: ShopPlanModel) {
            titleTextView.text = shopPlan.title
            shopNameTextView.text = shopPlan.shopName
            productCountTextView.text = shopPlan.products.size.toString()
            totalCostTextView.text = shopPlan.totalCost.toString()

            // Add click listener or any other logic as needed
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPlanViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_plan, parent, false)
        return ShopPlanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShopPlanViewHolder, position: Int) {
        val product = shopPlanList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return shopPlanList.size
    }

    fun addItem(shopPlan: ShopPlanModel) {
        shopPlanList.add(shopPlan)
        notifyDataSetChanged()
    }
}
