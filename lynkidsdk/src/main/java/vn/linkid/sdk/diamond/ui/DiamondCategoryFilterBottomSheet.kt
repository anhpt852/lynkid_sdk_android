package vn.linkid.sdk.diamond.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.linkid.sdk.category.viewmodel.CategoryFilterViewModel
import vn.linkid.sdk.category.viewmodel.CategoryFilterViewModelFactory
import vn.linkid.sdk.databinding.DialogDiamondCategoryListFilterBinding
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.GiftSorting
import vn.linkid.sdk.models.category.GiftType
import vn.linkid.sdk.models.category.OptionModel

class DiamondCategoryFilterBottomSheet(private val inputFilter: GiftFilterModel) :
    BottomSheetDialogFragment() {
    var onApplyFilter: ((GiftFilterModel) -> Unit)? = null

    private lateinit var binding: DialogDiamondCategoryListFilterBinding
    private lateinit var viewModel: CategoryFilterViewModel
    private val viewModelFactory = CategoryFilterViewModelFactory()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogDiamondCategoryListFilterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoryFilterViewModel::class.java]
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
            btnApply.setOnClickListener {
                onApplyFilter?.invoke(saveFilter())
                dismiss()
            }

        }
    }

    private fun saveFilter(): GiftFilterModel = GiftFilterModel(
        sorting = binding.radioGroupSort.checkedRadioButtonId.let {
            when (it) {
                binding.radioButton1.id -> GiftSorting.POPULAR
                binding.radioButton2.id -> GiftSorting.REQUIRED_COIN_ASC
                else -> GiftSorting.POPULAR
            }
        }
    )


}