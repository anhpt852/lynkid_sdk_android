package com.linkid.sdk.all_gift.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.linkid.sdk.all_gift.adapter.AllGiftCategoryAdapter
import com.linkid.sdk.all_gift.repository.AllGiftRepository
import com.linkid.sdk.all_gift.service.AllGiftService
import com.linkid.sdk.all_gift.viewmodel.AllGiftViewModel
import com.linkid.sdk.all_gift.viewmodel.AllGiftViewModelFactory
import com.linkid.sdk.databinding.FragmentAllGiftBinding
import com.linkid.sdk.dpToPx
import com.linkid.sdk.getStatusBarHeight
import com.linkid.sdk.mainAPI

class AllGiftFragment : Fragment() {

    private lateinit var viewModel: AllGiftViewModel
    private val service = AllGiftService(mainAPI)
    private val repository = AllGiftRepository(service)
    private val viewModelFactory = AllGiftViewModelFactory(repository)
    private lateinit var binding: FragmentAllGiftBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllGiftBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[AllGiftViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpCategories()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = btnBack.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            btnBack.layoutParams = layoutParams
        }
    }

    private fun setUpCategories() {
        binding.apply {
            viewModel.categories.observe(viewLifecycleOwner) { categories ->
                if (categories.getOrNull() != null) {
                    listCategory.layoutManager =
                        GridLayoutManager(
                            requireContext(),
                            2,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    listCategory.adapter = AllGiftCategoryAdapter(categories.getOrNull()!!)
                }
            }
        }
    }


}