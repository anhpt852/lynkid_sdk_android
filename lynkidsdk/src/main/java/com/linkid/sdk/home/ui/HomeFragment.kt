package com.linkid.sdk.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.R
import com.linkid.sdk.databinding.FragmentHomeBinding
import com.linkid.sdk.home.repository.HomeRepository
import com.linkid.sdk.home.service.HomeService
import com.linkid.sdk.home.viewmodel.HomeViewModel
import com.linkid.sdk.home.viewmodel.HomeViewModelFactory
import com.linkid.sdk.mainAPI

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private val service = HomeService(mainAPI)
    private val repository = HomeRepository(service)
    private val viewModelFactory = HomeViewModelFactory(repository)
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBannersAndNews()
    }

    private fun setUpBannersAndNews() {
        viewModel.bannersAndNews.observe(viewLifecycleOwner) { bannersAndNews ->
            if (bannersAndNews.getOrNull() != null) {

            }
        }
    }
}