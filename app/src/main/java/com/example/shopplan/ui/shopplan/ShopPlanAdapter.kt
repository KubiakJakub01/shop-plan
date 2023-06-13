package com.example.shopplan.ui.shopplan

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.R
import com.example.shopplan.ShopPlanFormActivity
import com.example.shopplan.model.menager.ShopPlanManager
import com.example.shopplan.model.table.ShopPlanModel

class ShopPlanAdapter() :
    RecyclerView.Adapter<ShopPlanAdapter.ShopPlanViewHolder>() {

    private val shopPlanList = ArrayList<ShopPlanModel>()
    companion object {
        private const val UPDATE_SHOP_PLAN_FORM_REQUEST_CODE = 2
    }

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
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPlanViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_shop_plan, parent, false)
        return ShopPlanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShopPlanViewHolder, position: Int) {
        val product = shopPlanList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ShopPlanFormActivity::class.java)
            intent.putExtra("shopPlan", product)
            (context as Activity).startActivityForResult(intent, UPDATE_SHOP_PLAN_FORM_REQUEST_CODE)
        }
    }

    override fun getItemCount(): Int {
        return shopPlanList.size
    }

    fun addItem(shopPlan: ShopPlanModel) {
        shopPlanList.add(shopPlan)
        notifyDataSetChanged()
    }

    fun updateItem(shopPlan: ShopPlanModel) {
        val index = shopPlanList.indexOfFirst { it.title == shopPlan.title }
        if (index != -1) {
            shopPlanList[index] = shopPlan
            notifyItemChanged(index)
        }
    }

    fun setShopPlans(shopPlans: List<ShopPlanModel>) {
        shopPlanList.clear()
        shopPlanList.addAll(shopPlans)
        notifyDataSetChanged()
    }
}
