package com.hcapps.recyclerviewselectionexample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcapps.recyclerviewselectionexample.databinding.ItemBinding
import com.hcapps.recyclerviewselectionexample.pojo.Product

class ProductAdapter: RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(private val binding: ItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindProduct(product: Product?) {
            with(binding) {

                title.text = product?.title
                description.text = product?.description
                brand.text = product?.brand

                Glide.with(thumbnail.context)
                    .load(product?.thumbnail)
                    .into(thumbnail)

            }

        }
    }

    private val differCallback = object: DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bindProduct(product)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}