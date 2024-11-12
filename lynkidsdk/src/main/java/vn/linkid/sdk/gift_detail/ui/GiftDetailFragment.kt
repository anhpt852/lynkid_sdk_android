package vn.linkid.sdk.gift_detail.ui

import android.os.Build
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
import vn.linkid.sdk.databinding.FragmentGiftDetailBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.gift_detail.adapter.ImagePagerAdapter
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.gift_detail.viewmodel.GiftDetailViewModel
import vn.linkid.sdk.gift_detail.viewmodel.GiftDetailViewModelFactory
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.my_reward.adapter.MyRewardDetailAddressAdapter
import vn.linkid.sdk.utils.openCall
import vn.linkid.sdk.utils.openEmail
import vn.linkid.sdk.utils.openInstallApp
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class GiftDetailFragment : Fragment() {

    private lateinit var binding: FragmentGiftDetailBinding
    private lateinit var viewModel: GiftDetailViewModel
    private val service = GiftDetailService(mainAPI)
    private val repository = GiftDetailRepository(service)
    private val viewModelFactory = GiftDetailViewModelFactory(repository)

    private val args: GiftDetailFragmentArgs by navArgs()
    private val giftId: Int by lazy { args.giftId }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGiftDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[GiftDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val statusHeight = getStatusBarHeight(root)
            fun updateTopMargin(view: View, additionalDp: Int) {
                val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = statusHeight + (context?.dpToPx(additionalDp) ?: 0)
                view.layoutParams = layoutParams
            }
            updateTopMargin(toolbar, 12)
            updateTopMargin(bgBack, 20)
            updateTopMargin(btnBack, 20)
            updateTopMargin(btnExitPromo, 20)
            updateTopMargin(scrollView, 12)

            btnBack.setOnClickListener { findNavController().popBackStack() }


            val bottomLayoutParam = btnExchange.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(16) ?: 0)
            btnExchange.layoutParams = bottomLayoutParam

            val bottomPromoLayoutParam = btnInstallApp.layoutParams as ViewGroup.MarginLayoutParams
            bottomPromoLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(16) ?: 0)
            btnInstallApp.layoutParams = bottomPromoLayoutParam
        }

        viewModel.getGiftDetail(giftId).observe(viewLifecycleOwner) { result ->
            result.getOrNull()?.let { giftDetail ->
                setUpView(giftDetail)
            }
        }
    }

    private fun setUpView(giftDetail: GiftDetail) {
        binding.apply {
            setUpScroll()
            setUpBasicInfo(giftDetail)
            setUpPrice(giftDetail)
            setUpFlashSale(giftDetail)
            setUpDescription(giftDetail)
            setUpAddress(giftDetail)
            setUpExchangeButton(giftDetail)
        }
    }

    private fun FragmentGiftDetailBinding.setUpScroll() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                toolbar.alpha = scrollY.toFloat() / 200
                bgBack.alpha = 1 - scrollY.toFloat() / 200
            }
        }
    }

    private fun FragmentGiftDetailBinding.setUpBasicInfo(giftDetail: GiftDetail) {
        txtGiftName.text = giftDetail.giftInfor?.name ?: ""
        fun formatExpireDate(expireData: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            return when {
                expireData.contains("-") && expireData.contains(":") ->
                    "Hạn sử dụng: ${
                        inputFormat.parse(expireData)?.let { Date(it.time + 7 * 3600 * 1000) }
                    }"

                expireData.lowercase().contains("tối thiểu") ->
                    "Ưu đãi ${expireData.lowercase()} kể từ ngày đổi"

                else ->
                    "Ưu đãi trong ${expireData.lowercase()} kể từ ngày đổi"
            }
        }

        val expireData = giftDetail.giftInfor?.expireDuration ?: ""
        if (expireData.isNotEmpty()) {
            txtExpireDate.visibility = View.VISIBLE
            btnExpiredInfo.visibility = View.VISIBLE
            txtExpireDate.text = formatExpireDate(expireData)
            btnExpiredInfo.visibility = View.VISIBLE
        } else {
            txtExpireDate.visibility = View.GONE
            btnExpiredInfo.visibility = View.GONE
        }


        val imageList = giftDetail.imageLink ?: emptyList()
        val imageStringList = imageList.map { it.link ?: "" }
        val pagerAdapter = ImagePagerAdapter(imageStringList.ifEmpty { listOf("") })
        pagerImage.adapter = pagerAdapter

        btnInstallAppSmall.setOnClickListener { openInstallApp(requireContext()) }
    }

    private fun FragmentGiftDetailBinding.setUpPrice(giftDetail: GiftDetail) {
        val fullPrice = giftDetail.giftInfor?.fullPrice ?: 0.0
        val discountPrice = giftDetail.giftInfor?.discountPrice ?: 0.0
        val requiredPrice = giftDetail.giftInfor?.requiredCoin ?: 0.0
        Log.d("TAG", "setUpPrice: ${giftDetail.giftInfor} $fullPrice $discountPrice $requiredPrice")

        txtPrice.text = requiredPrice.formatPrice()
        txtPriceSale.visibility = if (discountPrice > 0) View.VISIBLE else View.GONE
        layoutSale.visibility = if (discountPrice > 0) View.VISIBLE else View.GONE
        if (discountPrice > 0) {
            txtPriceSale.text = fullPrice.formatPrice()
            txtSalePercent.text = "-${requiredPrice * 100 / fullPrice}%"
            txtPrice.text = discountPrice.formatPrice()
            txtFlashPriceSale.text = fullPrice.formatPrice()
            txtFlashSalePercent.text = "-${discountPrice * 100 / fullPrice}%"
        } else {
            txtPrice.text = fullPrice.formatPrice()
        }

    }

    private fun FragmentGiftDetailBinding.setUpFlashSale(giftDetail: GiftDetail) {
        if ((giftDetail.giftDiscountInfor?.remainingQuantityFlashSale ?: 0) > 0) {
            viewModel.flashSale.observe(viewLifecycleOwner) { flashSale ->
                val flashSaleItem = flashSale?.getOrNull()?.result?.items?.firstOrNull()
                if (flashSaleItem != null) {
                    layoutFlashSale.visibility = View.VISIBLE
                    val currentTime = Calendar.getInstance().apply {
                        timeZone = TimeZone.getTimeZone("UTC")
                        add(Calendar.HOUR_OF_DAY, 7)
                    }
                    val dateFormat =
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()).apply {
                            timeZone = TimeZone.getTimeZone("UTC")
                        }
                    val startTime = flashSaleItem.startTime?.let { dateFormat.parse(it) }
                    val endTime = flashSaleItem.endTime?.let { dateFormat.parse(it) }

                    val diffEndCurrent =
                        endTime?.time?.minus(currentTime.timeInMillis)?.div(1000) ?: 0
                    val diffStartCurrent =
                        startTime?.time?.minus(currentTime.timeInMillis)?.div(1000) ?: 0

                    if (diffStartCurrent > 0) {
                        txtFlashSaleEnd.text = "Diễn ra sau"
                        imgCoin.visibility = View.GONE
                        txtPrice.visibility = View.GONE
                        txtPriceSale.visibility = View.GONE
                        layoutSale.visibility = View.GONE
                        viewModel.countdownFlashSaleTime.value = diffStartCurrent
                        viewModel.startCountdownFlashSaleTime()
                    } else if (diffEndCurrent > 0) {
                        txtFlashSaleEnd.text = "Kết thúc sau"
                        imgCoin.visibility = View.GONE
                        txtPrice.visibility = View.GONE
                        txtPriceSale.visibility = View.GONE
                        layoutSale.visibility = View.GONE
                        viewModel.countdownFlashSaleTime.value = diffEndCurrent
                        viewModel.startCountdownFlashSaleTime()
                    } else {
                        layoutFlashSale.visibility = View.GONE
                        imgCoin.visibility = View.VISIBLE
                        txtPrice.visibility = View.VISIBLE
                        txtPriceSale.visibility = View.VISIBLE
                        layoutSale.visibility = View.VISIBLE
                    }
                } else {
                    layoutFlashSale.visibility = View.GONE
                    imgCoin.visibility = View.VISIBLE
                    txtPrice.visibility = View.VISIBLE
                    txtPriceSale.visibility = View.VISIBLE
                    layoutSale.visibility = View.VISIBLE
                }
            }
            viewModel.countdownFlashSaleTime.observe(viewLifecycleOwner) { time ->
                val hours = time / 3600
                val minutes = (time % 3600) / 60
                val seconds = time % 60
                txtFlashSaleHour.text = hours.toString()
                txtFlashSaleMinute.text = minutes.toString()
                txtFlashSaleSecond.text = seconds.toString()
            }
        }
    }

    private fun FragmentGiftDetailBinding.setUpDescription(giftDetail: GiftDetail) {
        if ((giftDetail.giftInfor?.description ?: "").isNotEmpty()) {
            txtIntroduceTitle.visibility = View.VISIBLE
            webViewIntroduce.visibility = View.VISIBLE
            dividerIntroduce.visibility = View.VISIBLE
            webViewIntroduce.loadData(giftDetail.giftInfor?.description ?: "", "text/html", "UTF-8")
        } else {
            txtIntroduceTitle.visibility = View.GONE
            webViewIntroduce.visibility = View.GONE
            dividerIntroduce.visibility = View.GONE
        }
        if ((giftDetail.giftInfor?.condition ?: "").isNotEmpty()) {
            layoutCondition.visibility = View.VISIBLE
            webViewCondition.loadData(giftDetail.giftInfor?.condition ?: "", "text/html", "UTF-8")
        } else {
            layoutCondition.visibility = View.GONE
        }
        if ((giftDetail.giftInfor?.introduce ?: "").isNotEmpty()) {
            txtInstructionTitle.visibility = View.VISIBLE
            webViewInstruction.visibility = View.VISIBLE
            dividerInstruction.visibility = View.VISIBLE
            webViewInstruction.loadData(giftDetail.giftInfor?.introduce ?: "", "text/html", "UTF-8")
        } else {
            txtInstructionTitle.visibility = View.GONE
            webViewInstruction.visibility = View.GONE
            dividerInstruction.visibility = View.GONE
        }

        txtContactHotline.setOnClickListener { openCall(requireContext(), giftDetail.giftInfor?.contactHotline ?: "") }
        txtContactEmail.setOnClickListener { openEmail(requireContext(), giftDetail.giftInfor?.contactEmail ?: "") }
        if ((giftDetail.giftInfor?.contactEmail
                ?: "").isNotEmpty() && (giftDetail.giftInfor?.contactHotline ?: "").isNotEmpty()
        ) {
            txtContactTitle.visibility = View.VISIBLE
            txtContactBody.visibility = View.VISIBLE
            txtContactEmail.visibility = View.VISIBLE
            txtContactEmailTitle.visibility = View.VISIBLE
            txtContactHotline.visibility = View.VISIBLE
            txtContactHotlineTitle.visibility = View.VISIBLE
            txtContactEmail.text = giftDetail.giftInfor?.contactEmail ?: ""
            txtContactHotline.text = giftDetail.giftInfor?.contactHotline ?: ""
        } else if ((giftDetail.giftInfor?.contactEmail ?: "").isNotEmpty()) {
            txtContactTitle.visibility = View.VISIBLE
            txtContactBody.visibility = View.VISIBLE
            txtContactEmail.visibility = View.VISIBLE
            txtContactEmailTitle.visibility = View.VISIBLE
            txtContactHotline.visibility = View.GONE
            txtContactHotlineTitle.visibility = View.GONE
            txtContactEmail.text = giftDetail.giftInfor?.contactEmail ?: ""
        } else if ((giftDetail.giftInfor?.contactHotline ?: "").isNotEmpty()) {
            txtContactTitle.visibility = View.VISIBLE
            txtContactBody.visibility = View.VISIBLE
            txtContactEmail.visibility = View.GONE
            txtContactEmailTitle.visibility = View.GONE
            txtContactHotline.visibility = View.VISIBLE
            txtContactHotlineTitle.visibility = View.VISIBLE
            txtContactHotline.text = giftDetail.giftInfor?.contactHotline ?: ""
        } else {
            txtContactTitle.visibility = View.GONE
            txtContactBody.visibility = View.GONE
            txtContactEmail.visibility = View.GONE
            txtContactEmailTitle.visibility = View.GONE
            txtContactHotline.visibility = View.GONE
            txtContactHotlineTitle.visibility = View.GONE
        }
    }

    private fun FragmentGiftDetailBinding.setUpAddress(giftDetail: GiftDetail) {
        if (giftDetail.giftUsageAddress.isNullOrEmpty()) {
            layoutUsageAddress.visibility = View.GONE
        } else {
            val adapter = MyRewardDetailAddressAdapter(giftDetail.giftUsageAddress ?: emptyList())
            adapter.onItemClick = {
                Log.d("TAG", "setUpAddress: $it")
            }
            listAddress.layoutManager = LinearLayoutManager(context)
            listAddress.adapter = adapter
            btnAddressMore.setOnClickListener {
                val action = GiftDetailFragmentDirections.actionGiftDetailFragmentToGiftUsageFragment(giftDetail.giftInfor?.code ?: "")
                findNavController().navigate(action)
            }
        }
    }

    private fun FragmentGiftDetailBinding.setUpExchangeButton(giftDetail: GiftDetail) {


        val maxAllowedRedemptionOfUser = giftDetail.giftInfor?.maxAllowedRedemptionOfUser ?: 0
        val maxQuantityPerRedemptionOfUser =
            giftDetail.giftInfor?.maxQuantityPerRedemptionOfUser ?: 0
        val totalRedemptionOfUser = giftDetail.giftInfor?.totalRedeemedOfUser ?: 0
        val remainingQuantity = maxAllowedRedemptionOfUser - totalRedemptionOfUser

        txtExchangeStatus.apply {
            when {
                maxAllowedRedemptionOfUser == 0 && maxQuantityPerRedemptionOfUser == 0 -> visibility =
                    View.GONE

                maxAllowedRedemptionOfUser != 0 -> {
                    visibility = View.VISIBLE
                    text = buildString {
                        append("Bạn còn $remainingQuantity/$maxAllowedRedemptionOfUser lượt đổi")
                        if (maxQuantityPerRedemptionOfUser != 0) {
                            append(". Tối đa $maxQuantityPerRedemptionOfUser quà/lượt đổi")
                        }
                    }
                }

                else -> visibility = View.GONE
            }
        }

        btnExchange.setOnClickListener {
            if ((viewModel.countdownFlashSaleTime.value ?: 0) > 0) {
                layoutFlashSalePromo.visibility = View.VISIBLE
            } else {
                val action =
                    if (giftDetail.giftInfor?.isEGift == true) {
                        GiftDetailFragmentDirections.actionGiftDetailFragmentToGiftExchangeFragment(
                            giftId
                        )
                    } else {
                        GiftDetailFragmentDirections.actionGiftDetailFragmentToGiftExchangeAddressFragment(
                            giftId
                        )
                    }
                findNavController().navigate(action)
            }
        }
        btnKeepExchange.setOnClickListener {
            layoutFlashSalePromo.visibility = View.GONE
            val action =
                if (giftDetail.giftInfor?.isEGift == true) {
                    GiftDetailFragmentDirections.actionGiftDetailFragmentToGiftExchangeFragment(
                        giftId
                    )
                } else {
                    GiftDetailFragmentDirections.actionGiftDetailFragmentToGiftExchangeAddressFragment(
                        giftId
                    )
                }
            findNavController().navigate(action)
        }
        btnExitPromo.setOnClickListener {
            layoutFlashSalePromo.visibility = View.GONE
        }
    }

}