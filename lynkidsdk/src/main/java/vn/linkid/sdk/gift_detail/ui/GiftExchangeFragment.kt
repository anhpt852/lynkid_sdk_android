package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import vn.linkid.sdk.databinding.FragmentGiftExchangeBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.gift_detail.viewmodel.GiftExchangeViewModel
import vn.linkid.sdk.gift_detail.viewmodel.GiftExchangeViewModelFactory
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.models.gift.GiftDetail
import vn.linkid.sdk.models.gift.GiftExchange
import vn.linkid.sdk.models.gift.GiftReceiver
import vn.linkid.sdk.utils.formatDate

class GiftExchangeFragment : Fragment() {

    private lateinit var binding: FragmentGiftExchangeBinding
    private lateinit var viewModel: GiftExchangeViewModel
    private val service = GiftDetailService(mainAPI)
    private val repository = GiftDetailRepository(service)
    private val viewModelFactory = GiftExchangeViewModelFactory(repository)

    private val args: GiftExchangeFragmentArgs by navArgs()
    private val giftId: Int by lazy { args.giftId }
    private val giftReceiver: GiftReceiver? by lazy { args.giftReceiver }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftExchangeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[GiftExchangeViewModel::class.java]
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
            setUpQuantity(giftDetail)
            setUpGiftInfo(giftDetail)
            setUpBalance(giftDetail)
            setUpExchangeButton(giftDetail)
            setUpReceiver()
        }
    }

    private fun FragmentGiftExchangeBinding.setUpGiftInfo(giftDetail: GiftDetail) {
        txtGiftName.text = giftDetail.giftInfor?.name ?: ""
        txtPrice.text = (giftDetail.giftInfor?.requiredCoin ?: 0.0).formatPrice()
        Glide.with(root.context).load((giftDetail.imageLink ?: emptyList()).firstOrNull()?.link)
            .into(imgGift)
    }

    private fun FragmentGiftExchangeBinding.setUpQuantity(giftDetail: GiftDetail) {
        val maxAllowedRedemptionOfUser = giftDetail.giftInfor?.maxAllowedRedemptionOfUser ?: 0
        val maxQuantityPerRedemptionOfUser =
            giftDetail.giftInfor?.maxQuantityPerRedemptionOfUser ?: 0
        val totalRedemptionOfUser = giftDetail.giftInfor?.totalRedeemedOfUser ?: 0
        val remainingQuantity = maxAllowedRedemptionOfUser - totalRedemptionOfUser
        viewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            txtQuantity.text = quantity.toString()
            txtRequiredPoint.text =
                ((giftDetail.giftInfor?.requiredCoin ?: 0.0) * quantity).formatPrice()
            btnSubtractQuantities.isEnabled = quantity > 1
            btnAddQuantities.isEnabled =
                if (maxAllowedRedemptionOfUser == 0 && maxQuantityPerRedemptionOfUser == 0) true else quantity < remainingQuantity
            checkButtonAvailability(giftDetail)
        }
        btnSubtractQuantities.setOnClickListener { viewModel.decreaseQuantity() }
        btnAddQuantities.setOnClickListener { viewModel.increaseQuantity() }
    }

    private fun FragmentGiftExchangeBinding.setUpBalance(giftDetail: GiftDetail) {
        viewModel.pointInfoLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("GiftExchangeFragment", "setUpPointInfo: $loader")
            }
        }
        viewModel.pointInfo.observe(viewLifecycleOwner) { pointInfo ->
            if (pointInfo.getOrNull() != null) {
                val point = pointInfo.getOrNull()!!
                txtAvailablePoint.text = point.tokenBalance!!.formatPrice()
                checkButtonAvailability(giftDetail)
            }
        }
    }

    private fun FragmentGiftExchangeBinding.setUpExchangeButton(giftDetail: GiftDetail) {
        val bottomLayoutParam = btnExchange.layoutParams as ViewGroup.MarginLayoutParams
        bottomLayoutParam.bottomMargin = getNavigationBarHeight(root) + (context?.dpToPx(16) ?: 0)
        btnExchange.layoutParams = bottomLayoutParam

        val maxAllowedRedemptionOfUser = giftDetail.giftInfor?.maxAllowedRedemptionOfUser ?: 0
        val maxQuantityPerRedemptionOfUser =
            giftDetail.giftInfor?.maxQuantityPerRedemptionOfUser ?: 0
        val totalRedemptionOfUser = giftDetail.giftInfor?.totalRedeemedOfUser ?: 0
        val remainingQuantity = maxAllowedRedemptionOfUser - totalRedemptionOfUser

        viewModel.transactionLoading.observe(viewLifecycleOwner) { isLoading ->
            layoutLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        btnExchange.setOnClickListener {
            val sessionId = "LynkiD_SDK" + System.currentTimeMillis()
            viewModel.createTransaction(
                sessionId,
                giftDetail.giftInfor?.code ?: "",
                (giftDetail.giftInfor?.requiredCoin ?: 0.0) * (viewModel.quantity.value ?: 1),
                if (giftReceiver != null) parseReceiverInfo(giftReceiver!!) else ""
            ).observe(viewLifecycleOwner) { result ->
                result.getOrNull()?.let { exchangeModel ->
                    val isOtpSent = exchangeModel.isOtpSent ?: false
                    val expiredString =
                        formatDate(exchangeModel.items?.firstOrNull()?.eGift?.expiredDate ?: "")
                    val giftExchange = GiftExchange(
                        sessionId = sessionId,
                        giftCode = giftDetail.giftInfor?.code ?: "",
                        totalAmount = (giftDetail.giftInfor?.requiredCoin
                            ?: 0.0) * (viewModel.quantity.value ?: 1),
                        description = if (giftReceiver != null) parseReceiverInfo(giftReceiver!!) else "",
                        amount = viewModel.quantity.value,
                        brandImage = giftDetail.giftInfor?.brandLinkLogo,
                        brandName = giftDetail.giftInfor?.brandName,
                        giftName = giftDetail.giftInfor?.name,
                        expiredString = expiredString,
                        transactionCode = exchangeModel.items?.firstOrNull()?.code
                    )
                    val action = if (isOtpSent) {
                        GiftExchangeFragmentDirections.actionGiftExchangeFragmentToGiftOTPFragment(
                            giftExchange
                        )
                    } else {
                        GiftExchangeFragmentDirections.actionGiftExchangeFragmentToGiftExchangeSuccessFragment(
                            giftExchange
                        )
                    }
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun FragmentGiftExchangeBinding.checkButtonAvailability(giftDetail: GiftDetail) {
        val quantity = viewModel.quantity.value ?: 1
        val totalCost = (giftDetail.giftInfor?.requiredCoin ?: 0.0) * quantity
        val balance = viewModel.pointInfo.value?.getOrNull()?.tokenBalance ?: 0.0
        btnExchange.isEnabled = balance >= totalCost
        btnExchange.alpha = if (balance >= totalCost) 1f else 0.6f
    }

    private fun FragmentGiftExchangeBinding.setUpReceiver() {
        if (giftReceiver != null) {
            layoutAddress.visibility = View.VISIBLE
            txtName.text = giftReceiver?.name
            txtPhone.text = giftReceiver?.phone
            txtAddress.text =
                "${giftReceiver?.address}, ${giftReceiver?.wardName}, ${giftReceiver?.districtName}, ${giftReceiver?.cityName}"
            txtNote.text = giftReceiver?.note
        }

    }

    private fun parseReceiverInfo(receiver: GiftReceiver): String {
        val gson = Gson()
        val jsonObject = JsonObject()
        Log.d("GiftExchangeFragment", "parseReceiverInfo 1: $receiver")
        receiver.name?.let { jsonObject.addProperty("fullname", it) }
        receiver.phone?.let { jsonObject.addProperty("phone", it) }
        receiver.cityCode?.let { jsonObject.addProperty("cityId", it) }
        receiver.districtCode?.let { jsonObject.addProperty("districtId", it) }
        receiver.wardCode?.let { jsonObject.addProperty("wardId", it.ifEmpty { "0" }) }
        receiver.address?.let { jsonObject.addProperty("shipAddress", it) }
        receiver.note?.let { jsonObject.addProperty("note", it) }
        val result = gson.toJson(jsonObject)
        Log.d("GiftExchangeFragment", "parseReceiverInfo 2: $result")
        return result
    }


}