package vn.linkid.sdk.category.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.linkid.sdk.databinding.DialogCategoryListFilterBinding
import vn.linkid.sdk.models.category.GiftFilterModel

class CategoryFilterBottomSheet(private val inputFilter: GiftFilterModel) : BottomSheetDialogFragment() {
    var onApplyFilter: ((GiftFilterModel) -> Unit)? = null

    private lateinit var binding: DialogCategoryListFilterBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCategoryListFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpView(){
        binding.apply {
            btnExit.setOnClickListener {
                dismiss()
            }
            btnApply.setOnClickListener {
                onApplyFilter?.invoke(inputFilter)
                dismiss()
            }
            btnClear.setOnClickListener {
                onApplyFilter?.invoke(GiftFilterModel())
                dismiss()
            }
        }
    }

}