package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.databinding.FragmentImeadiaTabBinding
import vn.linkid.sdk.imedia.adapter.IMediaBrandAdapter
import vn.linkid.sdk.imedia.adapter.IMediaGroupAdapter
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.imedia.service.IMediaService
import vn.linkid.sdk.imedia.viewmodel.IMediaTabViewModel
import vn.linkid.sdk.imedia.viewmodel.IMediaTabViewModelFactory
import vn.linkid.sdk.models.imedia.TopupRedeemInfo
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.mainAPI

class IMediaTabFragment : Fragment() {
    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): IMediaTabFragment {
            val fragment = IMediaTabFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    private var tab: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = it.getInt(ARG_POSITION)
        }
    }

    private lateinit var binding: FragmentImeadiaTabBinding
    private lateinit var viewModel: IMediaTabViewModel
    private val service = IMediaService(mainAPI)
    private val repository = IMediaRepository(service)
    private val viewModelFactory = IMediaTabViewModelFactory(repository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentImeadiaTabBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[IMediaTabViewModel::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            if (tab == 1 || tab == 4) {
                layoutInformation.visibility = View.GONE
                layoutBrand.visibility = View.VISIBLE
            } else {
                layoutInformation.visibility = View.VISIBLE
                layoutBrand.visibility = View.GONE
            }

            val bottomLayoutParam = layoutBottom.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(16) ?: 0)
            layoutBottom.layoutParams = bottomLayoutParam

            imgBrand.clipToOutline = true
            viewModel.getBrandByVendor().observe(viewLifecycleOwner) {
                Log.d("IMediaTabFragment", "getBrandByVendor: $it")
                val brandList = it.getOrNull() ?: emptyList()
                viewModel.brandList.value = brandList
                if (brandList.isNotEmpty()) {
                    val firstBrand = brandList.first()
                    viewModel.selectedBrand.value = firstBrand
                    Glide.with(this@IMediaTabFragment)
                        .load(firstBrand.brandMapping?.linkLogo)
                        .into(imgBrand)
                    getIMediaGifts(firstBrand.brandMapping?.brandId ?: 0)
                }
                val adapter = IMediaBrandAdapter(brandList)
                adapter.onItemClick = { brand ->
                    if (brand != null) {
                        viewModel.selectedBrand.value = brand
                        getIMediaGifts(brand.brandMapping?.brandId ?: 0)
                    }
                }
                listBrand.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                listBrand.adapter = adapter
            }
            viewModel.getDiscountedIMedia(tab + 5).observe(viewLifecycleOwner) {
                Log.d("IMediaTabFragment", "getDiscountedIMedia: $it")
            }

            imgBrand.setOnClickListener {
                val bottomSheet = IMediaBrandBottomSheet(
                    viewModel.selectedBrand.value ?: return@setOnClickListener,
                    viewModel.brandList.value ?: emptyList()
                )
                bottomSheet.onApplyBrand = { brand ->
                    viewModel.selectedBrand.value = brand
                    Glide.with(this@IMediaTabFragment)
                        .load(brand.brandMapping?.linkLogo)
                        .into(imgBrand)
                    getIMediaGifts(brand.brandMapping?.brandId ?: 0)
                }
                bottomSheet.show(childFragmentManager, "IMediaBrandBottomSheet")
            }

            val layoutParams = btnApply.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(8) ?: 0)
            btnApply.layoutParams = layoutParams
            btnApply.setOnClickListener {
                viewModel.selectedGift.value?.let { gift ->
                    Log.d("IMediaTabFragment", "Selected gift: $gift")
                    (activity as LynkiDSDKActivity).navigateFromIMediaToGiftExchangeFragment(
                        gift.giftInfor?.id ?: 0, TopupRedeemInfo(
                            operation = if (tab == 0 || tab == 2 || tab == 3) 1200 else 1000,
                            ownerPhone = if (tab == 0 || tab == 2 || tab == 3) LynkiD_SDK.phoneNumber else edtPhoneNumber.text.toString(),
                            accountType = if (tab == 0) 0 else if (tab == 2) 1 else null,
                            type = tab,
                            brand = viewModel.selectedBrand.value?.brandMapping?.brandName,
                            brandImage = viewModel.selectedBrand.value?.brandMapping?.linkLogo,
                            price = gift.giftInfor?.fullPrice ?: 0.0,
                            bandwidth = gift.giftInfor?.name?.split("/")?.first(),
                            time = if (gift.giftInfor?.name?.contains("/") == true) "1 ngày" else gift.giftInfor?.description?.split(
                                ":"
                            )?.lastOrNull()?.trimStart()
                        )
                    )
                }
            }
        }
    }

    private fun getIMediaGifts(brandId: Int) {
        viewModel.getAllIMedia(brandId).observe(viewLifecycleOwner) {
            Log.d("IMediaTabFragment", "getAllIMedia: $it")
            binding.apply {
                val giftList = it.getOrNull()?.items ?: emptyList()
                if (tab < 3) {
                    val iMediaList = listOf(Pair("Mệnh giá", giftList))
                    val adapter = IMediaGroupAdapter(iMediaList, 0)
                    adapter.onItemClick = { gift -> viewModel.selectedGift.value = gift }
                    binding.listIMedia.layoutManager = LinearLayoutManager(context)
                    binding.listIMedia.adapter = adapter
                } else {
                    val iMediaList = giftList
                        .groupBy { gift -> gift.giftInfor?.description?.split(':')?.firstOrNull() }
                        .map { (key, value) -> Pair(key ?: "", value) }
                    val adapter = IMediaGroupAdapter(iMediaList, 1)
                    adapter.onItemClick = { gift -> viewModel.selectedGift.value = gift }
                    binding.listIMedia.layoutManager = LinearLayoutManager(context)
                    binding.listIMedia.adapter = adapter
                }
            }
        }
    }
}