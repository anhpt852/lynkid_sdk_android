package vn.linkid.sdk.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.FragmentSearchBinding
import vn.linkid.sdk.search.adapter.SearchAdapter
import vn.linkid.sdk.search.repository.SearchRepository
import vn.linkid.sdk.search.service.SearchService
import vn.linkid.sdk.search.viewmodel.SearchViewModel
import vn.linkid.sdk.search.viewmodel.SearchViewModelFactory
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class SearchFragment: Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel
    private val service = SearchService(mainAPI)
    private val repository = SearchRepository(service)
    private val viewModelFactory = SearchViewModelFactory(repository)
    private val searchAdapter = SearchAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader()
        setUpView()
    }
    private fun setUpLoader() {
        viewModel.uiState.observe(viewLifecycleOwner) { (isLoading, isEmpty) ->
            Log.d("SearchFragment", "setUpLoader: isLoading: $isLoading, isEmpty: $isEmpty")
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isEmpty && !isLoading) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }


    private fun setUpView(){
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
            btnClose.setOnClickListener { findNavController().popBackStack() }
            searchAdapter.onItemClick = {
                val action = SearchFragmentDirections.actionSearchFragmentToGiftDetailFragment(
                    it.giftInfo?.id ?: 0
                )
                findNavController().navigate(action)
            }
            searchAdapter.registerAdapterDataObserver(object :
                RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(searchAdapter.itemCount == 0)
                }

                override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(searchAdapter.itemCount == 0)
                }

                override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                    viewModel.isEmpty.postValue(searchAdapter.itemCount == 0)
                }
            })
            listGift.layoutManager = LinearLayoutManager(context)
            listGift.adapter = searchAdapter

            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    Log.d("SearchFragment", "afterTextChanged: ${s.toString()}")
                    viewModel.setKeyword(s.toString())
                }
            })
        }
        viewModel.giftList.observe(viewLifecycleOwner) { gifts ->
            searchAdapter.submitData(lifecycle, gifts)
            viewModel.isEmpty.postValue(searchAdapter.itemCount == 0)
        }
    }

}