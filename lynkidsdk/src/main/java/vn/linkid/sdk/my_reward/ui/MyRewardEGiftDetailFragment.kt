package vn.linkid.sdk.my_reward.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentMyRewardEGiftDetailBinding
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

class MyRewardEGiftDetailFragment : Fragment() {

    private lateinit var binding: FragmentMyRewardEGiftDetailBinding
    private lateinit var viewModel: MyRewardDetailViewModel
    private val service = MyRewardDetailService(mainAPI)
    private val repository = MyRewardDetailRepository(service)
    private val viewModelFactory = MyRewardDetailViewModelFactory(repository)

    private val args: MyRewardEGiftDetailFragmentArgs by navArgs()
    private val transactionCode: String by lazy { args.transactionCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyRewardEGiftDetailBinding.inflate(inflater, container, false)
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
                setUpEGiftView(giftInfoItem)
        }
    }

    private fun FragmentMyRewardEGiftDetailBinding.setUpCommonView(giftInfoItem: GiftInfoItem) {
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

    private fun FragmentMyRewardEGiftDetailBinding.setUpLocationList(giftInfoItem: GiftInfoItem) {
        if (giftInfoItem.giftUsageAddress.isNullOrEmpty()) {
            layoutUsageAddress.visibility = View.GONE
        } else {
            val adapter = MyRewardDetailAddressAdapter(giftInfoItem.giftUsageAddress ?: emptyList())
            adapter.onItemClick = {
                Log.d("TAG", "setUpAddress: $it")
            }
            listAddress.layoutManager = LinearLayoutManager(context)
            listAddress.adapter = adapter
            btnAddressMore.setOnClickListener {
                val action = MyRewardEGiftDetailFragmentDirections.actionMyRewardEGiftDetailFragmentToGiftUsageFragment(giftInfoItem.giftInfor?.code ?: "")
                findNavController().navigate(action)
            }
        }
    }

    private fun FragmentMyRewardEGiftDetailBinding.setUpEGiftView(giftInfoItem: GiftInfoItem) {
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

}