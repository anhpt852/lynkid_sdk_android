package vn.linkid.sdk.all_gift.ui

import android.os.Bundle
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
import vn.linkid.sdk.all_gift.adapter.GiftGroupAdapter
import vn.linkid.sdk.all_gift.repository.GiftGroupRepository
import vn.linkid.sdk.all_gift.service.GiftGroupService
import vn.linkid.sdk.all_gift.viewmodel.GiftGroupViewModel
import vn.linkid.sdk.all_gift.viewmodel.GiftGroupViewModelFactory
import vn.linkid.sdk.category.ui.CategoryFragmentDirections
import vn.linkid.sdk.databinding.FragmentGiftGroupBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class GiftGroupFragment : Fragment() {

    private lateinit var binding: FragmentGiftGroupBinding
    private lateinit var viewModel: GiftGroupViewModel
    private val service = GiftGroupService(mainAPI)
    private val repository = GiftGroupRepository(service)
    private val viewModelFactory = GiftGroupViewModelFactory(repository)

    private val args: GiftGroupFragmentArgs by navArgs()
    private val groupCode: String by lazy { args.groupCode }
    private val groupName: String by lazy { args.groupName }

    private val adapter = GiftGroupAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentGiftGroupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[GiftGroupViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setGroupCode(groupCode)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
            txtTitle.text = groupName
            listGift.layoutManager = LinearLayoutManager(binding.root.context)
            listGift.adapter = adapter
            adapter.onItemClick = { gift ->
                val action =
                    GiftGroupFragmentDirections.actionGiftGroupFragmentToGiftDetailFragment(
                        gift.giftInfor?.id ?: 0
                    )
                findNavController().navigate(action)
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
            layoutListGift.setOnRefreshListener {
                adapter.refresh()
                layoutListGift.isRefreshing = false
            }
            viewModel.loader.observe(viewLifecycleOwner) {
                loading.visibility = if (it) View.VISIBLE else View.GONE
            }
        }
        viewModel.giftsByGroupCode.observe(viewLifecycleOwner) { giftsByGroup ->
            Log.d("GiftGroupFragment", "getGiftsByGroupCode: $giftsByGroup")
            adapter.submitData(lifecycle, giftsByGroup)
        }
    }

}