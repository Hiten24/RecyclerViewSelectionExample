package com.hcapps.recyclerviewselectionexample.pojo

import android.content.Context
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.parcelize.Parcelize
import java.io.IOException

data class ProductData(
    val products: List<Product>
)

@Parcelize
data class Product(
    val id: Long,
    val title: String,
    val description: String,
    val price: Int,
    val rating: Float,
    val brand: String,
    val thumbnail: String,
    val images: List<String>
): Parcelable {

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