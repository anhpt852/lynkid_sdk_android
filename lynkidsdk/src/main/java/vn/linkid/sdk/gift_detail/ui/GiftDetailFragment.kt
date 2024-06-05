package vn.linkid.sdk.gift_detail.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.databinding.FragmentGiftDetailBinding
import vn.linkid.sdk.gift_detail.repository.GiftDetailRepository
import vn.linkid.sdk.gift_detail.service.GiftDetailService
import vn.linkid.sdk.gift_detail.viewmodel.GiftDetailViewModel
import vn.linkid.sdk.gift_detail.viewmodel.GiftDetailViewModelFactory
import vn.linkid.sdk.mainAPI
import vn.linkid.sdk.models.gift.GiftDetail

class GiftDetailFragment : Fragment() {

    private lateinit var binding: FragmentGiftDetailBinding
    private lateinit var viewModel: GiftDetailViewModel
    private val service = GiftDetailService(mainAPI)
    private val repository = GiftDetailRepository(service)
    private val viewModelFactory = GiftDetailViewModelFactory(repository)

    private val args: GiftDetailFragmentArgs by navArgs()
    private val giftId: Int by lazy { args.giftId }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentGiftDetailBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[GiftDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getGiftDetail(giftId).observe(viewLifecycleOwner) { result ->
            val giftDetail = result.getOrNull()
            if (giftDetail != null) {
                setUpView(giftDetail)
            }
        }
    }

    private fun setUpView(giftDetail: GiftDetail) {
        // Set up view
    }

}