package vn.linkid.sdk.category.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vn.linkid.sdk.category.adapter.CategoryAdapter
import vn.linkid.sdk.category.adapter.GiftsByCategoryAdapter
import vn.linkid.sdk.category.repository.CategoryRepository
import vn.linkid.sdk.category.service.CategoryService
import vn.linkid.sdk.category.viewmodel.CategoryViewModel
import vn.linkid.sdk.category.viewmodel.CategoryViewModelFactory
import vn.linkid.sdk.databinding.FragmentCategoryBinding
import vn.linkid.sdk.mainAPI

class CategoryFragment : Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private val service = CategoryService(mainAPI)
    private val repository = CategoryRepository(service)
    private val viewModelFactory = CategoryViewModelFactory(repository)
    private lateinit var binding: FragmentCategoryBinding

    private val categoryAdapter = CategoryAdapter(emptyList(), "")
    private val giftsByCategoryAdapter = GiftsByCategoryAdapter(emptyList())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader()
        setUpCategoryList()
        setUpGiftList()
    }

    private fun setUpLoader() {
        viewModel.loader.observe(viewLifecycleOwner, {

        })
    }

    private fun setUpCategoryList() {
        binding.apply {
            listCategory.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            listCategory.adapter = categoryAdapter
            categoryAdapter.onItemClick = { category ->
                viewModel.categoryCode.postValue(category.code)
                viewModel.getGiftsByCategory(0)
                categoryAdapter.updateSelectedCategoryCode(category.code ?: "")
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.updateCategories(categories.getOrNull() ?: emptyList())
        }
    }

    private fun setUpGiftList() {
        binding.apply {
            listGift.layoutManager = LinearLayoutManager(binding.root.context)
            listGift.adapter = giftsByCategoryAdapter
            giftsByCategoryAdapter.onItemClick = { gift ->

            }
        }
        viewModel.giftsByCategory.observe(viewLifecycleOwner) { giftsByCategory ->
            giftsByCategoryAdapter.updateGifts(giftsByCategory)
        }
        viewModel.getGiftsByCategory(0)
    }

}