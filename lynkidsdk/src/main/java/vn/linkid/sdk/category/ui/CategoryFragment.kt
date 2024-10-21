package vn.linkid.sdk.category.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vn.linkid.sdk.all_gift.ui.AllGiftFragmentDirections
import vn.linkid.sdk.category.adapter.CategoryAdapter
import vn.linkid.sdk.category.adapter.GiftsByCategoryAdapter
import vn.linkid.sdk.category.repository.CategoryRepository
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.category.viewmodel.CategoryViewModel
import vn.linkid.sdk.category.viewmodel.CategoryViewModelFactory
import vn.linkid.sdk.databinding.FragmentCategoryBinding
import vn.linkid.sdk.home.ui.HomeFragmentDirections
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.models.category.GiftFilterModel

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private val service = CategoryService(mainAPI)
    private val repository = CategoryRepository(service)
    private val viewModelFactory = CategoryViewModelFactory(repository)
    private lateinit var binding: FragmentCategoryBinding

    private val categoryAdapter = CategoryAdapter(emptyList())
    private val giftsByCategoryAdapter = GiftsByCategoryAdapter()

    private val args: CategoryFragmentArgs by navArgs()
    private val categoryCode: String by lazy { args.categoryCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoryViewModel::class.java]
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

            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnSearch.setOnClickListener {
                val action = CategoryFragmentDirections.actionCategoryFragmentToSearchFragment()
                findNavController().navigate(action)
            }
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
        viewModel.initCateCode(categoryCode)
        binding.apply {
            listCategory.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            listCategory.adapter = categoryAdapter
            categoryAdapter.onItemClick = { category ->
                if (category.categoryTypeCode == "Diamond") {
                    val action =
                        AllGiftFragmentDirections.actionAllGiftFragmentToDiamondCategoryFragment(category.code ?: "")
                    findNavController().navigate(action)
                } else {
                    Log.d("CategoryFragment", "Selected category: ${category.code}")
                    viewModel.setCateCode(category.code ?: "")
                    viewModel.giftFilter.postValue(GiftFilterModel())
                }
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories.getOrNull() ?: emptyList())
            categoryAdapter.selectedPosition = (categories.getOrNull()
                ?: emptyList()).indexOfFirst { it.code == viewModel.categoryCode.value }
            binding.listCategory.scrollToPosition(categoryAdapter.selectedPosition)
        }
    }

    private fun setUpGiftList() {
        binding.apply {
            listGift.layoutManager = LinearLayoutManager(binding.root.context)
            listGift.adapter = giftsByCategoryAdapter
            giftsByCategoryAdapter.onItemClick = { gift ->
                val action = CategoryFragmentDirections.actionCategoryFragmentToGiftDetailFragment(
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
            giftsByCategoryAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(giftsByCategoryAdapter.itemCount == 0)
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(giftsByCategoryAdapter.itemCount == 0)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(giftsByCategoryAdapter.itemCount == 0)
                }
            })
            layoutListGift.setOnRefreshListener {
                giftsByCategoryAdapter.refresh()
                layoutListGift.isRefreshing = false
            }
        }
        viewModel.giftsByCategory.observe(viewLifecycleOwner) { giftsByCategory ->
            Log.d("CategoryFragment", "getGiftsByCategory: $giftsByCategory")
            giftsByCategoryAdapter.submitData(lifecycle, giftsByCategory)
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