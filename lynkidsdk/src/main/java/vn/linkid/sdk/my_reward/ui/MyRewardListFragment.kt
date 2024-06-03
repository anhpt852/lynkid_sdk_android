package vn.linkid.sdk.my_reward.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vn.linkid.sdk.databinding.FragmentMyRewardListBinding
import vn.linkid.sdk.mainAPI
import vn.linkid.sdk.my_reward.repository.MyRewardListRepository
import vn.linkid.sdk.my_reward.service.MyRewardListService
import vn.linkid.sdk.my_reward.viewmodel.MyRewardListViewModel
import vn.linkid.sdk.my_reward.viewmodel.MyRewardListViewModelFactory

class MyRewardListFragment(private val tab: Int) : Fragment() {

    private lateinit var binding: FragmentMyRewardListBinding
    private lateinit var viewModel: MyRewardListViewModel
    private val service = MyRewardListService(mainAPI)
    private val repository = MyRewardListRepository(service)
    private val viewModelFactory = MyRewardListViewModelFactory(repository)
    private lateinit var adapter: MyRewardListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRewardListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[MyRewardListViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        loadData()
    }

    private fun setUpView() {
        binding.apply {
            adapter = MyRewardListAdapter(tab = tab)
            listMyReward.layoutManager = LinearLayoutManager(context)
            listMyReward.adapter = adapter
        }
    }

    private fun loadData() {
        viewModel.getMyRewards(tab).observe(viewLifecycleOwner) {
            adapter.updateGiftList(it.getOrNull()?.items ?: emptyList())
        }
    }

}