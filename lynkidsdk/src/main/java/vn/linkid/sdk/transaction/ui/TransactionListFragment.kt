package vn.linkid.sdk.transaction.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.databinding.FragmentTransactionListBinding
import vn.linkid.sdk.models.category.GiftFilterModel
import vn.linkid.sdk.transaction.adapter.TransactionAdapter
import vn.linkid.sdk.transaction.repository.TransactionRepository
import vn.linkid.sdk.transaction.service.TransactionService
import vn.linkid.sdk.transaction.viewmodel.TransactionListViewModel
import vn.linkid.sdk.transaction.viewmodel.TransactionListViewModelFactory
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class TransactionListFragment() : Fragment() {
    private var tab: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = it.getInt(ARG_POSITION)
        }
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): TransactionListFragment {
            val fragment = TransactionListFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var viewModel: TransactionListViewModel
    private val service = TransactionService(mainAPI)
    private val repository = TransactionRepository(service)
    private lateinit var viewModelFactory: TransactionListViewModelFactory
    private lateinit var binding: FragmentTransactionListBinding

    private val adapter = TransactionAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionListBinding.inflate(inflater, container, false)
        Log.d("TransactionListFragment", "tab: $tab")
        viewModelFactory = TransactionListViewModelFactory(repository, tab)
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionListViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader()
        setUpTransactionList()
    }


    private fun setUpLoader() {
        viewModel.uiState.observe(viewLifecycleOwner) { (isLoading, isEmpty) ->
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isEmpty && !isLoading) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }

    private fun setUpTransactionList() {
        binding.apply {
            listTransaction.layoutManager = LinearLayoutManager(binding.root.context)
            listTransaction.adapter = adapter
            adapter.onItemClick = { transactionItem ->
                Log.d("TransactionListFragment", "Selected transaction: $transactionItem")
                (activity as LynkiDSDKActivity).navigateFromTransactionToTransactionDetail(
                    transactionItem.giftTransaction?.transactionCode ?: ""
                )
            }
            adapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(adapter.itemCount == 0)
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(adapter.itemCount == 0)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(adapter.itemCount == 0)
                }
            })
            layoutTransactionList.setOnRefreshListener {
                adapter.refresh()
                layoutTransactionList.isRefreshing = false
            }
        }
        viewModel.transactionItems.observe(viewLifecycleOwner) { transactionItems ->
            Log.d("TransactionListFragment", "getTransactions: $transactionItems")
            adapter.submitData(lifecycle, transactionItems)
        }
    }

}