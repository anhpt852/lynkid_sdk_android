package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.R
import vn.linkid.sdk.address.viewmodel.GiftExchangeAddressPickerViewModel
import vn.linkid.sdk.address.viewmodel.GiftExchangeAddressPickerViewModelFactory
import vn.linkid.sdk.databinding.FragmentGiftExchangeAddressBinding
import vn.linkid.sdk.models.gift.GiftReceiver
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight

class GiftExchangeAddressFragment : Fragment() {

    private lateinit var binding: FragmentGiftExchangeAddressBinding
    private val pickerViewModel: GiftExchangeAddressPickerViewModel by activityViewModels { GiftExchangeAddressPickerViewModelFactory() }


    private val args: GiftExchangeAddressFragmentArgs by navArgs()
    private val giftId: Int by lazy { args.giftId }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGiftExchangeAddressBinding.inflate(inflater, container, false)
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
            val bottomLayoutParam = btnExchange.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            btnExchange.layoutParams = bottomLayoutParam

            btnBack.setOnClickListener { findNavController().popBackStack() }

            edtName.setText(LynkiD_SDK.name)
            edtPhone.setText(LynkiD_SDK.phoneNumber.replace("+84", "0"))

            pickerViewModel.selectedCity.observe(viewLifecycleOwner) {
                txtCityTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                txtCity.visibility = View.VISIBLE
                txtCity.text = it?.name
                if (it?.name != null) {
                    layoutDistrict.setBackgroundResource(R.drawable.bg_outlined_button)
                }
            }
            layoutCity.setOnClickListener {
                val action =
                    GiftExchangeAddressFragmentDirections.actionGiftExchangeAddressFragmentToAddressPickerFragment(
                        "",
                        "City"
                    )
                findNavController().navigate(action)
            }

            pickerViewModel.selectedDistrict.observe(viewLifecycleOwner) {
                txtDistrictTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                txtDistrict.visibility = View.VISIBLE
                txtDistrict.text = it?.name
                if (it?.name != null) {
                    layoutWard.setBackgroundResource(R.drawable.bg_outlined_button)
                }
            }
            layoutDistrict.setOnClickListener {
                if (pickerViewModel.getSelectedCity().value != null) {
                    val action =
                        GiftExchangeAddressFragmentDirections.actionGiftExchangeAddressFragmentToAddressPickerFragment(
                            pickerViewModel.getSelectedCity().value?.code ?: "",
                            "District"
                        )
                    findNavController().navigate(action)
                }
            }

            pickerViewModel.selectedWard.observe(viewLifecycleOwner) {
                txtWardTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
                txtWard.visibility = View.VISIBLE
                txtWard.text = it?.name
            }
            layoutWard.setOnClickListener {
                if (pickerViewModel.getSelectedDistrict().value != null) {
                    val action =
                        GiftExchangeAddressFragmentDirections.actionGiftExchangeAddressFragmentToAddressPickerFragment(
                            pickerViewModel.getSelectedDistrict().value?.code ?: "",
                            "Ward"
                        )
                    findNavController().navigate(action)
                }
            }

            btnExchange.setOnClickListener {
                val giftReceiver = GiftReceiver(
                    edtName.text.toString(),
                    edtPhone.text.toString(),
                    pickerViewModel.getSelectedCity().value?.code,
                    pickerViewModel.getSelectedCity().value?.name,
                    pickerViewModel.getSelectedDistrict().value?.code,
                    pickerViewModel.getSelectedDistrict().value?.name,
                    pickerViewModel.getSelectedWard().value?.code,
                    pickerViewModel.getSelectedWard().value?.name,
                    edtAddress.text.toString(),
                    edtNote.text.toString()
                )
                val action =
                    GiftExchangeAddressFragmentDirections.actionGiftExchangeAddressFragmentToGiftExchangeFragment(
                        giftId,
                        giftReceiver
                    )
                findNavController().navigate(action)
            }
        }
    }

}