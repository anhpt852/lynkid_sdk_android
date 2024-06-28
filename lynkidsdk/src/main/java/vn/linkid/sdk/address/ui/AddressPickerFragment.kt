package vn.linkid.sdk.address.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import vn.linkid.sdk.address.adapter.AddressPickerAdapter
import vn.linkid.sdk.address.repository.AddressPickerRepository
import vn.linkid.sdk.address.service.AddressPickerService
import vn.linkid.sdk.address.viewmodel.AddressPickerViewModel
import vn.linkid.sdk.address.viewmodel.AddressPickerViewModelFactory
import vn.linkid.sdk.address.viewmodel.GiftExchangeAddressPickerViewModel
import vn.linkid.sdk.address.viewmodel.GiftExchangeAddressPickerViewModelFactory
import vn.linkid.sdk.databinding.FragmentAddressPickerBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class AddressPickerFragment : Fragment() {

    private lateinit var binding: FragmentAddressPickerBinding
    private lateinit var viewModel: AddressPickerViewModel
    private lateinit var pickerViewModel: GiftExchangeAddressPickerViewModel
    private val service = AddressPickerService(mainAPI)
    private val repository = AddressPickerRepository(service)
    private val viewModelFactory = AddressPickerViewModelFactory(repository)
    private val pickerViewModelFactory = GiftExchangeAddressPickerViewModelFactory()

    private val args: AddressPickerFragmentArgs by navArgs()
    private val parentCode: String by lazy { args.parentCode }
    private val level: String by lazy { args.level }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressPickerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[AddressPickerViewModel::class.java]
        pickerViewModel = ViewModelProvider(
            this,
            pickerViewModelFactory
        )[GiftExchangeAddressPickerViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
        }
        setUpLoader()
        setUpAddressList()
    }


    private fun setUpLoader() {
        viewModel.uiState.observe(viewLifecycleOwner) { (isLoading, isEmpty) ->
            Log.d("AddressPickerFragment", "setUpLoader: isLoading: $isLoading, isEmpty: $isEmpty")
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isEmpty && !isLoading) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }

    private fun setUpAddressList() {
        viewModel.getAddress(parentCode, level).observe(viewLifecycleOwner) { addressList ->
            Log.d("AddressPickerFragment", "setUpAddressList: addressList: $addressList")
            binding.apply {
                val adapter = AddressPickerAdapter(addressList.getOrNull()?.items ?: emptyList())
                adapter.onItemClick = { address ->
                    Log.d("AddressPickerFragment", "onItemClick: address: $address")
                    when (level) {
                        "City" -> pickerViewModel.setSelectedCity(address)
                        "District" -> pickerViewModel.setSelectedDistrict(address)
                        else -> pickerViewModel.setSelectedWard(address)
                    }
                    findNavController().popBackStack()
                }
                val layoutManager = LinearLayoutManager(requireContext())
                listAddress.layoutManager = layoutManager
                listAddress.adapter = adapter

                edtSearch.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable?) {
                        adapter.filter(s.toString())
                    }
                })
            }
        }
    }

}