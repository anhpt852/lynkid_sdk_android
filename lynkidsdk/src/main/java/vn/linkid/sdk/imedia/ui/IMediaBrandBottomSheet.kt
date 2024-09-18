package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.linkid.sdk.databinding.DialogImediaBrandBinding
import vn.linkid.sdk.imedia.adapter.DialogIMediaBrandAdapter
import vn.linkid.sdk.models.imedia.GetThirdPartyBrandByVendor

class IMediaBrandBottomSheet(
    private val selectedBrand: GetThirdPartyBrandByVendor,
    private val brandList: List<GetThirdPartyBrandByVendor>,
) :
    BottomSheetDialogFragment() {
    var onApplyBrand: ((GetThirdPartyBrandByVendor) -> Unit)? = null

    private lateinit var binding: DialogImediaBrandBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogImediaBrandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            btnExit.setOnClickListener {
                dismiss()
            }
            val adapter = DialogIMediaBrandAdapter(selectedBrand, brandList)
            adapter.onItemClick = {
                if (it != null) {
                    onApplyBrand?.invoke(it)
                    dismiss()
                }
            }
            listBrand.layoutManager = LinearLayoutManager(context)
            listBrand.adapter = adapter
        }
    }


}