package vn.linkid.sdk.address.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.linkid.sdk.databinding.FragmentAddressPickerBinding

class AddressPickerFragment: Fragment() {

    private lateinit var binding: FragmentAddressPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

}