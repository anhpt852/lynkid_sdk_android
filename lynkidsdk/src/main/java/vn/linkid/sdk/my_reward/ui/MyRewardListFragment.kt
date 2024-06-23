package vn.linkid.sdk.my_reward.ui

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
import vn.linkid.sdk.databinding.FragmentMyRewardListBinding
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.my_reward.adapter.MyRewardListAdapter
import vn.linkid.sdk.my_reward.repository.MyRewardListRepository
import vn.linkid.sdk.my_reward.service.MyRewardListService
import vn.linkid.sdk.my_reward.viewmodel.MyRewardListViewModel
import vn.linkid.sdk.my_reward.viewmodel.MyRewardListViewModelFactory

class MyRewardListFragment : Fragment() {

    private var tab: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tab = it.getInt(ARG_POSITION)
        }
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): MyRewardListFragment {
            val fragment = MyRewardListFragment()
            val args = Bundle()
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: FragmentMyRewardListBinding
    private lateinit var viewModel: MyRewardListViewModel
    private val service = MyRewardListService(mainAPI)
    private val repository = MyRewardListRepository(service)
    private lateinit var viewModelFactory: MyRewardListViewModelFactory
    private val adapter = MyRewardListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyRewardListBinding.inflate(inflater, container, false)
        viewModelFactory = MyRewardListViewModelFactory(repository, tab)
        viewModel = ViewModelProvider(this, viewModelFactory)[MyRewardListViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLoader()
        setUpList()
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

    private fun setUpList() {
        binding.apply {
            listMyReward.layoutManager = LinearLayoutManager(binding.root.context)
            listMyReward.adapter = adapter
            adapter.onItemClicked = { myReward ->
                Log.d("MyRewardListFragment", "Selected reward: $myReward")
                (activity as LynkiDSDKActivity).navigateFromMyRewardToMyRewardDetail(
                    myReward.giftTransaction?.transactionCode ?: ""
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
            layoutMyRewardList.setOnRefreshListener {
                adapter.refresh()
                layoutMyRewardList.isRefreshing = false
            }
        }
        viewModel.myRewards.observe(viewLifecycleOwner) { myReward ->
            Log.d("MyRewardListFragment", "getMyRewards: $myReward")
            adapter.submitData(lifecycle, myReward)
        }
    }

}