package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.databinding.FragmentImeadiaHistoryTabBinding
import vn.linkid.sdk.imedia.adapter.IMediaHistoryTabAdapter
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.imedia.service.IMediaService
import vn.linkid.sdk.imedia.viewmodel.IMediaHistoryTabViewModel
import vn.linkid.sdk.imedia.viewmodel.IMediaHistoryTabViewModelFactory
import vn.linkid.sdk.my_reward.ui.TrialGiftDialog
import vn.linkid.sdk.utils.mainAPI

class IMediaHistoryTabFragment : Fragment() {
    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): IMediaHistoryTabFragment {
            val fragment = IMediaHistoryTabFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }


    private var tab: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = it.getInt(ARG_POSITION)
        }
    }

    private lateinit var binding: FragmentImeadiaHistoryTabBinding
    private lateinit var viewModel: IMediaHistoryTabViewModel
    private val service = IMediaService(mainAPI)
    private val repository = IMediaRepository(service)
    private lateinit var viewModelFactory: IMediaHistoryTabViewModelFactory
    private lateinit var adapter: IMediaHistoryTabAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImeadiaHistoryTabBinding.inflate(inflater, container, false)
        viewModelFactory = IMediaHistoryTabViewModelFactory(repository, tab)
        viewModel = ViewModelProvider(this, viewModelFactory)[IMediaHistoryTabViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.apply {
            viewModel.uiState.observe(viewLifecycleOwner) { (isLoading, isEmpty) ->
                binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
                if (isEmpty && !isLoading) {
                    binding.layoutEmpty.visibility = View.VISIBLE
                } else {
                    binding.layoutEmpty.visibility = View.GONE
                }
            }

            adapter = IMediaHistoryTabAdapter(tab)
            listIMediaHistory.layoutManager = LinearLayoutManager(binding.root.context)
            listIMediaHistory.adapter = adapter
            adapter.onItemClick = { iMediaHistory ->
                Log.d("IMediaHistoryTab", "Selected iMediaHistory: $iMediaHistory")
                (activity as LynkiDSDKActivity).navigateFromIMediaHistoryToMyRewardDetail(
                    iMediaHistory.transactionCode ?: ""
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
        }
        viewModel.iMediaHistory.observe(viewLifecycleOwner) { iMediaHistory ->
            Log.d("IMediaHistoryTab", "getIMediaHistory: $iMediaHistory")
            adapter.submitData(lifecycle, iMediaHistory)
        }
    }
}