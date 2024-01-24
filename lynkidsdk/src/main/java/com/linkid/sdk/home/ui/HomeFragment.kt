package com.linkid.sdk.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.linkid.sdk.R
import com.linkid.sdk.databinding.FragmentHomeBinding
import com.linkid.sdk.dpToPx
import com.linkid.sdk.getStatusBarHeight
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
        setUpView()
        setUpMemberInfo()
        setUpPointInfo()
        setUpBannersAndNews()
    }

    private fun setUpView(){
        binding.apply {
            val layoutParams = imgAvatar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            imgAvatar.layoutParams = layoutParams
        }
    }

    private fun setUpMemberInfo(){
        viewModel.memberInfoLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpMemberInfo: $loader")
            }
        }
        viewModel.memberInfo.observe(viewLifecycleOwner) { memberInfo ->
            if (memberInfo.getOrNull() != null) {
                val member = memberInfo.getOrNull()!!
                binding.apply {
                    Glide.with(imgAvatar)
                        .load(member.avatar)
                        .circleCrop()
                        .into(imgAvatar)
                    txtMemberName.text = member.name
                }
            }
        }
    }

    private fun setUpPointInfo(){
        viewModel.pointInfoLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpPointInfo: $loader")
            }
        }
        viewModel.pointInfo.observe(viewLifecycleOwner) { pointInfo ->
            if (pointInfo.getOrNull() != null) {
                val point = pointInfo.getOrNull()!!
                binding.apply {
                    txtBalance.text = point.tokenBalance.toString()
                }
            }
        }
    }

    private fun setUpBannersAndNews() {
        viewModel.bannersAndNewsLoader.observe(viewLifecycleOwner) { loader ->
            if (loader) {
                Log.d("HomeFragment", "setUpBannersAndNews: $loader")
            }
        }
        viewModel.bannersAndNews.observe(viewLifecycleOwner) { bannersAndNews ->
            if (bannersAndNews.getOrNull() != null) {
                Log.d("HomeFragment", "setUpBannersAndNews: ${bannersAndNews.getOrNull()}")
            }
        }
    }
}