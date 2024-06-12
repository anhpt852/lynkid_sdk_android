package vn.linkid.sdk.category.ui

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
import vn.linkid.sdk.category.adapter.CategoryAdapter
import vn.linkid.sdk.category.adapter.GiftsByCategoryAdapter
import vn.linkid.sdk.category.repository.CategoryRepository
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.category.viewmodel.CategoryViewModel
import vn.linkid.sdk.category.viewmodel.CategoryViewModelFactory
import vn.linkid.sdk.databinding.FragmentCategoryBinding
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

    private val categoryAdapter = CategoryAdapter(emptyList(), "")
    private val giftsByCategoryAdapter = GiftsByCategoryAdapter(emptyList())

    private val args: CategoryFragmentArgs by navArgs()
    private val categoryCode: String by lazy { args.categoryCode }

    private var currentPage = 0

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
        }
    }

    private fun setUpLoader() {
        viewModel.loader.observe(viewLifecycleOwner) {
            binding.loading.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun setUpCategoryList() {
        binding.apply {
            listCategory.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            listCategory.adapter = categoryAdapter
            categoryAdapter.onItemClick = { category ->
                Log.d("CategoryFragment", "Selected category: ${category.code}")
                viewModel.categoryCode.postValue(category.code)
                viewModel.giftFilter.postValue(GiftFilterModel())
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories.getOrNull() ?: emptyList())
        }
        viewModel.categoryCode.postValue(categoryCode)
        viewModel.categoryCode.observe(viewLifecycleOwner) { categoryCode ->
            Log.d("CategoryFragment", "Selected category load: $categoryCode")
            if (categoryCode.isNotEmpty()) {
                viewModel.getGiftsByCategory(currentPage)
                    .observe(viewLifecycleOwner) { giftsByCategory ->
                        Log.d("CategoryFragment", "Gifts by category: $giftsByCategory")
                    }
                categoryAdapter.updateSelectedCategoryCode(categoryCode ?: "")
            }
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

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    val endHasBeenReached = lastVisibleItem + 1 >= totalItemCount
                    if (totalItemCount > 0 && endHasBeenReached && viewModel.loader.value == false && ((viewModel.giftsByCategory.value?.size
                            ?: 0) < (viewModel.totalGiftCount.value ?: 0))
                    ) {
                        currentPage++
                        viewModel.getGiftsByCategory(currentPage)
                    }
                }
            })
        }
        viewModel.giftsByCategory.observe(viewLifecycleOwner) { giftsByCategory ->
            Log.d("CategoryFragment", "giftsByCategory: $giftsByCategory")
            giftsByCategoryAdapter.updateGifts(giftsByCategory)
        }
//        viewModel.getGiftsByCategory(0).observe(viewLifecycleOwner) { giftsByCategory ->
//            Log.d("CategoryFragment", "getGiftsByCategory: $giftsByCategory")
//        }
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