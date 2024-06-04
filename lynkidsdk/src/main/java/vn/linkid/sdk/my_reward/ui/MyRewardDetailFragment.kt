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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentMyRewardDetailBinding
import vn.linkid.sdk.dpToPx
import vn.linkid.sdk.getStatusBarHeight
import vn.linkid.sdk.mainAPI
import vn.linkid.sdk.models.my_reward.GiftInfoItem
import vn.linkid.sdk.models.my_reward.RewardUsedStatus
import vn.linkid.sdk.models.my_reward.WhyHaveRewardType
import vn.linkid.sdk.my_reward.repository.MyRewardDetailRepository
import vn.linkid.sdk.my_reward.service.MyRewardDetailService
import vn.linkid.sdk.my_reward.viewmodel.MyRewardDetailViewModel
import vn.linkid.sdk.my_reward.viewmodel.MyRewardDetailViewModelFactory
import java.util.Date
import java.util.Locale

class MyRewardDetailFragment : Fragment() {

    private lateinit var binding: FragmentMyRewardDetailBinding
    private lateinit var viewModel: MyRewardDetailViewModel
    private val service = MyRewardDetailService(mainAPI)
    private val repository = MyRewardDetailRepository(service)
    private val viewModelFactory = MyRewardDetailViewModelFactory(repository)

    private val args: MyRewardDetailFragmentArgs by navArgs()
    private val transactionCode = args.transactionCode

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
            setUpCommonView(giftInfoItem)
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

        txtGiftName.text = giftInfoItem.giftTransaction?.giftName

        Glide.with(imgBrand)
            .load(giftInfoItem.brandInfo?.brandImage)
            .circleCrop()
            .placeholder(R.drawable.img_lynkid)
            .into(imgBrand)

        webViewIntroduce.loadUrl(giftInfoItem.giftTransaction?.giftDescription ?: "")
        webViewInstruction.loadUrl(giftInfoItem.giftTransaction?.introduce ?: "")
        txtContactEmail.text = giftInfoItem.giftTransaction?.contactEmail ?: ""
        txtContactHotline.text = giftInfoItem.giftTransaction?.contactHotline ?: ""
    }

    private fun FragmentMyRewardDetailBinding.setUpLocationList(giftInfoItem: GiftInfoItem){
        giftInfoItem.giftUsageAddress
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
        spannable.setSpan(ForegroundColorSpan(Color.parseColor("#F5574E")), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)  // Adjust index based on actual text
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txtWarning.text = spannable

    }

    private fun formatDate(date: String?): String {
        if (date.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC")
            val expireDate = inputFormat.parse(date)?.let {
                Date(it.time + 7 * 3600 * 1000) // Adjusting for UTC+7
            }

            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            outputFormat.format(expireDate)
        } catch (e: Exception) {
            ""
        }
    }

    private fun FragmentMyRewardDetailBinding.setUpPhysicalGiftView(giftInfoItem: GiftInfoItem) {

    }

    private fun FragmentMyRewardDetailBinding.setUpTopUpView(giftInfoItem: GiftInfoItem) {

    }

}