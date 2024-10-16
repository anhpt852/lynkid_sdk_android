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
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.imedia.adapter.IMediaAdapter
import vn.linkid.sdk.imedia.adapter.IMediaBrandAdapter
import vn.linkid.sdk.imedia.adapter.IMediaGroupAdapter
import vn.linkid.sdk.imedia.adapter.IMediaHotAdapter
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.imedia.service.IMediaService
import vn.linkid.sdk.imedia.viewmodel.IMediaTabViewModel
import vn.linkid.sdk.imedia.viewmodel.IMediaTabViewModelFactory
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.models.imedia.TopupRedeemInfo
import vn.linkid.sdk.utils.PhoneTextWatcher
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatPrice
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
    private val giftDetailService = GiftDetailService(mainAPI)
    private val repository = IMediaRepository(service)
    private val giftDetailRepository = GiftDetailRepository(giftDetailService)
    private val viewModelFactory = IMediaTabViewModelFactory(repository, giftDetailRepository)
    private lateinit var hotAdapter: IMediaHotAdapter
    private lateinit var adapter: IMediaGroupAdapter

    private lateinit var phoneNumberFormatter: PhoneTextWatcher

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
            setUpLayout()
            setUpPhoneInput()
            setUpBrand()
            setUpApplyButton()
        }
    }

    private fun FragmentImeadiaTabBinding.setUpApplyButton() {
        btnApply.setOnClickListener {
            if (phoneNumberFormatter.isValid()) {
                viewModel.selectedGift.value?.let { gift ->
                    Log.d("IMediaTabFragment", "Selected gift: $gift")
                    (activity as LynkiDSDKActivity).navigateFromIMediaToGiftExchangeFragment(
                        gift.giftInfor?.id ?: 0, TopupRedeemInfo(
                            operation = if (tab == 0 || tab == 2 || tab == 3) 1200 else 1000,
                            ownerPhone = if (tab == 1 || tab == 4) LynkiD_SDK.phoneNumber else formatPhoneNumber(
                                edtPhoneNumber.text.toString().replace(" ", "").replace("+84", "0")
                            ),
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

    private fun FragmentImeadiaTabBinding.setUpBrand() {
        imgBrand.clipToOutline = true
        viewModel.getBrandByVendor(tab).observe(viewLifecycleOwner) {
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
                setUpDiscountGifts(firstBrand.brandMapping?.brandId ?: 0)
            }
            val adapter = IMediaBrandAdapter(brandList)
            adapter.onItemClick = { brand ->
                if (brand != null) {
                    viewModel.selectedBrand.value = brand
                    getIMediaGifts(brand.brandMapping?.brandId ?: 0)
                    setUpDiscountGifts(brand.brandMapping?.brandId ?: 0)
                }
            }
            listBrand.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            listBrand.adapter = adapter
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
                setUpDiscountGifts(brand.brandMapping?.brandId ?: 0)
            }
            bottomSheet.show(childFragmentManager, "IMediaBrandBottomSheet")
        }
    }

    private fun FragmentImeadiaTabBinding.setUpDiscountGifts(brandId: Int) {
        viewModel.getDiscountedIMedia(tab + 5, brandId).observe(viewLifecycleOwner) {
            Log.d("IMediaTabFragment", "getDiscountedIMedia: $it")
            val giftList = when (tab) {
                0, 3 -> (it.getOrNull()?.items
                    ?: emptyList()).filter { gift ->
                    gift.giftInfor?.thirdPartyGiftCode == null
                }

                1, 4 -> (it.getOrNull()?.items
                    ?: emptyList()).filter { gift ->
                    gift.giftInfor?.thirdPartyGiftCode != null
                }

                else -> it.getOrNull()?.items ?: emptyList()
            }
            if (giftList.isEmpty()) {
                layoutHot.visibility = View.GONE
            } else {
                layoutHot.visibility = View.VISIBLE
                if (((it.getOrNull()?.masDiscount) ?: 0.toDouble()) > 0) {
                    layoutSale.visibility = View.VISIBLE
                    txtSalePercent.text = "-${it.getOrNull()?.masDiscount ?: 0}%"
                } else {
                    layoutSale.visibility = View.GONE
                }
                hotAdapter = IMediaHotAdapter(giftList, 0)
                hotAdapter.onItemClick = { gift ->
                    viewModel.selectedGift.value = GiftDetail(
                        giftInfor = gift?.giftInfor,
                        imageLink = gift?.imageLink,
                        flashSaleProgramInfor = gift?.flashSaleProgramInfor,
                        giftDiscountInfor = gift?.giftDiscountInfor,
                        giftUsageAddress = null,
                        errorCode = null,
                        giftCategoryTypeCode = null,
                        feeInfor = null,
                        balanceAbleToCashout = null
                    )
                    checkBalance()
                    if (::adapter.isInitialized) {
                        adapter.clearAllSelections()
                    }
                }
                listIMediaHot.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                listIMediaHot.adapter = hotAdapter
            }

        }

    }

    private fun FragmentImeadiaTabBinding.setUpPhoneInput() {
        phoneNumberFormatter = PhoneTextWatcher(
            edtPhoneNumber,
            inputPhoneNumber,
            onValidButton = { isValid ->
                if (isValid && viewModel.selectedGift.value != null) {
                    checkBalance()
                } else {
                    btnApply.alpha = 0.6f
                }
            },
            "Thông tin chưa đúng định dạng"
        )
        edtPhoneNumber.addTextChangedListener(phoneNumberFormatter)
        edtPhoneNumber.setText(LynkiD_SDK.phoneNumber)
    }

    private fun FragmentImeadiaTabBinding.setUpLayout() {
        if (tab == 1 || tab == 4) {
            layoutInformation.visibility = View.GONE
            layoutBrand.visibility = View.VISIBLE
        } else {
            layoutInformation.visibility = View.VISIBLE
            layoutBrand.visibility = View.GONE
        }

        layoutNote.visibility = if (tab == 2) View.VISIBLE else View.GONE

        val bottomLayoutParam = btnApply.layoutParams as ViewGroup.MarginLayoutParams
        bottomLayoutParam.bottomMargin =
            getNavigationBarHeight(root) + (context?.dpToPx(48) ?: 0)
        btnApply.layoutParams = bottomLayoutParam


        viewModel.pointInfo.observe(viewLifecycleOwner) { pointInfo ->
            if (pointInfo.getOrNull() != null) {
                Log.d("IMediaTabFragment", "Point info: ${pointInfo.getOrNull()}")
            }
        }
    }

    private fun formatPhoneNumber(phoneNumber: String) = if (phoneNumber.startsWith("0")) {
        "+84${phoneNumber.substring(1)}"
    } else {
        phoneNumber
    }

    private fun getIMediaGifts(brandId: Int) {
        viewModel.getAllIMedia(brandId).observe(viewLifecycleOwner) {
            Log.d("IMediaTabFragment", "getAllIMedia: $it")
            binding.apply {
                val giftList = when (tab) {
                    0, 3 -> (it.getOrNull()?.items
                        ?: emptyList()).filter { gift ->
                        gift.giftInfor?.thirdPartyGiftCode == null
                    }

                    1, 4 -> (it.getOrNull()?.items
                        ?: emptyList()).filter { gift ->
                        gift.giftInfor?.thirdPartyGiftCode != null
                    }

                    else -> it.getOrNull()?.items ?: emptyList()
                }
                if (tab < 3) {
                    val iMediaList = listOf(Pair("Mệnh giá", giftList))
                    adapter = IMediaGroupAdapter(iMediaList, 0)
                    adapter.onItemClick = { gift ->
                        viewModel.selectedGift.value = gift
                        checkBalance()
                        if (::hotAdapter.isInitialized) {
                            hotAdapter.clearSelection()
                        }
                    }
                    listIMedia.layoutManager = LinearLayoutManager(context)
                    listIMedia.adapter = adapter
                } else {
                    val iMediaList = giftList
                        .groupBy { gift -> gift.giftInfor?.description?.split(':')?.firstOrNull() }
                        .map { (key, value) -> Pair(key ?: "", value) }
                    adapter = IMediaGroupAdapter(iMediaList, 1)
                    adapter.onItemClick = { gift ->
                        viewModel.selectedGift.value = gift
                        checkBalance()
                        if (::hotAdapter.isInitialized) {
                            hotAdapter.clearSelection()
                        }
                    }
                    listIMedia.layoutManager = LinearLayoutManager(context)
                    listIMedia.adapter = adapter
                }
            }
        }
    }

    private fun FragmentImeadiaTabBinding.checkBalance() {
        if (viewModel.selectedGift.value != null) {
            val totalCost = (viewModel.selectedGift.value?.giftInfor?.requiredCoin ?: 0.0)
            val balance = viewModel.pointInfo.value?.getOrNull()?.tokenBalance ?: 0.0
            layoutBalanceCheck.visibility = if (balance >= totalCost) View.GONE else View.VISIBLE
            txtBalanceCheck.text =
                "Tích thêm ${(totalCost - balance).formatPrice()} điểm nữa để đổi ưu đãi nhé. Khám phá ngay."
            btnApply.alpha = if (balance >= totalCost) 1f else 0.6f
        } else {
            layoutBalanceCheck.visibility = View.GONE
            btnApply.alpha = 0.6f
        }
    }
}