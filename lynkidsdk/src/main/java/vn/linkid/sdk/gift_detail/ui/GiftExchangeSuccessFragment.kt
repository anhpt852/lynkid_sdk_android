package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.linkid.sdk.databinding.FragmentGiftExchangeSuccessBinding

class GiftExchangeSuccessFragment: Fragment() {

    private lateinit var binding: FragmentGiftExchangeSuccessBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftExchangeSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }
}