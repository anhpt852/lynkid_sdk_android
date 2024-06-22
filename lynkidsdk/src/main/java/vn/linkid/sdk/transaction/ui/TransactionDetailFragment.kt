package vn.linkid.sdk.transaction.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.databinding.FragmentTransactionDetailBinding
import vn.linkid.sdk.transaction.repository.TransactionRepository
import vn.linkid.sdk.transaction.service.TransactionService
import vn.linkid.sdk.transaction.viewmodel.TransactionDetailViewModel
import vn.linkid.sdk.transaction.viewmodel.TransactionDetailViewModelFactory
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModel
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class TransactionDetailFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailBinding
    private lateinit var viewModel: TransactionDetailViewModel
    private val service = TransactionService(mainAPI)
    private val repository = TransactionRepository(service)
    private val viewModelFactory = TransactionDetailViewModelFactory(repository)

    private val args: TransactionDetailFragmentArgs by navArgs()
    private val transactionCode: String by lazy { args.transactionCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[TransactionDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpTransactionDetail()
    }

    private fun setUpView() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
        }
    }

    private fun setUpTransactionDetail(){
        viewModel.getTransactionDetail(transactionCode).observe(viewLifecycleOwner) { result ->
            result.onSuccess { transaction ->
                Log.d("TransactionDetailFragment", "setUpTransactionDetail: $transaction")
                binding.apply {

                }
            }
        }
    }

}