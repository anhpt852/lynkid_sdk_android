package com.linkid.sdk.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.linkid.sdk.LynkiD_SDK
import com.linkid.sdk.R
import com.linkid.sdk.databinding.FragmentSplashBinding
import com.linkid.sdk.home.viewmodel.HomeViewModel
import com.linkid.sdk.mainAPI
import com.linkid.sdk.splash.repository.SplashRepository
import com.linkid.sdk.splash.service.SplashService
import com.linkid.sdk.splash.viewmodel.SplashViewModel
import com.linkid.sdk.splash.viewmodel.SplashViewModelFactory

class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel
    private val service = SplashService(mainAPI)
    private val repository = SplashRepository(service)
    private val viewModelFactory = SplashViewModelFactory(repository)
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        binding = FragmentSplashBinding.bind(view)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToken()
    }

    private fun setUpToken(){
        viewModel.generateToken().observe(viewLifecycleOwner) {
            val authToken = it.getOrNull()
            if (authToken != null) {
                LynkiD_SDK.token = authToken.seedToken
                val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

}