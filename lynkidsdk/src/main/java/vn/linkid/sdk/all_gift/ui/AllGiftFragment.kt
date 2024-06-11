package vn.linkid.sdk.all_gift.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import vn.linkid.sdk.all_gift.adapter.AllGiftCategoryAdapter
import vn.linkid.sdk.all_gift.adapter.AllGiftGroupAdapter
import vn.linkid.sdk.all_gift.repository.AllGiftRepository
import vn.linkid.sdk.all_gift.service.AllGiftService
import vn.linkid.sdk.all_gift.viewmodel.AllGiftViewModel
import vn.linkid.sdk.all_gift.viewmodel.AllGiftViewModelFactory
import vn.linkid.sdk.databinding.FragmentAllGiftBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.handleScroll
import vn.linkid.sdk.utils.mainAPI

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
        setUpGift()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams

            val cardLayoutParams = cardCategory.layoutParams as ViewGroup.MarginLayoutParams
            cardLayoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(128) ?: 0)
            cardCategory.layoutParams = cardLayoutParams
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
                    val categoryAdapter = AllGiftCategoryAdapter(categories.getOrNull()!!)
                    listCategory.adapter = categoryAdapter
                    categoryAdapter.onItemClick = { category ->
                        val action =
                            AllGiftFragmentDirections.actionAllGiftFragmentToCategoryFragment(
                                category.code ?: ""
                            )
                        findNavController().navigate(action)
                    }
                    listCategory.handleScroll(binding.indicatorCategory)
                }
            }
        }
    }


    private fun setUpGift() {
        viewModel.giftGroupLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("AllGiftFragment", "setUpGift: $loader")
            }
        }
        viewModel.giftGroups.observe(viewLifecycleOwner) { giftGroups ->
            if (giftGroups.getOrNull() != null) {
                binding.apply {
                    listGift.layoutManager = LinearLayoutManager(requireContext())
                    listGift.adapter =
                        AllGiftGroupAdapter(giftGroups.getOrNull()!!)
                }
            }
        }
    }


}