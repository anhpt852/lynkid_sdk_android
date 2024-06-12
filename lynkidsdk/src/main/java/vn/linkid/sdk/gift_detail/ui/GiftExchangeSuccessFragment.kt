package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.FragmentGiftExchangeSuccessBinding
import vn.linkid.sdk.utils.formatPrice

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
            btnDetail.setOnClickListener {
                val action = GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToMyRewardDetailFragment(transactionCode)
                findNavController().navigate(action)
            }
        }
    }
}