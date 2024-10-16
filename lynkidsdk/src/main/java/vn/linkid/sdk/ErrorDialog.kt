package vn.linkid.sdk

import android.app.ActionBar
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import vn.linkid.sdk.databinding.DialogErrorBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight

class ErrorDialog : DialogFragment() {


    companion object {
        private const val ARG_TEXT1 = "title"
        private const val ARG_TEXT2 = "description"
        fun newInstance(title: String, description: String): ErrorDialog {
            val fragment = ErrorDialog()
            val args = Bundle()
            args.putString(ARG_TEXT1, title)
            args.putString(ARG_TEXT2, description)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: DialogErrorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT
        );
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.decorView?.setPadding(
            requireContext().dpToPx(16),
            0,
            requireContext().dpToPx(16),
            0
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnOK.setOnClickListener {
                dismiss()
            }
            val title = arguments?.getString(ARG_TEXT1) ?: ""
            val description = arguments?.getString(ARG_TEXT2) ?: ""
            if (title.isNotEmpty()) {
                txtTitle.text = title
            }
            if (description.isNotEmpty()) {
                txtDescription.text = description
            }

        }
    }

}