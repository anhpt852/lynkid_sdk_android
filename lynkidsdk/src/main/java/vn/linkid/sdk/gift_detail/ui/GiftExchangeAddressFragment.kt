package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import vn.linkid.sdk.address.viewmodel.GiftExchangeAddressPickerViewModel
import vn.linkid.sdk.address.viewmodel.GiftExchangeAddressPickerViewModelFactory
import vn.linkid.sdk.databinding.FragmentGiftExchangeAddressBinding

class GiftExchangeAddressFragment : Fragment() {

    private lateinit var binding: FragmentGiftExchangeAddressBinding
    private lateinit var pickerViewModel: GiftExchangeAddressPickerViewModel
    private val pickerViewModelFactory = GiftExchangeAddressPickerViewModelFactory()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftExchangeAddressBinding.inflate(inflater, container, false)
        pickerViewModel = ViewModelProvider(this, pickerViewModelFactory)[GiftExchangeAddressPickerViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView(){
        binding.apply {
            edtCity.setOnClickListener {
                val action = GiftExchangeAddressFragmentDirections.actionGiftExchangeAddressFragmentToAddressPickerFragment("City", "0")
                findNavController().navigate(action)
            }
        }
    }

}