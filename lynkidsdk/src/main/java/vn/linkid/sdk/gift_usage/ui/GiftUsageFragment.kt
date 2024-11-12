package vn.linkid.sdk.gift_usage.ui

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.FragmentGiftUsageBinding
import vn.linkid.sdk.gift_usage.adapter.GiftUsageAdapter
import vn.linkid.sdk.gift_usage.repository.GiftUsageRepository
import vn.linkid.sdk.gift_usage.service.GiftUsageService
import vn.linkid.sdk.gift_usage.viewmodel.GiftUsageViewModel
import vn.linkid.sdk.gift_usage.viewmodel.GiftUsageViewModelFactory
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class GiftUsageFragment : Fragment() {

    private lateinit var binding: FragmentGiftUsageBinding
    private lateinit var viewModel: GiftUsageViewModel
    private val service = GiftUsageService(mainAPI)
    private val repository = GiftUsageRepository(service)
    private val viewModelFactory = GiftUsageViewModelFactory(repository)

    private val args: GiftUsageFragmentArgs by navArgs()
    private val giftCode: String by lazy { args.giftCode }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGiftUsageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[GiftUsageViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
        }
        viewModel.setGiftCode(giftCode)
        setUpLoader()
        setUpGiftUsage()
    }


    private fun setUpLoader() {
        viewModel.uiState.observe(viewLifecycleOwner) { (isLoading, isEmpty) ->
            Log.d("GiftUsageFragment", "setUpLoader: isLoading: $isLoading, isEmpty: $isEmpty")
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isEmpty && !isLoading) {
                binding.layoutEmpty.visibility = View.VISIBLE
            } else {
                binding.layoutEmpty.visibility = View.GONE
            }
        }
    }

    private fun setUpGiftUsage() {
        binding.apply {
            edtSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    viewModel.setName(s?.toString() ?: "")
                }
            })
            val adapter = GiftUsageAdapter()
            listAddress.layoutManager = LinearLayoutManager(binding.root.context)
            listAddress.adapter = adapter
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
            viewModel.giftUsage.observe(viewLifecycleOwner) { giftUsage ->
                Log.d("GiftUsageFragment", "setUpGiftUsage: $giftUsage")
                adapter.submitData(viewLifecycleOwner.lifecycle, giftUsage)
            }
        }
    }


}