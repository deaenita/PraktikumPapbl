package com.deaenita.praktikumpapbl

import android.health.connect.datatypes.units.Length
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deaenita.praktikumpapbl.adapter.ProductAdapter
import com.deaenita.praktikumpapbl.helper.Product
import com.deaenita.praktikumpapbl.helper.ProductApi
import com.deaenita.praktikumpapbl.helper.RetrofitHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val productApi = RetrofitHelper.getInstance().create(ProductApi::class.java)

        // Ambil data produk menggunakan Coroutine
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = productApi.getProducts()
                if (response.isSuccessful) {
                    val productList = response.body()?.products
                    productList?.let {
                        // Log untuk mengecek data masuk
                        Log.d("MainActivitya", "Data berhasil dimuat: $productList")

                        // Update UI di Main Thread
                        CoroutineScope(Dispatchers.Main).launch {
                            productAdapter = ProductAdapter(it)
                            recyclerView.adapter = productAdapter
                        }
                    }
                } else {
                    Log.e("MainActivity", "Failed to retrieve data: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.message}")
            }
        }
    }
}