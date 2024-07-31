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
import vn.linkid.sdk.InstallAppDialog
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentHomeBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.handleScroll
import vn.linkid.sdk.home.adapter.HomeBannerAdapter
import vn.linkid.sdk.home.adapter.HomeCategoryAdapter
import vn.linkid.sdk.home.adapter.HomeGiftAdapter
import vn.linkid.sdk.home.repository.HomeRepository
import vn.linkid.sdk.home.service.HomeService
import vn.linkid.sdk.home.viewmodel.HomeViewModel
import vn.linkid.sdk.home.viewmodel.HomeViewModelFactory
import vn.linkid.sdk.utils.mainAPI

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private val service = HomeService(mainAPI)
    private val repository = HomeRepository(service)
    private val viewModelFactory = HomeViewModelFactory(repository)
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        setUpFlashSale()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = imgAvatar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            imgAvatar.layoutParams = layoutParams

            if (LynkiD_SDK.isAnonymous) {
                cardAnonymous.visibility = View.VISIBLE
                cardBalance.visibility = View.GONE
            } else {
                cardAnonymous.visibility = View.GONE
                cardBalance.visibility = View.VISIBLE
            }
            btnLogin.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToAuthFragment(LynkiD_SDK.connectedMember!!)
                findNavController().navigate(action)
            }

            btnExitDark.setOnClickListener { (activity as LynkiDSDKActivity).exitSDK() }

            btnFindDark.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
                findNavController().navigate(action)
            }

            btnDataTopUp.setOnClickListener {
                val dialog = InstallAppDialog()
                dialog.show(childFragmentManager, "InstallAppDialog")
            }

            btnPhoneTopUp.setOnClickListener {
                val dialog = InstallAppDialog()
                dialog.show(childFragmentManager, "InstallAppDialog")
            }

            btnInstallAppBig.setOnClickListener {
                val dialog = InstallAppDialog()
                dialog.show(childFragmentManager, "InstallAppDialog")
            }

            btnScrollToTop.setOnClickListener {
                scrollHome.smoothScrollTo(0, 0)
            }
        }
    }

    private fun setUpMemberInfo() {
        binding.apply {
            Glide.with(imgAvatar).load(R.drawable.ic_avatar_placeholder).circleCrop()
                .into(imgAvatar)
            txtMemberName.text = LynkiD_SDK.name
        }
        viewModel.memberInfoLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpMemberInfo: $loader")
            }
        }
        viewModel.memberInfo.observe(viewLifecycleOwner) { memberInfo ->
            if (memberInfo.getOrNull() != null) {
                val member = memberInfo.getOrNull()!!
                binding.apply {
                    Glide.with(imgAvatar).load(member.avatar)
                        .placeholder(R.drawable.ic_avatar_placeholder).circleCrop().into(imgAvatar)
                    txtMemberName.text =
                        if (!member.name.isNullOrEmpty()) member.name else LynkiD_SDK.name
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
                            if (category.categoryTypeCode == "Diamond") HomeFragmentDirections.actionHomeFragmentToDiamondCategoryFragment(
                                category.code ?: ""
                            ) else HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
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
                    if (!homeGiftGroup.getOrNull()!!.data?.giftGroup?.name.isNullOrEmpty()) {
                        txtGiftTitle.text = homeGiftGroup.getOrNull()!!.data?.giftGroup?.name
                    }
                    listGift.layoutManager = GridLayoutManager(requireContext(), 2)
                    val adapter =
                        HomeGiftAdapter(homeGiftGroup.getOrNull()!!.data?.gifts ?: listOf())
                    adapter.onItemClickListener = { gift ->
                        val action = HomeFragmentDirections.actionHomeFragmentToGiftDetailFragment(
                            gift.giftInfo?.id ?: 0
                        )
                        findNavController().navigate(action)
                    }
                    listGift.adapter = adapter
                    btnGiftSeeMore.setOnClickListener {
                        val action = HomeFragmentDirections.actionHomeFragmentToGiftGroupFragment(
                            homeGiftGroup.getOrNull()!!.data?.giftGroup?.code ?: "",
                            homeGiftGroup.getOrNull()!!.data?.giftGroup?.name ?: ""
                        )
                        findNavController().navigate(action)
                    }
                }
            }
        }

    }

    private fun setUpFlashSale() {
        viewModel.homeFlashSaleLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpFlashSale: $loader")
            }
        }
        viewModel.homeFlashSale.observe(viewLifecycleOwner) { homeFlashSale ->
            if (homeFlashSale.getOrNull() != null) {
                binding.apply {
//                    listGift.layoutManager = GridLayoutManager(requireContext(), 2)
//                    val adapter =
//                        HomeGiftAdapter(homeFlashSale.getOrNull()!!.data?.gifts ?: listOf())
//                    adapter.onItemClickListener = { gift ->
//                        val action = HomeFragmentDirections.actionHomeFragmentToGiftDetailFragment(
//                            gift.giftInfo?.id ?: 0
//                        )
//                        findNavController().navigate(action)
//                    }
//                    listGift.adapter = adapter

                }
            }
        }

    }
}