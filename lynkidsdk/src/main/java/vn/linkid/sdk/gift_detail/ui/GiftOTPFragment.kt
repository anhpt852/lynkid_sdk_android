package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.databinding.FragmentGiftOtpBinding
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.gift_detail.viewmodel.GiftOTPViewModel
import vn.linkid.sdk.gift_detail.viewmodel.GiftOTPViewModelFactory
import vn.linkid.sdk.models.gift.GiftExchange
import vn.linkid.sdk.utils.adjustWhenKeyboardShown
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatDate
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class GiftOTPFragment : Fragment() {

    private lateinit var binding: FragmentGiftOtpBinding
    private lateinit var viewModel: GiftOTPViewModel
    private val service = GiftDetailService(mainAPI)
    private val repository = GiftDetailRepository(service)
    private val viewModelFactory = GiftOTPViewModelFactory(repository)

    private val args: GiftOTPFragmentArgs by navArgs()
    private val giftExchange: GiftExchange by lazy { args.giftExchange }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftOtpBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[GiftOTPViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        view.adjustWhenKeyboardShown(binding.btnVerify)
        view.adjustWhenKeyboardShown(binding.btnRetry)
    }

    private fun setUpView() {
        viewModel.sessionId.value = giftExchange.sessionId
        binding.apply {
            val statusHeight = getStatusBarHeight(root)
            fun updateTopMargin(view: View, additionalDp: Int) {
                val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.topMargin = statusHeight + (context?.dpToPx(additionalDp) ?: 0)
                view.layoutParams = layoutParams
            }
            updateTopMargin(toolbar, 12)
            val bottomLayoutParam = btnVerify.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(16) ?: 0)
            btnVerify.layoutParams = bottomLayoutParam
            btnBack.setOnClickListener { findNavController().popBackStack() }
            txtTitle.text = "Vui lòng nhập mã xác thực (OTP) được gửi về số điện thoại ${LynkiD_SDK.phoneNumber}"
            viewModel.isLoading.observe(viewLifecycleOwner) {
                layoutLoading.visibility = if (it) View.VISIBLE else View.GONE
            }

            btnRetry.setOnClickListener {
                val sessionId = "LynkiD_SDK" + System.currentTimeMillis()
                viewModel.createTransaction(
                    sessionId,
                    giftExchange.giftCode ?: "",
                    giftExchange.amount,
                    giftExchange.totalAmount,
                    giftExchange.description
                ).observe(viewLifecycleOwner) { result ->
                    result.getOrNull()?.let {
                        viewModel.sessionId.value = sessionId
                    }
                }
            }

            btnVerify.setOnClickListener {
                viewModel.confirmTransaction(edtPin.text.toString())
                    .observe(viewLifecycleOwner) { result ->
                        result.getOrNull()?.let { exchangeModel ->
                            val eGift = exchangeModel.items?.firstOrNull()?.eGift
                            val expiredString =
                                formatDate(eGift?.expiredDate ?: "")
                            val newGiftExchange = giftExchange.copy(
                                expiredString = expiredString
                            )
                            val action =
                                GiftOTPFragmentDirections.actionGiftOTPFragmentToGiftExchangeSuccessFragment(
                                    newGiftExchange
                                )
                            findNavController().navigate(action)
                        }
                    }
            }
        }
    }

}