package com.hcapps.recyclerviewselectionexample.pojo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class ProductData(
    val products: List<Product>
)

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Int,
    val rating: Float,
    val brand: String,
    val thumbnail: String,
    val images: List<String>
) {

    companion object {
        fun parseProductJson(context: Context): ProductData? {
            var json = ""
            try {
                json = context.assets.open("ProductData.json")
                    .bufferedReader()
                    .use { it.readText() }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            val listOfProductType = object : TypeToken<ProductData>() {}.type
            return Gson().fromJson(json, listOfProductType)
        }
    }
    
}