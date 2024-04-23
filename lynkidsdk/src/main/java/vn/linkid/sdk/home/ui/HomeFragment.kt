package vn.linkid.sdk.home.ui

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
import androidx.recyclerview.widget.PagerSnapHelper
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.all_gift.ui.AllGiftFragmentDirections
import vn.linkid.sdk.databinding.FragmentHomeBinding
import vn.linkid.sdk.dpToPx
import vn.linkid.sdk.formatPrice
import vn.linkid.sdk.getStatusBarHeight
import vn.linkid.sdk.handleScroll
import vn.linkid.sdk.home.adapter.HomeBannerAdapter
import vn.linkid.sdk.home.adapter.HomeCategoryAdapter
import vn.linkid.sdk.home.adapter.HomeGiftAdapter
import vn.linkid.sdk.home.repository.HomeRepository
import vn.linkid.sdk.home.service.HomeService
import vn.linkid.sdk.home.viewmodel.HomeViewModel
import vn.linkid.sdk.home.viewmodel.HomeViewModelFactory
import vn.linkid.sdk.mainAPI

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private val service = HomeService(mainAPI)
    private val repository = HomeRepository(service)
    private val viewModelFactory = HomeViewModelFactory(repository)
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpMemberInfo()
        setUpPointInfo()
        setUpCategories()
        setUpBannersAndNews()
        setUpGift()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = imgAvatar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            imgAvatar.layoutParams = layoutParams
        }
    }

    private fun setUpMemberInfo() {
        viewModel.memberInfoLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpMemberInfo: $loader")
            }
        }
        viewModel.memberInfo.observe(viewLifecycleOwner) { memberInfo ->
            if (memberInfo.getOrNull() != null) {
                val member = memberInfo.getOrNull()!!
                binding.apply {
                    Glide.with(imgAvatar)
                        .load(member.avatar)
                        .circleCrop()
                        .into(imgAvatar)
                    txtMemberName.text = member.name
                }
            }
        }
    }

    private fun setUpPointInfo() {
        viewModel.pointInfoLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpPointInfo: $loader")
            }
        }
        viewModel.pointInfo.observe(viewLifecycleOwner) { pointInfo ->
            if (pointInfo.getOrNull() != null) {
                val point = pointInfo.getOrNull()!!
                binding.apply {
                    txtBalance.text = point.tokenBalance!!.formatPrice()
                }
            }
        }
    }

    private fun setUpCategories() {
        viewModel.categoriesLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpCategories: $loader")
            }
        }
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            if (categories.getOrNull() != null) {
                binding.apply {
                    listCategory.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    val categoryAdapter = HomeCategoryAdapter(categories.getOrNull()!!)
                    listCategory.adapter = categoryAdapter
                    listCategory.handleScroll(binding.indicatorCategory)
                    categoryAdapter.onItemClick = { category ->
                        val action =
                            HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                                category.code ?: ""
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
        binding.btnCategorySeeMore.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToAllGiftFragment()
            findNavController().navigate(action)
        }
    }

    private fun setUpBannersAndNews() {
        viewModel.bannersAndNewsLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpBannersAndNews: $loader")
            }
        }
        viewModel.bannersAndNews.observe(viewLifecycleOwner) { bannersAndNews ->
            if (bannersAndNews.getOrNull() != null) {
                binding.apply {
                    listBanner.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    listBanner.adapter = HomeBannerAdapter(
                        bannersAndNews.getOrNull()!!.data?.get(0)?.resultDto?.items ?: listOf()
                    )
                    val snapHelper = PagerSnapHelper()
                    snapHelper.attachToRecyclerView(listBanner)
                }
            }
        }
    }

    private fun setUpGift() {
        viewModel.homeGiftGroupLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpGift: $loader")
            }
        }
        viewModel.homeGiftGroup.observe(viewLifecycleOwner) { homeGiftGroup ->
            if (homeGiftGroup.getOrNull() != null) {
                binding.apply {
                    listGift.layoutManager = GridLayoutManager(requireContext(), 2)
                    listGift.adapter =
                        HomeGiftAdapter(homeGiftGroup.getOrNull()!!.data?.gifts ?: listOf())
                }
            }
        }

    }
}