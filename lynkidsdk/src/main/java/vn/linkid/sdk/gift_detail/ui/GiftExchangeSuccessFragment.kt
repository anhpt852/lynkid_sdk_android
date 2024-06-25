package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.FragmentGiftExchangeSuccessBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight

class GiftExchangeSuccessFragment : Fragment() {

    private lateinit var binding: FragmentGiftExchangeSuccessBinding

    private val args: GiftExchangeSuccessFragmentArgs by navArgs()
    private val amount: Int by lazy { args.amount }
    private val coin: Long by lazy { args.coin }
    private val brandImage: String by lazy { args.brandImage }
    private val brandName: String by lazy { args.brandName }
    private val giftName: String by lazy { args.giftName }
    private val expiredString: String by lazy { args.expiredString }
    private val transactionCode: String by lazy { args.transactionCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftExchangeSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams

            val bottomLayoutParam = btnUse.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin = getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            btnUse.layoutParams = bottomLayoutParam


            btnHome.setOnClickListener { findNavController().popBackStack() }
            txtExchangeInfo.text = "Bạn vừa tiết kiệm ${coin.formatPrice()}VND cùng LynkiD"
            txtBrand.text = brandName
            Glide.with(root.context)
                .load(brandImage)
                .into(imgBrand)
            txtGiftName.text = giftName
            txtExpireDate.text = if (expiredString.isNotEmpty()) "HSD: $expiredString" else ""
            if (amount > 1) {
                tagAmount.visibility = View.VISIBLE
                txtAmount.text = "X${amount.formatPrice()}"
            } else {
                tagAmount.visibility = View.GONE
            }
            layoutGift.setOnClickListener {
                val action = GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToMyRewardDetailFragment(transactionCode)
                findNavController().navigate(action)
            }
            btnDetail.setOnClickListener {
                val action = GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToTransactionDetailFragment(transactionCode, false)
                findNavController().navigate(action)
            }
        }
    }
}