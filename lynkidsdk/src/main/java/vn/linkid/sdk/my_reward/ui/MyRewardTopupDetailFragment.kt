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
import com.google.gson.Gson
import com.google.gson.JsonObject
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentMyRewardTopupDetailBinding
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

class MyRewardTopupDetailFragment : Fragment() {

    private lateinit var binding: FragmentMyRewardTopupDetailBinding
    private lateinit var viewModel: MyRewardDetailViewModel
    private val service = MyRewardDetailService(mainAPI)
    private val repository = MyRewardDetailRepository(service)
    private val viewModelFactory = MyRewardDetailViewModelFactory(repository)

    private val args: MyRewardTopupDetailFragmentArgs by navArgs()
    private val transactionCode: String by lazy { args.transactionCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyRewardTopupDetailBinding.inflate(inflater, container, false)
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
            setUpTopUpView(giftInfoItem)
        }
    }

    private fun FragmentMyRewardTopupDetailBinding.setUpCommonView(giftInfoItem: GiftInfoItem) {
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

    private fun FragmentMyRewardTopupDetailBinding.setUpLocationList(giftInfoItem: GiftInfoItem) {
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

    private fun FragmentMyRewardTopupDetailBinding.setUpTopUpView(giftInfoItem: GiftInfoItem) {


        val description = giftInfoItem.giftTransaction?.description ?: ""
        if (description.isNotEmpty()) {
            try {
                val jsonObject = Gson().fromJson(description, JsonObject::class.java)
                val operation = jsonObject?.get("operation")?.asInt
                val ownerPhone = jsonObject?.get("ownerPhone")?.asString ?: ""
                val accountType = jsonObject?.get("accountType")?.asString ?: ""
                val isTopup = operation == 1200;
            } catch (e: Exception) {
                // do nothing
            }
        }


        val eGiftCode = giftInfoItem.eGift?.code ?: ""
        if(eGiftCode.isNotEmpty()) {
            txtCardNumber.text = eGiftCode
        } else {
            txtCardNumber.visibility = View.GONE
        }
        val serialNumber = giftInfoItem.giftTransaction?.serialNo ?: ""
        if(serialNumber.isNotEmpty()) {
            txtCardSerial.text = "Số seri: " + serialNumber
        } else {
            txtCardSerial.visibility = View.GONE
        }

    }

}