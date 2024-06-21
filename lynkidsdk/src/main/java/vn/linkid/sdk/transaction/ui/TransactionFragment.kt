package vn.linkid.sdk.transaction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.databinding.FragmentTransactionBinding
import vn.linkid.sdk.transaction.repository.TransactionRepository
import vn.linkid.sdk.transaction.service.TransactionService
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModel
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModelFactory
import vn.linkid.sdk.utils.mainAPI

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private val service = TransactionService(mainAPI)
    private val repository = TransactionRepository(service)
    private val viewModelFactory = TransactionViewModelFactory(repository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]
        return binding.root
    }

}