package com.hcapps.recyclerviewselectionexample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.hcapps.recyclerviewselectionexample.databinding.FragmentChildBinding
import com.hcapps.recyclerviewselectionexample.pojo.Product

class ChildFragment: Fragment() {

    private lateinit var binding: FragmentChildBinding
    private val args by navArgs<ChildFragmentArgs>()
    private var product: Product? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildBinding.inflate(inflater, container, false)
        product = args.product
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (product == null) {
            Toast.makeText(requireContext(), "Something wrong with this product, Try Again", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        with(binding) {

            Glide.with(this@ChildFragment)
                .load(product?.images?.lastOrNull())
                .into(ivProductImage)

            starOne.isSelected = true

            tvProductTitle.text = product?.title
            tvProductDescription.text = product?.description
            product?.rating?.toInt()?.let { fillStar(it) }
            tvStarReview.text = product?.rating.toString()
        }

    }

    private fun fillStar(star: Int) {
        val starIds = listOf(binding.starOne, binding.starTwo, binding.starThree, binding.starFour, binding.starFive)
        for (i in 0 until star) {
            starIds[i].isSelected = true
        }
    }

}