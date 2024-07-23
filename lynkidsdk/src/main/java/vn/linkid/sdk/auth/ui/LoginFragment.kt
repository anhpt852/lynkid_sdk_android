package vn.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import vn.linkid.sdk.auth.repository.LoginRepository
import vn.linkid.sdk.auth.service.LoginService
import vn.linkid.sdk.auth.viewmodel.LoginViewModel
import vn.linkid.sdk.auth.viewmodel.LoginViewModelFactory
import vn.linkid.sdk.databinding.FragmentLoginBinding
import vn.linkid.sdk.utils.mainAPI
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.models.auth.ConnectedMember

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private val service = LoginService(mainAPI)
    private val repository = LoginRepository(service)
    private val viewModelFactory = LoginViewModelFactory(repository)
    private lateinit var binding: FragmentLoginBinding

    private val args: LoginFragmentArgs by navArgs()
    private val connectedMember: ConnectedMember by lazy { args.connectedMember }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
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
            bottomLayoutParam.bottomMargin = getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            layoutAuth.layoutParams = bottomLayoutParam

            txtPhoneVerify.text = "Số điện thoại ${connectedMember.phoneNumber} có phải là số điện thoại của bạn"

            btnExit.setOnClickListener { (activity as LynkiDSDKActivity).exitSDK() }
            txtSkip.setOnClickListener {
                LynkiD_SDK.isAnonymous = true
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }

            btnLogin.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

}