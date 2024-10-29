package vn.linkid.sdk.my_reward.ui

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vn.linkid.sdk.ErrorDialog
import vn.linkid.sdk.databinding.DialogMyRewardTopupCopyBinding
import vn.linkid.sdk.utils.copyToClipboard
import vn.linkid.sdk.utils.dpToPx

class MyRewardTopupCopyDialog : BottomSheetDialogFragment() {


    companion object {
        private const val ARG_TEXT1 = "cardNumber"
        private const val ARG_TEXT2 = "serialNumber"
        fun newInstance(cardNumber: String, serialNumber: String): MyRewardTopupCopyDialog {
            val fragment = MyRewardTopupCopyDialog()
            val args = Bundle()
            args.putString(ARG_TEXT1, cardNumber)
            args.putString(ARG_TEXT2, serialNumber)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: DialogMyRewardTopupCopyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogMyRewardTopupCopyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnExit.setOnClickListener {
                dismiss()
            }
            val cardNumber = arguments?.getString(ARG_TEXT1) ?: ""
            val serialNumber = arguments?.getString(ARG_TEXT2) ?: ""
            if (cardNumber.isNotEmpty()) {
                txtCardNumber.text = cardNumber
            }
            if (serialNumber.isNotEmpty()) {
                txtSerialNumber.text = serialNumber
            }
            txtCardNumberTitle.setOnClickListener {
                copyToClipboard(
                    requireContext(),
                    cardNumber,
                    "cardNumber",
                    "Đã sao chép mã thẻ"
                )
                dismiss()
            }
            txtSerialNumberTitle.setOnClickListener {
                copyToClipboard(
                    requireContext(),
                    serialNumber,
                    "serialNumber",
                    "Đã sao chép mã seri"
                )
                dismiss()
            }
            txtCardNumber.setOnClickListener {
                copyToClipboard(
                    requireContext(),
                    cardNumber,
                    "cardNumber",
                    "Đã sao chép mã thẻ"
                )
                dismiss()
            }
            txtSerialNumber.setOnClickListener {
                copyToClipboard(
                    requireContext(),
                    serialNumber,
                    "serialNumber",
                    "Đã sao chép mã seri"
                )
                dismiss()
            }


        }
    }

}