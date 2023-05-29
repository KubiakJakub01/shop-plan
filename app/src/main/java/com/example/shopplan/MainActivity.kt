package com.example.shopplan

import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.RecyclerView
import com.example.shopplan.ui.theme.ShopPlanTheme

class MainActivity : ComponentActivity() {

    private lateinit var rvShopPlans: RecyclerView
//    private lateinit var shopPlanAdapter: ShopPlanAdapter
    private lateinit var btnCreateShopPlan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvShopPlans = findViewById(R.id.rvShopPlans)
        btnCreateShopPlan = findViewById(R.id.btnCreateShopPlan)

        rvShopPlans.adapter = ShopPlanAdapter(this)
        rvShopPlans.setHasFixedSize(true)
    }

    fun openShopPlanForm(view: View) {
        val intent = Intent(this, ShopPlanFormActivity::class.java)
        startActivity(intent)
    }


}
