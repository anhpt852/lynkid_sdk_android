package vn.linkid.sdk.auth.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.auth.repository.SwitchAccountRepository
import vn.linkid.sdk.auth.service.SwitchAccountService
import vn.linkid.sdk.auth.viewmodel.SwitchAccountViewModel
import vn.linkid.sdk.auth.viewmodel.SwitchAccountViewModelFactory
import vn.linkid.sdk.databinding.FragmentSwitchAccountBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class SwitchAccountFragment : Fragment() {

    private lateinit var viewModel: SwitchAccountViewModel
    private val service = SwitchAccountService(mainAPI)
    private val repository = SwitchAccountRepository(service)
    private val viewModelFactory = SwitchAccountViewModelFactory(repository)
    private lateinit var binding: FragmentSwitchAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwitchAccountBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[SwitchAccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = btnExit.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            btnExit.layoutParams = layoutParams

            val bottomLayoutParam = layoutAuth.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            layoutAuth.layoutParams = bottomLayoutParam

            btnExit.setOnClickListener { (activity as LynkiDSDKActivity).exitSDK() }

            txtFirstPhone.text = LynkiD_SDK.connectedPhoneNumber
            txtSecondPhone.text = LynkiD_SDK.phoneNumber

            viewModel.currentOption.observe(viewLifecycleOwner) {
                layoutCheckFirstProfile.visibility = if (it == 0) View.VISIBLE else View.GONE
                txtFirstPhone.setTextColor(Color.parseColor(if (it == 0) "#663692" else "#242424"))
                layoutCheckSecondProfile.visibility = if (it == 1) View.VISIBLE else View.GONE
                txtSecondPhone.setTextColor(Color.parseColor(if (it == 1) "#663692" else "#242424"))
            }

            layoutFirstProfile.setOnClickListener { viewModel.currentOption.value = 0 }
            layoutSecondProfile.setOnClickListener { viewModel.currentOption.value = 1 }

            txtSkip.setOnClickListener {
                LynkiD_SDK.isAnonymous = true
                val action = SwitchAccountFragmentDirections.actionSwitchAccountFragmentToHomeFragment()
                findNavController().navigate(action)
            }

            btnLogin.setOnClickListener {
                if (viewModel.currentOption.value == 0) {
                    viewModel.switchMember().observe(viewLifecycleOwner) {
                        val action =
                            SwitchAccountFragmentDirections.actionSwitchAccountFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                } else {
                    viewModel.createMember().observe(viewLifecycleOwner) {
                        val action =
                            SwitchAccountFragmentDirections.actionSwitchAccountFragmentToHomeFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}