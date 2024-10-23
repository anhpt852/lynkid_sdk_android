package vn.linkid.sdk.my_reward.ui

import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentMyRewardDetailBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import vn.linkid.sdk.models.my_reward.RewardStatus
import vn.linkid.sdk.models.my_reward.RewardUsedStatus
import vn.linkid.sdk.models.my_reward.WhyHaveRewardType
import vn.linkid.sdk.my_reward.adapter.MyRewardDetailAddressAdapter
import vn.linkid.sdk.my_reward.repository.MyRewardDetailRepository
import vn.linkid.sdk.my_reward.service.MyRewardDetailService
import vn.linkid.sdk.my_reward.viewmodel.MyRewardDetailViewModel
import vn.linkid.sdk.my_reward.viewmodel.MyRewardDetailViewModelFactory
import vn.linkid.sdk.utils.copyToClipboard
import vn.linkid.sdk.utils.formatDate
import vn.linkid.sdk.utils.openCall
import vn.linkid.sdk.utils.openEmail
import java.util.Date
import java.util.Locale

class MyRewardDetailFragment : Fragment() {

    private lateinit var binding: FragmentMyRewardDetailBinding
    private lateinit var viewModel: MyRewardDetailViewModel
    private val service = MyRewardDetailService(mainAPI)
    private val repository = MyRewardDetailRepository(service)
    private val viewModelFactory = MyRewardDetailViewModelFactory(repository)

    private val args: MyRewardDetailFragmentArgs by navArgs()
    private val transactionCode: String by lazy { args.transactionCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyRewardDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[MyRewardDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMyRewardDetail(transactionCode).observe(viewLifecycleOwner) {
            val giftInfoItem = it.getOrNull()?.result?.items?.firstOrNull()
            if (giftInfoItem != null) {
                setUpView(giftInfoItem)
            }
        }
    }

    private fun setUpView(giftInfoItem: GiftInfoItem) {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            setUpCommonView(giftInfoItem)
            setUpLocationList(giftInfoItem)
            if (giftInfoItem.eGift != null) {
                setUpEGiftView(giftInfoItem)
            } else {
                setUpPhysicalGiftView(giftInfoItem)
            }
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpCommonView(giftInfoItem: GiftInfoItem) {
        val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
        toolbar.layoutParams = layoutParams

        val backgroundLayoutParams = imgHeaderBackground.layoutParams
        backgroundLayoutParams.height = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0) + (context?.dpToPx(56) ?: 0) + (context?.dpToPx(40) ?: 0)
        imgHeaderBackground.layoutParams = backgroundLayoutParams

        txtGiftName.text = giftInfoItem.giftTransaction?.giftName

        Glide.with(imgBrand)
            .load(giftInfoItem.brandInfo?.brandImage)
            .circleCrop()
            .placeholder(R.drawable.img_lynkid)
            .into(imgBrand)


        if ((giftInfoItem.giftTransaction?.description ?: "").isNotEmpty()) {
            txtIntroduceTitle.visibility = View.VISIBLE
            webViewIntroduce.visibility = View.VISIBLE
            dividerIntroduce.visibility = View.VISIBLE
            webViewIntroduce.loadData(giftInfoItem.giftTransaction?.description ?: "", "text/html", "UTF-8")
        } else {
            txtIntroduceTitle.visibility = View.GONE
            webViewIntroduce.visibility = View.GONE
            dividerIntroduce.visibility = View.GONE
        }
        if ((giftInfoItem.giftTransaction?.condition ?: "").isNotEmpty()) {
            layoutCondition.visibility = View.VISIBLE
            webViewCondition.loadData(giftInfoItem.giftTransaction?.condition ?: "", "text/html", "UTF-8")
        } else {
            layoutCondition.visibility = View.GONE
        }
        if ((giftInfoItem.giftTransaction?.introduce ?: "").isNotEmpty()) {
            txtInstructionTitle.visibility = View.VISIBLE
            webViewInstruction.visibility = View.VISIBLE
            dividerInstruction.visibility = View.VISIBLE
            webViewInstruction.loadData(giftInfoItem.giftTransaction?.introduce ?: "", "text/html", "UTF-8")
        } else {
            txtInstructionTitle.visibility = View.GONE
            webViewInstruction.visibility = View.GONE
            dividerInstruction.visibility = View.GONE
        }

        txtContactHotline.setOnClickListener { openCall(requireContext(), giftInfoItem.giftTransaction?.contactHotline ?: "") }
        txtContactEmail.setOnClickListener { openEmail(requireContext(), giftInfoItem.giftTransaction?.contactEmail ?: "") }
        if ((giftInfoItem.giftTransaction?.contactEmail
                ?: "").isNotEmpty() && (giftInfoItem.giftTransaction?.contactHotline ?: "").isNotEmpty()
        ) {
            txtContactTitle.visibility = View.VISIBLE
            txtContactBody.visibility = View.VISIBLE
            txtContactEmail.visibility = View.VISIBLE
            txtContactEmailTitle.visibility = View.VISIBLE
            txtContactHotline.visibility = View.VISIBLE
            txtContactHotlineTitle.visibility = View.VISIBLE
            txtContactEmail.text = giftInfoItem.giftTransaction?.contactEmail ?: ""
            txtContactHotline.text = giftInfoItem.giftTransaction?.contactHotline ?: ""
        } else if ((giftInfoItem.giftTransaction?.contactEmail ?: "").isNotEmpty()) {
            txtContactTitle.visibility = View.VISIBLE
            txtContactBody.visibility = View.VISIBLE
            txtContactEmail.visibility = View.VISIBLE
            txtContactEmailTitle.visibility = View.VISIBLE
            txtContactHotline.visibility = View.GONE
            txtContactHotlineTitle.visibility = View.GONE
            txtContactEmail.text = giftInfoItem.giftTransaction?.contactEmail ?: ""
        } else if ((giftInfoItem.giftTransaction?.contactHotline ?: "").isNotEmpty()) {
            txtContactTitle.visibility = View.VISIBLE
            txtContactBody.visibility = View.VISIBLE
            txtContactEmail.visibility = View.GONE
            txtContactEmailTitle.visibility = View.GONE
            txtContactHotline.visibility = View.VISIBLE
            txtContactHotlineTitle.visibility = View.VISIBLE
            txtContactHotline.text = giftInfoItem.giftTransaction?.contactHotline ?: ""
        } else {
            txtContactTitle.visibility = View.GONE
            txtContactBody.visibility = View.GONE
            txtContactEmail.visibility = View.GONE
            txtContactEmailTitle.visibility = View.GONE
            txtContactHotline.visibility = View.GONE
            txtContactHotlineTitle.visibility = View.GONE
        }

//        webViewIntroduce.loadUrl(giftInfoItem.giftTransaction?.giftDescription ?: "")
//        webViewInstruction.loadUrl(giftInfoItem.giftTransaction?.introduce ?: "")
//        txtContactEmail.text = giftInfoItem.giftTransaction?.contactEmail ?: ""
//        txtContactHotline.text = giftInfoItem.giftTransaction?.contactHotline ?: ""
    }

    private fun FragmentMyRewardDetailBinding.setUpLocationList(giftInfoItem: GiftInfoItem) {
        if (giftInfoItem.giftUsageAddress.isNullOrEmpty()) {
            layoutUsageAddress.visibility = View.GONE
        } else {
            val adapter = MyRewardDetailAddressAdapter(giftInfoItem.giftUsageAddress ?: emptyList())
            adapter.onItemClick = {
                Log.d("TAG", "setUpAddress: $it")
            }
            listAddress.layoutManager = LinearLayoutManager(context)
            listAddress.adapter = adapter
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpEGiftView(giftInfoItem: GiftInfoItem) {
        val expiredDate = formatDate(giftInfoItem.eGift?.expiredDate)
        val sentDate = formatDate(giftInfoItem.giftTransaction?.transferTime)
        val usedDate = formatDate(giftInfoItem.giftTransaction?.eGiftUsedAt)

        val (text, color) = when {
            giftInfoItem.giftTransaction?.whyHaveIt == WhyHaveRewardType.SENT -> "Đã tặng vào: $sentDate" to "#F5574E"
            giftInfoItem.eGift?.usedStatus == RewardUsedStatus.EXPIRED -> "Hết hạn vào: $expiredDate" to "#F5574E"
            giftInfoItem.eGift?.usedStatus == RewardUsedStatus.USED && usedDate.isNotEmpty() -> "Đã dùng vào: $usedDate" to "#F5574E"
            giftInfoItem.eGift?.usedStatus == RewardUsedStatus.USED -> "Đã sử dụng" to "#F5574E"
            expiredDate.isNotEmpty() -> "HSD: $expiredDate" to "#6D6B7A"
            else -> "" to "#6D6B7A"
        }
        txtExpireDate.text = text
        txtExpireDate.setTextColor(Color.parseColor(color))

        if (giftInfoItem.giftTransaction?.whyHaveIt == WhyHaveRewardType.RECEIVED) {
            tagGift.visibility = View.VISIBLE
        }

        layoutEGiftFooter.visibility = View.VISIBLE


        val codeImage = giftInfoItem.giftTransaction?.qrCode
        val codeString = giftInfoItem.eGift?.code

        if (!codeImage.isNullOrEmpty()) {
            Glide.with(imgCode)
                .load(codeImage)
                .into(imgCode)
        } else {
            imgCode.visibility = View.GONE
        }
        if (!codeString.isNullOrEmpty()) {
            txtCode.text = codeString
        } else {
            txtCode.visibility = View.GONE
        }

        val fullText = "Lưu ý: Không cung cấp ảnh chụp màn hình cho nhân viên để thanh toán."
        val spannable = SpannableString(fullText)
        spannable.setSpan(
            ForegroundColorSpan(Color.parseColor("#F5574E")),
            0,
            7,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )  // Adjust index based on actual text
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtWarning.text = spannable

        btnCopy.setOnClickListener {
            copyToClipboard(
                requireContext(),
                codeString,
                "gift_code",
                "Đã sao chép mã quà tặng"
            )
        }

    }


    private fun FragmentMyRewardDetailBinding.setUpPhysicalGiftView(giftInfoItem: GiftInfoItem) {
        layoutPhysicalFooter.visibility = View.VISIBLE
        when (giftInfoItem.giftTransaction?.rewardStatus) {
            RewardStatus.PENDING -> setUpPhysicalTypeOne(1)
            RewardStatus.WAITING -> setUpPhysicalTypeOne(1)
            RewardStatus.DELIVERING -> setUpPhysicalTypeOne(2)
            RewardStatus.DELIVERED -> setUpPhysicalTypeOne(3)
            RewardStatus.RETURNING -> setUpPhysicalTypeOne(3)
            RewardStatus.RETURNED -> setUpPhysicalTypeThree(4)
            RewardStatus.CANCELLING -> setUpPhysicalTypeThree(4)
            RewardStatus.CANCELED -> setUpPhysicalTypeThree(4)
            RewardStatus.CONFIRM_FAILED -> setUpPhysicalTypeOne(1)
            RewardStatus.CONFIRMED -> setUpPhysicalTypeOne(1)
            RewardStatus.REJECTED -> setUpPhysicalTypeTwo(2)
            RewardStatus.APPROVED -> setUpPhysicalTypeOne(1)
            null -> layoutPhysicalFooter.visibility = View.GONE
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpPhysicalTypeOne(step: Int) {
        layoutStep2.visibility = View.GONE
        layoutStep2Image.visibility = View.GONE
        txtStep2Description.visibility = View.GONE
        layoutStep4.visibility = View.GONE
        layoutStep4Image.visibility = View.GONE
        txtStep4Description.visibility = View.GONE

        txtStep1Description.text = "Đang xử lý"
        txtStep3Description.text = "Đang giao hàng"
        txtStep3.text = "2"
        txtStep5Description.text = "Đã giao hàng"
        txtStep5.text = "3"
        if (step >= 1) {
            progressShipping.progress = 20
            layoutStep1.visibility = View.VISIBLE
            layoutStep1Image.visibility = View.VISIBLE
            txtStep1Description.visibility = View.VISIBLE
            layoutStep1Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep1Description.setTextColor(Color.parseColor("#663692"))
            txtStep1.visibility = View.GONE
            imgStep1Done.visibility = View.VISIBLE
            layoutStep1.setBackgroundResource(R.drawable.bg_circular_purple)
        }
        if (step >= 2) {
            progressShipping.progress = 50
            layoutStep3.visibility = View.VISIBLE
            layoutStep3Image.visibility = View.VISIBLE
            txtStep3Description.visibility = View.VISIBLE
            layoutStep3Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep3Description.setTextColor(Color.parseColor("#663692"))
            txtStep3.visibility = View.GONE
            imgStep3Done.visibility = View.VISIBLE
            layoutStep3.setBackgroundResource(R.drawable.bg_circular_purple)
        }
        if (step >= 3) {
            progressShipping.progress = 100
            layoutStep5.visibility = View.VISIBLE
            layoutStep5Image.visibility = View.VISIBLE
            txtStep5Description.visibility = View.VISIBLE
            layoutStep5Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep5Description.setTextColor(Color.parseColor("#663692"))
            txtStep5.visibility = View.GONE
            imgStep5Done.visibility = View.VISIBLE
            layoutStep5.setBackgroundResource(R.drawable.bg_circular_purple)
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpPhysicalTypeTwo(step: Int) {
        layoutStep2.visibility = View.GONE
        layoutStep2Image.visibility = View.GONE
        txtStep2Description.visibility = View.GONE
        layoutStep3.visibility = View.GONE
        layoutStep3Image.visibility = View.GONE
        txtStep3Description.visibility = View.GONE
        layoutStep4.visibility = View.GONE
        layoutStep4Image.visibility = View.GONE
        txtStep4Description.visibility = View.GONE

        txtStep1Description.text = "Đang xử lý"
        txtStep5Description.text = "Đã hủy"
        txtStep5.text = "2"
        if (step >= 1) {
            progressShipping.progress = 20
            layoutStep1.visibility = View.VISIBLE
            layoutStep1Image.visibility = View.VISIBLE
            txtStep1Description.visibility = View.VISIBLE
            layoutStep1Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep1Description.setTextColor(Color.parseColor("#663692"))
            txtStep1.visibility = View.GONE
            imgStep1Done.visibility = View.VISIBLE
            layoutStep1.setBackgroundResource(R.drawable.bg_circular_purple)
        }
        if (step >= 2) {
            progressShipping.progress = 100
            layoutStep5.visibility = View.VISIBLE
            layoutStep5Image.visibility = View.VISIBLE
            txtStep5Description.visibility = View.VISIBLE
            layoutStep5Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep5Description.setTextColor(Color.parseColor("#663692"))
            txtStep5.visibility = View.GONE
            imgStep5Done.visibility = View.VISIBLE
            layoutStep5.setBackgroundResource(R.drawable.bg_circular_purple)
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpPhysicalTypeThree(step: Int) {
        layoutStep3.visibility = View.GONE
        layoutStep3Image.visibility = View.GONE
        txtStep3Description.visibility = View.GONE

        txtStep1Description.text = "Đang xử lý"
        txtStep2Description.text = "Đang giao hàng"
        txtStep2.text = "2"
        txtStep4Description.text = "Vận chuyển trả hàng"
        txtStep4.text = "3"
        txtStep5Description.text = "Đã huỷ"
        txtStep5.text = "4"
        if (step >= 1) {
            progressShipping.progress = 20
            layoutStep1.visibility = View.VISIBLE
            layoutStep1Image.visibility = View.VISIBLE
            txtStep1Description.visibility = View.VISIBLE
            layoutStep1Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep1Description.setTextColor(Color.parseColor("#663692"))
            txtStep1.visibility = View.GONE
            imgStep1Done.visibility = View.VISIBLE
            layoutStep1.setBackgroundResource(R.drawable.bg_circular_purple)
        }
        if (step >= 2) {
            progressShipping.progress = 40
            layoutStep2.visibility = View.VISIBLE
            layoutStep2Image.visibility = View.VISIBLE
            txtStep2Description.visibility = View.VISIBLE
            layoutStep2Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep2Description.setTextColor(Color.parseColor("#663692"))
            txtStep2.visibility = View.GONE
            imgStep2Done.visibility = View.VISIBLE
            layoutStep2.setBackgroundResource(R.drawable.bg_circular_purple)
        }
        if (step >= 3) {
            progressShipping.progress = 60
            layoutStep4.visibility = View.VISIBLE
            layoutStep4Image.visibility = View.VISIBLE
            txtStep4Description.visibility = View.VISIBLE
            layoutStep4Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep4Description.setTextColor(Color.parseColor("#663692"))
            txtStep4.visibility = View.GONE
            imgStep4Done.visibility = View.VISIBLE
            layoutStep4.setBackgroundResource(R.drawable.bg_circular_purple)
        }
        if (step >= 4) {
            progressShipping.progress = 100
            layoutStep5.visibility = View.VISIBLE
            layoutStep5Image.visibility = View.VISIBLE
            txtStep5Description.visibility = View.VISIBLE
            layoutStep5Image.setBackgroundResource(R.drawable.bg_circular_purple)
            txtStep5Description.setTextColor(Color.parseColor("#663692"))
            txtStep5.visibility = View.GONE
            imgStep5Done.visibility = View.VISIBLE
            layoutStep5.setBackgroundResource(R.drawable.bg_circular_purple)
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpTopUpView(giftInfoItem: GiftInfoItem) {

    }

}