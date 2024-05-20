package vn.linkid.sdk.category.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.linkid.sdk.category.viewmodel.CategoryFilterViewModel
import vn.linkid.sdk.category.viewmodel.CategoryFilterViewModelFactory
import vn.linkid.sdk.databinding.DialogCategoryListFilterBinding
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.models.category.GiftType
import vn.linkid.sdk.models.category.OptionModel

class CategoryFilterBottomSheet(private val inputFilter: GiftFilterModel) :
    BottomSheetDialogFragment() {
    var onApplyFilter: ((GiftFilterModel) -> Unit)? = null

    private lateinit var binding: DialogCategoryListFilterBinding
    private lateinit var viewModel: CategoryFilterViewModel
    private val viewModelFactory = CategoryFilterViewModelFactory()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCategoryListFilterBinding.inflate(inflater, container, false)
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
            btnClear.setOnClickListener {
                onApplyFilter?.invoke(GiftFilterModel())
                dismiss()
            }
            layoutFirstPrice.setOnClickListener {
                layoutCheckFirstPrice.visibility = View.VISIBLE
                layoutCheckSecondPrice.visibility = View.GONE
                layoutCheckThirdPrice.visibility = View.GONE
                layoutCheckFourthPrice.visibility = View.GONE
                editFrom.setText("0")
                editTo.setText("100000")
            }
            layoutSecondPrice.setOnClickListener {
                layoutCheckFirstPrice.visibility = View.GONE
                layoutCheckSecondPrice.visibility = View.VISIBLE
                layoutCheckThirdPrice.visibility = View.GONE
                layoutCheckFourthPrice.visibility = View.GONE
                editFrom.setText("100000")
                editTo.setText("200000")
            }
            layoutThirdPrice.setOnClickListener {
                layoutCheckFirstPrice.visibility = View.GONE
                layoutCheckSecondPrice.visibility = View.GONE
                layoutCheckThirdPrice.visibility = View.VISIBLE
                layoutCheckFourthPrice.visibility = View.GONE
                editFrom.setText("200000")
                editTo.setText("300000")
            }
            layoutFourthPrice.setOnClickListener {
                layoutCheckFirstPrice.visibility = View.GONE
                layoutCheckSecondPrice.visibility = View.GONE
                layoutCheckThirdPrice.visibility = View.GONE
                layoutCheckFourthPrice.visibility = View.VISIBLE
                editFrom.setText("400000")
                editTo.setText("500000")
            }
            layoutFirstType.setOnClickListener {
                if (viewModel.giftType.value == 0) {
                    layoutCheckFirstType.visibility = View.GONE
                    viewModel.setGiftType(null)
                } else {
                    layoutCheckFirstType.visibility = View.VISIBLE
                    layoutCheckSecondType.visibility = View.GONE
                    viewModel.setGiftType(0)
                }
            }
            layoutSecondType.setOnClickListener {
                if (viewModel.giftType.value == 1) {
                    layoutCheckSecondType.visibility = View.GONE
                    viewModel.setGiftType(null)
                } else {
                    layoutCheckFirstType.visibility = View.GONE
                    layoutCheckSecondType.visibility = View.VISIBLE
                    viewModel.setGiftType(1)
                }
            }
            layoutFirstAddress.setOnClickListener {
                if (viewModel.isFirstLocation.value == true) {
                    layoutCheckFirstAddress.visibility = View.GONE
                    viewModel.setIsFirstLocation(false)
                } else {
                    layoutCheckFirstAddress.visibility = View.VISIBLE
                    viewModel.setIsFirstLocation(true)
                }
            }
            layoutSecondAddress.setOnClickListener {
                if (viewModel.isSecondLocation.value == true) {
                    layoutCheckSecondAddress.visibility = View.GONE
                    viewModel.setIsSecondLocation(false)
                } else {
                    layoutCheckSecondAddress.visibility = View.VISIBLE
                    viewModel.setIsSecondLocation(true)
                }
            }
            setUpInputFilter()
        }
    }

    private fun DialogCategoryListFilterBinding.setUpInputFilter() {
        editFrom.setText(inputFilter.fromCoin)
        editTo.setText(inputFilter.toCoin)
        when (inputFilter.giftType) {
            GiftType.EGIFT -> {
                layoutCheckFirstType.visibility = View.VISIBLE
                layoutCheckSecondType.visibility = View.GONE
            }

            GiftType.PHYSICAL_GIFT -> {
                layoutCheckFirstType.visibility = View.GONE
                layoutCheckSecondType.visibility = View.VISIBLE
            }

            else -> {
                layoutCheckFirstType.visibility = View.GONE
                layoutCheckSecondType.visibility = View.GONE
            }
        }
        if (inputFilter.selectedCities.any { it.optionId == "0" }) {
            layoutCheckFirstAddress.visibility = View.VISIBLE
        } else {
            layoutCheckFirstAddress.visibility = View.GONE
        }
        if (inputFilter.selectedCities.any { it.optionId == "1" }) {
            layoutCheckSecondAddress.visibility = View.VISIBLE
        } else {
            layoutCheckSecondAddress.visibility = View.GONE
        }
    }

    private fun saveFilter(): GiftFilterModel = GiftFilterModel(
        fromCoin = binding.editFrom.text.toString(),
        toCoin = binding.editTo.text.toString(),
        giftType = when {
            binding.layoutCheckFirstType.visibility == View.VISIBLE -> GiftType.EGIFT
            binding.layoutCheckSecondType.visibility == View.VISIBLE -> GiftType.PHYSICAL_GIFT
            else -> GiftType.NONE
        },
        selectedCities = mutableListOf<OptionModel>().apply {
            if (binding.layoutCheckFirstAddress.visibility == View.VISIBLE) {
                add(OptionModel("0", ""))
            }
            if (binding.layoutCheckSecondAddress.visibility == View.VISIBLE) {
                add(OptionModel("1", ""))
            }
        }
    )


}