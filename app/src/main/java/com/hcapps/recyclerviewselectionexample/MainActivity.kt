package com.hcapps.recyclerviewselectionexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.hcapps.recyclerviewselectionexample.databinding.ActivityMainBinding
import com.hcapps.recyclerviewselectionexample.pojo.Product.Companion.parseProductJson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val list = parseProductJson(this)?.products
        Log.d("test_list_product", "onCreate: $list")

    }
}