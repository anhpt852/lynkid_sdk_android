package vn.linkid.sdk.gift_detail.ui

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
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
import java.util.Date
import java.util.Locale

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
            updateTopMargin(scrollView, 12)

            btnBack.setOnClickListener { findNavController().popBackStack() }
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
            setUpDescription(giftDetail)
            setUpAddress(giftDetail)
            setUpExchangeButton(giftDetail)
        }
    }

    private fun FragmentGiftDetailBinding.setUpScroll() {
        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            toolbar.alpha = scrollY.toFloat() / 200
            bgBack.alpha = 1 - scrollY.toFloat() / 200
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
        txtExpireDate.text = formatExpireDate(expireData)


        val imageList = giftDetail.imageLink ?: emptyList()
        val imageStringList = imageList.map { it.link ?: "" }
        val pagerAdapter = ImagePagerAdapter(imageStringList)
        pagerImage.adapter = pagerAdapter
    }

    private fun FragmentGiftDetailBinding.setUpPrice(giftDetail: GiftDetail) {
        val fullPrice = giftDetail.giftInfor?.fullPrice ?: 0.0
        val discountPrice = giftDetail.giftInfor?.discountPrice ?: 0.0
        val requiredPrice = giftDetail.giftInfor?.requiredCoin ?: 0.0

        txtPrice.text = requiredPrice.formatPrice()
        txtPriceSale.visibility = if (fullPrice > requiredPrice) View.VISIBLE else View.GONE
        layoutSale.visibility = txtPriceSale.visibility
        if (fullPrice > requiredPrice) {
            txtPriceSale.text = fullPrice.formatPrice()
            txtSalePercent.text = "-$discountPrice%"
        }

    }

    private fun FragmentGiftDetailBinding.setUpDescription(giftDetail: GiftDetail) {
        webViewIntroduce.loadData(giftDetail.giftInfor?.description ?: "", "text/html", "UTF-8")
        webViewCondition.loadData(giftDetail.giftInfor?.condition ?: "", "text/html", "UTF-8")
        webViewInstruction.loadData(giftDetail.giftInfor?.introduce ?: "", "text/html", "UTF-8")
        txtContactEmail.text = giftDetail.giftInfor?.contactEmail ?: ""
        txtContactHotline.text = giftDetail.giftInfor?.contactHotline ?: ""
    }

    private fun FragmentGiftDetailBinding.setUpAddress(giftDetail: GiftDetail) {
        val adapter = MyRewardDetailAddressAdapter(giftDetail.giftUsageAddress ?: emptyList())
        adapter.onItemClick = {
            Log.d("TAG", "setUpAddress: $it")
        }
        listAddress.layoutManager = LinearLayoutManager(context)
        listAddress.adapter = adapter
    }

    private fun FragmentGiftDetailBinding.setUpExchangeButton(giftDetail: GiftDetail) {
        val bottomLayoutParam = btnExchange.layoutParams as ViewGroup.MarginLayoutParams
        bottomLayoutParam.bottomMargin = getNavigationBarHeight(root) + (context?.dpToPx(16) ?: 0)
        btnExchange.layoutParams = bottomLayoutParam

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

}