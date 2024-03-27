package com.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.linkid.sdk.auth.repository.AuthRepository
import com.linkid.sdk.auth.service.AuthService
import com.linkid.sdk.auth.viewmodel.AuthViewModel
import com.linkid.sdk.auth.viewmodel.AuthViewModelFactory
import com.linkid.sdk.databinding.FragmentAuthBinding
import com.linkid.sdk.mainAPI
import com.linkid.sdk.models.auth.ConnectedMember
import androidx.navigation.fragment.navArgs


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
            btnAllow.setOnClickListener {
                val isAccountExist = connectedMember.isExisting ?: false
                val isAccountConnected = connectedMember.connectionInfo?.isExisting ?: false
                val action =
                    if (isAccountExist) AuthFragmentDirections.actionAuthFragmentToLoginFragment(
                        connectedMember
                    )
                    else if (isAccountConnected) AuthFragmentDirections.actionAuthFragmentToSwitchAccountFragment(
                        connectedMember
                    )
                    else AuthFragmentDirections.actionAuthFragmentToRegisterFragment(connectedMember)
                findNavController().navigate(action)
            }
        }
    }
}