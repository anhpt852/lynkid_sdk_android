package vn.linkid.sdk.diamond.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.category.ui.CategoryFilterBottomSheet
import vn.linkid.sdk.databinding.FragmentDiamondCategoryBinding
import vn.linkid.sdk.diamond.adapter.DiamondCategoryAdapter
import vn.linkid.sdk.diamond.adapter.DiamondGiftsAdapter
import vn.linkid.sdk.diamond.repository.DiamondRepository
import vn.linkid.sdk.diamond.service.DiamondService
import vn.linkid.sdk.diamond.viewmodel.DiamondCategoryViewModel
import vn.linkid.sdk.diamond.viewmodel.DiamondCategoryViewModelFactory
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class DiamondCategoryFragment: Fragment() {

    private lateinit var binding: FragmentDiamondCategoryBinding
    private lateinit var viewModel: DiamondCategoryViewModel
    private val service: DiamondService = DiamondService(mainAPI)
    private val repository = DiamondRepository(service)

    private val args: DiamondCategoryFragmentArgs by navArgs()
    private val diamondCateCode: String by lazy { args.diamondCateCode }

    private val categoryAdapter = DiamondCategoryAdapter(emptyList())
    private val giftAdapter = DiamondGiftsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiamondCategoryBinding.inflate(inflater, container, false)
        val viewModelFactory = DiamondCategoryViewModelFactory(repository, diamondCateCode)
        viewModel = ViewModelProvider(this, viewModelFactory)[DiamondCategoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpLoader()
        setUpCategoryList()
        setUpGiftList()
        setUpFilter()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
        }
    }

    private fun setUpLoader() {
        viewModel.uiState.observe(viewLifecycleOwner) { (isLoading, isEmpty) ->
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isEmpty && !isLoading) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }



    private fun setUpCategoryList() {
        binding.apply {
            listCategory.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            listCategory.adapter = categoryAdapter
            categoryAdapter.onItemClick = { category ->
                Log.d("DiamondCategoryFragment", "Selected category: ${category.giftCategory?.code}")
                viewModel.setCateCode(category.giftCategory?.code ?: "")
                viewModel.giftFilter.postValue(GiftFilterModel())
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories.getOrNull() ?: emptyList())
            categoryAdapter.selectedPosition = (categories.getOrNull()
                ?: emptyList()).indexOfFirst { it.giftCategory?.code == viewModel.categoryCode.value }
            binding.listCategory.scrollToPosition(categoryAdapter.selectedPosition)
        }
    }

    private fun setUpGiftList() {
        binding.apply {
            listGift.layoutManager = LinearLayoutManager(binding.root.context)
            listGift.adapter = giftAdapter
            giftAdapter.onItemClick = { gift ->
                val action = DiamondCategoryFragmentDirections.actionDiamondCategoryFragmentToDiamondGiftDetailFragment(
                    gift.giftInfor?.id ?: 0
                )
                findNavController().navigate(action)
            }
            listGift.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var previousScrollPosition = 0

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > previousScrollPosition) {
                        viewModel.setShowFilter(false)
                    } else if (dy < previousScrollPosition) {
                        viewModel.setShowFilter(true)
                    }

                    previousScrollPosition = dy
                }
            })
            giftAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(giftAdapter.itemCount == 0)
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(giftAdapter.itemCount == 0)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(giftAdapter.itemCount == 0)
                }
            })
            layoutListGift.setOnRefreshListener {
                giftAdapter.refresh()
                layoutListGift.isRefreshing = false
            }
        }
        viewModel.giftsByCategory.observe(viewLifecycleOwner) { giftsByCategory ->
            Log.d("CategoryFragment", "getGiftsByCategory: $giftsByCategory")
            giftAdapter.submitData(lifecycle, giftsByCategory)
        }
    }

    private fun setUpFilter() {
        val layoutParams = binding.layoutFilter.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.bottomMargin = getNavigationBarHeight(binding.root) + (context?.dpToPx(8) ?: 0)
        binding.layoutFilter.layoutParams = layoutParams
        binding.layoutFilter.setOnClickListener {
            val bottomSheet =
                CategoryFilterBottomSheet(viewModel.giftFilter.value ?: GiftFilterModel())
            bottomSheet.onApplyFilter = { filter ->
                viewModel.giftFilter.postValue(filter)
            }
            bottomSheet.show(childFragmentManager, "CategoryFilterBottomSheet")
        }
        viewModel.isShowFilter.observe(viewLifecycleOwner) { isShowFilter ->
            binding.layoutFilter.visibility = if (isShowFilter) View.VISIBLE else View.GONE
        }
    }




}