package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.databinding.FragmentGiftExchangeBinding
import vn.linkid.sdk.dpToPx
import vn.linkid.sdk.getStatusBarHeight
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.gift_detail.viewmodel.GiftExchangeViewModel
import vn.linkid.sdk.gift_detail.viewmodel.GiftExchangeViewModelFactory
import vn.linkid.sdk.mainAPI
import vn.linkid.sdk.models.gift.GiftDetail

class GiftExchangeFragment: Fragment() {

    private lateinit var binding: FragmentGiftExchangeBinding
    private lateinit var viewModel: GiftExchangeViewModel
    private val service = GiftDetailService(mainAPI)
    private val repository = GiftDetailRepository(service)
    private val viewModelFactory = GiftExchangeViewModelFactory(repository)

    private val args: GiftExchangeFragmentArgs by navArgs()
    private val giftId: Int by lazy { args.giftId }

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
        }
    }

    private fun FragmentGiftExchangeBinding.setUpGiftInfo(giftDetail: GiftDetail) {

    }

    private fun FragmentGiftExchangeBinding.setUpQuantity(giftDetail: GiftDetail) {
        viewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            txtQuantity.text = quantity.toString()
            btnSubtractQuantities.isEnabled = quantity > 1
            btnAddQuantities.isEnabled = quantity < 1
        }
        btnSubtractQuantities.setOnClickListener { viewModel.decreaseQuantity() }
        btnAddQuantities.setOnClickListener { viewModel.increaseQuantity() }
    }

    private fun FragmentGiftExchangeBinding.setUpBalance(giftDetail: GiftDetail) {

    }



}