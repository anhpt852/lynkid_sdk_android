package vn.linkid.sdk.auth.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import vn.linkid.sdk.auth.repository.AuthRepository
import vn.linkid.sdk.auth.service.AuthService
import vn.linkid.sdk.auth.viewmodel.AuthViewModel
import vn.linkid.sdk.auth.viewmodel.AuthViewModelFactory
import vn.linkid.sdk.databinding.FragmentAuthBinding
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.models.auth.ConnectedMember
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight


class AuthFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private val service = AuthService(mainAPI)
    private val repository = AuthRepository(service)
    private val viewModelFactory = AuthViewModelFactory(repository)
    private lateinit var binding: FragmentAuthBinding

    private val args: AuthFragmentArgs by navArgs()
    private val connectedMember: ConnectedMember by lazy { args.connectedMember }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
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

            txtName.text = connectedMember.basicInfo?.name
            txtPhone.text = connectedMember.phoneNumber

            btnDecline.setOnClickListener {
                LynkiD_SDK.isAnonymous = true
                val action = AuthFragmentDirections.actionAuthFragmentToHomeFragment()
                findNavController().navigate(action)
            }

            btnAllow.setOnClickListener {
                LynkiD_SDK.isAnonymous = false
                val isAccountExist = connectedMember.isExisting ?: false
                val isAccountConnected = connectedMember.connectionInfo?.isExisting ?: false
                val isDifferentPhone =
                    connectedMember.phoneNumber != connectedMember.connectionInfo?.connectedToPhone
                Log.d(
                    "AuthFragment",
                    "isAccountExist: $isAccountExist, isAccountConnected: $isAccountConnected, isDifferentPhone: $isDifferentPhone"
                )
                val action =
                    if (!isAccountExist && !isAccountConnected) AuthFragmentDirections.actionAuthFragmentToRegisterFragment(
                        connectedMember
                    )
                    else if (!isAccountExist) {
                        if (isDifferentPhone) AuthFragmentDirections.actionAuthFragmentToSwitchAccountFragment(
                            connectedMember
                        )
                        else AuthFragmentDirections.actionAuthFragmentToLoginWithoutConnectFragment(
                            connectedMember
                        )
                    } else if (!isAccountConnected) AuthFragmentDirections.actionAuthFragmentToLoginWithoutConnectFragment(
                        connectedMember
                    ) else AuthFragmentDirections.actionAuthFragmentToLoginFragment(connectedMember)
                findNavController().navigate(action)
            }
        }
    }
}