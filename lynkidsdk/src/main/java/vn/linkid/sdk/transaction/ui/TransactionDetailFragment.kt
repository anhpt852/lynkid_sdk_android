package vn.linkid.sdk.transaction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.databinding.FragmentTransactionDetailBinding
import vn.linkid.sdk.transaction.repository.TransactionRepository
import vn.linkid.sdk.transaction.service.TransactionService
import vn.linkid.sdk.transaction.viewmodel.TransactionDetailViewModel
import vn.linkid.sdk.transaction.viewmodel.TransactionDetailViewModelFactory
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModel
import vn.linkid.sdk.utils.mainAPI

class TransactionDetailFragment: Fragment() {

    private lateinit var binding: FragmentTransactionDetailBinding
    private lateinit var viewModel: TransactionDetailViewModel
    private val service = TransactionService(mainAPI)
    private val repository = TransactionRepository(service)
    private val viewModelFactory = TransactionDetailViewModelFactory(repository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionDetailViewModel::class.java]
        return binding.root
    }

}