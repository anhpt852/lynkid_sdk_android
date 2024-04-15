package com.linkid.sdk.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.linkid.sdk.category.repository.CategoryRepository


@Suppress("UNCHECKED_CAST")
class CategoryViewModelFactory(private val repository: CategoryRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CategoryViewModel(repository) as T
}