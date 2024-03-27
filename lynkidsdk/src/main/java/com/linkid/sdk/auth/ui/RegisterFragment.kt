package com.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.auth.repository.RegisterRepository
import com.linkid.sdk.auth.service.RegisterService
import com.linkid.sdk.auth.viewmodel.RegisterViewModel
import com.linkid.sdk.auth.viewmodel.RegisterViewModelFactory
import com.linkid.sdk.databinding.FragmentRegisterBinding
import com.linkid.sdk.mainAPI

class RegisterFragment: Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private val service = RegisterService(mainAPI)
    private val repository = RegisterRepository(service)
    private val viewModelFactory = RegisterViewModelFactory(repository)
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
        return binding.root
    }

}