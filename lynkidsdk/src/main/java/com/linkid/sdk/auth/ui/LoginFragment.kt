package com.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.auth.repository.LoginRepository
import com.linkid.sdk.auth.service.LoginService
import com.linkid.sdk.auth.viewmodel.LoginViewModel
import com.linkid.sdk.auth.viewmodel.LoginViewModelFactory
import com.linkid.sdk.databinding.FragmentLoginBinding
import com.linkid.sdk.mainAPI

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private val service = LoginService(mainAPI)
    private val repository = LoginRepository(service)
    private val viewModelFactory = LoginViewModelFactory(repository)
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        return binding.root
    }

}