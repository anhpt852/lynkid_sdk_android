package com.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.auth.repository.SwitchAccountRepository
import com.linkid.sdk.auth.service.SwitchAccountService
import com.linkid.sdk.auth.viewmodel.SwitchAccountViewModel
import com.linkid.sdk.auth.viewmodel.SwitchAccountViewModelFactory
import com.linkid.sdk.databinding.FragmentSwitchAccountBinding
import com.linkid.sdk.mainAPI

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
}