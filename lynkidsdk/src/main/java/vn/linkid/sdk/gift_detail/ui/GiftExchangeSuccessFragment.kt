package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentGiftExchangeSuccessBinding
import vn.linkid.sdk.models.gift.GiftExchange
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight

class GiftExchangeSuccessFragment : Fragment() {

    private lateinit var binding: FragmentGiftExchangeSuccessBinding

    private val args: GiftExchangeSuccessFragmentArgs by navArgs()
    private val giftExchange: GiftExchange by lazy { args.giftExchange }
    private val from: String? by lazy { args.from }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
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
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            btnUse.layoutParams = bottomLayoutParam

            val bottomHomeLayoutParam = btnBottomHome.layoutParams as ViewGroup.MarginLayoutParams
            bottomHomeLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            btnBottomHome.layoutParams = bottomHomeLayoutParam

            giftExchange.apply {
                btnHome.setOnClickListener {
                    if(from == "TopUp") {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    } else {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }
                }
                txtExchangeInfo.text =
                    "Bạn vừa tiết kiệm ${totalAmount.formatPrice()}VND cùng LynkiD"
                txtExchangeInfo.visibility = if (from == "TopUp") View.GONE else View.VISIBLE

                txtBrand.text = brandName
                Glide.with(root.context)
                    .load(brandImage)
                    .centerCrop()
                    .into(imgBrand)
                txtGiftName.text = giftName
                txtExpireDate.text =
                    if (!expiredString.isNullOrEmpty()) "HSD: $expiredString" else ""
                if ((amount ?: 1) > 1) {
                    tagAmount.visibility = View.VISIBLE
                    txtAmount.text = "X${(amount ?: 1).formatPrice()}"
                } else {
                    tagAmount.visibility = View.GONE
                }
                layoutGift.setOnClickListener {
                    val action =
                        if (isEGift == true) GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToMyRewardEGiftDetailFragment(
                            transactionCode ?: ""
                        ) else GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToMyRewardPhysicalDetailFragment(
                            transactionCode ?: ""
                        )
                    findNavController().navigate(action)
                }
                btnDetail.setOnClickListener {
                    val action =
                        GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToTransactionDetailFragment(
                            transactionCode ?: "",
                            false
                        )
                    findNavController().navigate(action)
                }

                btnUse.setOnClickListener {
                    val action =
                        if (isEGift == true) GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToMyRewardEGiftDetailFragment(
                            transactionCode ?: ""
                        ) else GiftExchangeSuccessFragmentDirections.actionGiftExchangeSuccessFragmentToMyRewardPhysicalDetailFragment(
                            transactionCode ?: ""
                        )
                    findNavController().navigate(action)
                }

                btnReExchange.setOnClickListener {
                    findNavController().popBackStack()
                    findNavController().popBackStack()
                }

                btnBottomHome.setOnClickListener {
                    if(from == "TopUp") {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    } else {
                        findNavController().popBackStack()
                        findNavController().popBackStack()
                    }
                }


                if (from == "TopUp") {
                    btnUse.visibility = View.GONE
                    btnBottomHome.visibility = View.VISIBLE
                    btnReExchange.visibility = View.VISIBLE
                } else {
                    btnUse.visibility = View.VISIBLE
                    btnBottomHome.visibility = View.GONE
                    btnReExchange.visibility = View.GONE
                }
            }
        }
    }
}