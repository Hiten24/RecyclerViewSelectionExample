package com.hcapps.recyclerviewselectionexample

import android.annotation.SuppressLint
import android.app.Notification.Action
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.hcapps.recyclerviewselectionexample.adapter.MediaKeyProvider
import com.hcapps.recyclerviewselectionexample.adapter.ProductAdapter
import com.hcapps.recyclerviewselectionexample.adapter.ProductDetailsLookUp
import com.hcapps.recyclerviewselectionexample.databinding.FragmentHomeBinding
import com.hcapps.recyclerviewselectionexample.pojo.Product
import com.hcapps.recyclerviewselectionexample.pojo.Product.Companion.parseProductJson

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productAdapter: ProductAdapter
    private var selectionActionBar: ActionMode? = null

    val selectedProduct = mutableListOf<Long>()

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
        selectionObserver()
    }

    private fun setupProductAdapter(): ProductAdapter {
        productAdapter = ProductAdapter()
        binding.rvHome.adapter = productAdapter
        productAdapter.tracker = setTracker()
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

    private fun selectionObserver() {
        val selectionObserver = object: SelectionTracker.SelectionObserver<Long>() {

            override fun onItemStateChanged(key: Long, selected: Boolean) {
                super.onItemStateChanged(key, selected)
                if (selected) {
                    if (!selectedProduct.contains(key)) {
                        selectedProduct.add(key)
                    }
                } else {
                    selectedProduct.remove(key)
                }
                Log.d("HomeFragment", "onItemStateChanged: $selectedProduct")

                if (selectedProduct.isNotEmpty()) {
                    if (selectionActionBar == null) {
                        selectionActionBar = requireActivity().startActionMode(actionBarCallback)
                    }
                    selectionActionBar?.title = "${selectedProduct.size} selected"
                } else {
                    selectionActionBar?.finish()
                    selectionActionBar = null
                }

            }

            @SuppressLint("RestrictedApi")
            override fun onSelectionCleared() {
                super.onSelectionCleared()
                selectedProduct.removeAll(selectedProduct)
                selectionActionBar?.finish()
                selectionActionBar = null
            }

        }
        productAdapter.tracker?.addObserver(selectionObserver)
    }

    val actionBarCallback = object: ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            requireActivity().menuInflater.inflate(R.menu.selection_contextual_action_bar, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.delete -> {
                    if (selectedProduct.isNotEmpty()) {
                        Toast.makeText(
                            requireContext(),
                            "Deleting selected file...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    productAdapter.tracker?.clearSelection()
                    true
                }
                R.id.select_all -> {
                    productAdapter.tracker?.setItemsSelected(
                        productAdapter.differ.currentList.map { it.id },
                        true
                    )
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            productAdapter.tracker?.clearSelection()
        }
    }
}