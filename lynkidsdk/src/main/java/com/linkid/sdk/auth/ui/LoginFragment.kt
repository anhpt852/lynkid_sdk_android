package com.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.linkid.sdk.auth.repository.LoginRepository
import com.linkid.sdk.auth.service.LoginService
import com.linkid.sdk.auth.viewmodel.LoginViewModel
import com.linkid.sdk.auth.viewmodel.LoginViewModelFactory
import com.linkid.sdk.databinding.FragmentLoginBinding
import com.linkid.sdk.mainAPI
import androidx.navigation.fragment.navArgs
import com.linkid.sdk.models.auth.ConnectedMember

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
            btnLogin.text =
                if (connectedMember.connectionInfo?.isExisting == true) "Xác nhận" else "Tiếp tục"

            btnLogin.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

}