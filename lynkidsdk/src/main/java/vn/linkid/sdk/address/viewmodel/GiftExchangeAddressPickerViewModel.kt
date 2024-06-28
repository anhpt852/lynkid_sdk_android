package vn.linkid.sdk.address.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import vn.linkid.sdk.models.address.Address

class GiftExchangeAddressPickerViewModel: ViewModel() {
    val selectedCity = MutableLiveData<Address?>()
    val selectedDistrict = MutableLiveData<Address?>()
    val selectedWard = MutableLiveData<Address?>()

    fun getSelectedCity(): LiveData<Address?> = selectedCity
    fun getSelectedDistrict(): LiveData<Address?> = selectedDistrict
    fun getSelectedWard(): LiveData<Address?> = selectedWard

    fun setSelectedCity(address: Address) {
        selectedCity.value = address
    }
    fun setSelectedDistrict(address: Address) {
        selectedDistrict.value = address
    }
    fun setSelectedWard(address: Address) {
        selectedWard.value = address
    }

}