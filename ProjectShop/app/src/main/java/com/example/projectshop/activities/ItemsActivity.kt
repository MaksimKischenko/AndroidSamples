package com.example.projectshop.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectshop.R
import com.example.projectshop.adapters.ItemsAdapter
import com.example.projectshop.models.Item
import com.razorpay.Checkout

class ItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        Checkout.preload(applicationContext)
        val co = Checkout()
        co.setKeyID("rzp_live_XXXXXXXXXXXXXX")


        val itemsList : RecyclerView = findViewById(R.id.itemsList)
        val items = arrayListOf<Item>()
        items.add(Item(1, "sofa", "SOFA", "Simple sofa",99.4))
        items.add(Item(2, "car", "CAR", "Simple car", 23.3))
        items.add(Item(4, "tree", "TREE", "Simple tree", 34.2))

        itemsList.layoutManager = LinearLayoutManager(this)
        itemsList.adapter = ItemsAdapter(items, this)
    }
}