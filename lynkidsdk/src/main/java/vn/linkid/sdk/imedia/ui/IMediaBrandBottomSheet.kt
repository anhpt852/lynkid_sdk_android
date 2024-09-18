package vn.linkid.sdk.imedia.ui

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor

class IMediaBrandBottomSheet(private val selectedBrand: GetThirdPartyBrandByVendor, private val brandList: List<GetThirdPartyBrandByVendor>) :
    BottomSheetDialogFragment()  {
    var onApplyBrand: ((GetThirdPartyBrandByVendor) -> Unit)? = null


}