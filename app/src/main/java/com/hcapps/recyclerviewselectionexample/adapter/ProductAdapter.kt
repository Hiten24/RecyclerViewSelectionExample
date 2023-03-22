package com.hcapps.recyclerviewselectionexample.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hcapps.recyclerviewselectionexample.R
import com.hcapps.recyclerviewselectionexample.databinding.ItemBinding
import com.hcapps.recyclerviewselectionexample.pojo.Product

class ProductAdapter(
    private val onSelectOfProduct: (Product?) -> Unit
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    var tracker: SelectionTracker<Long>? = null

    inner class ProductViewHolder(private val binding: ItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindProduct(product: Product?, isSelected: Boolean) {
            with(binding) {

                title.text = product?.title
                description.text = product?.description
                brand.text = product?.brand

                Glide.with(thumbnail.context)
                    .load(product?.thumbnail)
                    .into(thumbnail)

                ivSelection.isVisible = isSelected
                val backgroundColor = if (isSelected) R.color.select_color else R.color.white
                card.setCardBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(card.context, backgroundColor)))

                card.setOnClickListener {
                    onSelectOfProduct.invoke(product)
                }

            }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
            return object: ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = bindingAdapterPosition

                override fun getSelectionKey(): Long {
                    val item = differ.currentList[bindingAdapterPosition]
                    return item.id
                }
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
        tracker?.let {
            val isSelected = it.isSelected(product.id)
            holder.bindProduct(product, isSelected)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}

class MediaKeyProvider(
    private val adapter: ProductAdapter
): ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long =
        adapter.differ.currentList[position].id

    override fun getPosition(key: Long): Int =
        adapter.differ.currentList.indexOfFirst { it.id == key }
}

class ProductDetailsLookUp(private val recyclerView: RecyclerView): ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long> {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        return (view?.let { recyclerView.getChildViewHolder(it) } as ProductAdapter.ProductViewHolder).getItemDetails()
    }
}