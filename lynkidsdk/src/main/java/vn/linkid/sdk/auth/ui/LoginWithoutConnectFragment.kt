package vn.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.auth.repository.LoginRepository
import vn.linkid.sdk.auth.service.LoginService
import vn.linkid.sdk.auth.viewmodel.LoginViewModel
import vn.linkid.sdk.auth.viewmodel.LoginViewModelFactory
import vn.linkid.sdk.databinding.FragmentLoginWithoutConnectBinding
import vn.linkid.sdk.models.auth.ConnectedMember
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class LoginWithoutConnectFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private val service = LoginService(mainAPI)
    private val repository = LoginRepository(service)
    private val viewModelFactory = LoginViewModelFactory(repository)
    private lateinit var binding: FragmentLoginWithoutConnectBinding

    private val args: LoginWithoutConnectFragmentArgs by navArgs()
    private val connectedMember: ConnectedMember by lazy { args.connectedMember }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginWithoutConnectBinding.inflate(inflater, container, false)
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

            txtName.text = connectedMember.basicInfo?.name
            txtPhone.text = connectedMember.phoneNumber

            btnLogin.setOnClickListener {
                val action = LoginWithoutConnectFragmentDirections.actionLoginWithoutConnectFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

}