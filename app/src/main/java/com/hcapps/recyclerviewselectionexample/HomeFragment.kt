package com.hcapps.recyclerviewselectionexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.hcapps.recyclerviewselectionexample.adapter.MediaKeyProvider
import com.hcapps.recyclerviewselectionexample.adapter.ProductAdapter
import com.hcapps.recyclerviewselectionexample.adapter.ProductDetailsLookUp
import com.hcapps.recyclerviewselectionexample.databinding.FragmentHomeBinding
import com.hcapps.recyclerviewselectionexample.pojo.Product.Companion.parseProductJson

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = setupProductAdapter()
        adapter.differ.submitList(parseProductJson(requireContext())?.products)
    }

    private fun setupProductAdapter(): ProductAdapter {
        productAdapter = ProductAdapter()
        productAdapter.tracker = setTracker()
        binding.rvHome.adapter = productAdapter
        return productAdapter
    }

    private fun setTracker(): SelectionTracker<Long> {
        return SelectionTracker.Builder(
            "product_selector",
            binding.rvHome,
            MediaKeyProvider(productAdapter),
            ProductDetailsLookUp(binding.rvHome),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
    }

}