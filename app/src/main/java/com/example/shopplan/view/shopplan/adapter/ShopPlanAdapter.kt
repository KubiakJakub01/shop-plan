package com.example.shopplan.view.shopplan

import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.R
import com.example.shopplan.model.table.ShopPlanModel
import com.example.shopplan.view.shopplanform.ShopPlanFormActivity

class ShopPlanAdapter :
    RecyclerView.Adapter<ShopPlanAdapter.ShopPlanViewHolder>() {

    private val shopPlanList = ArrayList<ShopPlanModel>()

    companion object {
        private const val UPDATE_SHOP_PLAN_FORM_REQUEST_CODE = 2
    }

    inner class ShopPlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val shopNameTextView: TextView = itemView.findViewById(R.id.shopNameTextView)
        private val productCountTextView: TextView =
            itemView.findViewById(R.id.productCountTextView)
        private val totalCostTextView: TextView = itemView.findViewById(R.id.totalCostTextView)
        private val menuButton: ImageButton = itemView.findViewById(R.id.menu_button)

        fun bind(shopPlan: ShopPlanModel) {
            titleTextView.text = shopPlan.title
            shopNameTextView.text = shopPlan.shopName
            productCountTextView.text = shopPlan.products.size.toString()
            totalCostTextView.text = shopPlan.totalCost.toString()


        }
    }

    private fun popupMenu(view: View, holder: ShopPlanViewHolder, shopPlan: ShopPlanModel) {
        val popupMenu = PopupMenu(view.context,view)
        popupMenu.inflate(R.menu.item_shop_plan_popup)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.edit_category -> {
                    openShopPlanFormActivity(holder, shopPlan)
                    true
                }
                R.id.delete_category -> {
                    categoryActionListener.onCategoryDelete(category)
                    true
                }
                else -> false
            }
        }
        popupMenu.setForceShowIcon(true)
        popupMenu.gravity = Gravity.END;
        popupMenu.show()
    }

    private fun openShopPlanFormActivity(holder: ShopPlanViewHolder, shopPlan: ShopPlanModel) {
        val context = holder.itemView.context
        val intent = Intent(context, ShopPlanFormActivity::class.java)
        intent.putExtra("shopPlan", shopPlan)
        (context as Activity).startActivityForResult(intent, UPDATE_SHOP_PLAN_FORM_REQUEST_CODE)
    }

    private fun deleteShopPlan(shopPlan: ShopPlanModel) {
        val index = shopPlanList.indexOfFirst { it.title == shopPlan.title }
        if (index != -1) {
            shopPlanList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopPlanViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_shop_plan, parent, false)
        return ShopPlanViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShopPlanViewHolder, position: Int) {
        val shopPlan = shopPlanList[position]
        holder.bind(shopPlan)

        holder.itemView.setOnClickListener {
            openShopPlanFormActivity(holder, shopPlan)
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
