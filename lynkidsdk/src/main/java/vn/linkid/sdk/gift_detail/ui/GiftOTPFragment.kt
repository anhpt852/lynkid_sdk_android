package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.databinding.FragmentGiftOtpBinding
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.gift_detail.viewmodel.GiftOTPViewModel
import vn.linkid.sdk.gift_detail.viewmodel.GiftOTPViewModelFactory
import vn.linkid.sdk.utils.formatDate
import vn.linkid.sdk.utils.mainAPI

class GiftOTPFragment : Fragment() {

    private lateinit var binding: FragmentGiftOtpBinding
    private lateinit var viewModel: GiftOTPViewModel
    private val service = GiftDetailService(mainAPI)
    private val repository = GiftDetailRepository(service)
    private val viewModelFactory = GiftOTPViewModelFactory(repository)

    private val args: GiftOTPFragmentArgs by navArgs()
    private val sessionId: String by lazy { args.sessionId }

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
    }

    private fun setUpView() {
        viewModel.sessionId.value = sessionId
        binding.apply {
            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) {
                    // Show loading
                } else {
                    // Hide loading
                }
            }

            btnRetry.setOnClickListener {
                // Retry
            }

            btnVerify.setOnClickListener {
                viewModel.confirmTransaction(edtPin.text.toString())
                    .observe(viewLifecycleOwner) { result ->
                        result.getOrNull()?.let { exchangeModel ->
                            val eGift = exchangeModel.items?.firstOrNull()?.eGift
                            val expiredString =
                                formatDate(eGift?.expiredDate ?: "")
                            val action =
                                GiftOTPFragmentDirections.actionGiftOTPFragmentToGiftExchangeSuccessFragment(
                                    amount = exchangeModel.totalCount ?: 1,
                                    coin = 0,
                                    brandImage = "",
                                    brandName = "",
                                    giftName = "",
                                    expiredString = expiredString,
                                    transactionCode = exchangeModel.items?.firstOrNull()?.code ?: ""
                                )
                            findNavController().navigate(action)
                        }
                    }
            }
        }
    }

}