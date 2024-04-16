package com.linkid.sdk.category.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.category.repository.CategoryRepository
import com.linkid.sdk.category.service.CategoryService
import com.linkid.sdk.category.viewmodel.CategoryViewModel
import com.linkid.sdk.category.viewmodel.CategoryViewModelFactory
import com.linkid.sdk.databinding.FragmentCategoryBinding
import com.linkid.sdk.mainAPI

class CategoryFragment: Fragment() {

    private lateinit var viewModel: CategoryViewModel
    private val service = CategoryService(mainAPI)
    private val repository = CategoryRepository(service)
    private val viewModelFactory = CategoryViewModelFactory(repository)
    private lateinit var binding: FragmentCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[CategoryViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader()
        setUpCategoryList()
        setUpGiftList()
    }

    private fun setUpLoader(){
        viewModel.loader.observe(viewLifecycleOwner, {

        })
    }

    private fun setUpCategoryList(){

    }

    private fun setUpGiftList(){

    }

}