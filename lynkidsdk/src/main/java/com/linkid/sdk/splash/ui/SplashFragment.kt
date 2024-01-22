package com.linkid.sdk.splash.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.linkid.sdk.R
import com.linkid.sdk.databinding.FragmentSplashBinding
import com.linkid.sdk.home.viewmodel.HomeViewModel
import com.linkid.sdk.splash.viewmodel.SplashViewModel
import com.linkid.sdk.splash.viewmodel.SplashViewModelFactory

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel
    private val viewModelFactory = SplashViewModelFactory()

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
        setUpLoader()
    }

    private fun setUpLoader() {
        viewModel.loader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
                findNavController().navigate(action)
            }
        }
    }

}