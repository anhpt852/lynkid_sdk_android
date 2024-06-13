package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.linkid.sdk.databinding.FragmentGiftExchangeAddressBinding

class GiftExchangeAddressFragment : Fragment() {

    private lateinit var binding: FragmentGiftExchangeAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftExchangeAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

}