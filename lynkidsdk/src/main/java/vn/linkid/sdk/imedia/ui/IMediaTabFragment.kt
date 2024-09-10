package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import vn.linkid.sdk.databinding.FragmentImeadiaTabBinding
import vn.linkid.sdk.imedia.adapter.IMediaGroupAdapter
import vn.linkid.sdk.imedia.repository.IMediaRepository
import vn.linkid.sdk.imedia.service.IMediaService
import vn.linkid.sdk.imedia.viewmodel.IMediaTabViewModel
import vn.linkid.sdk.imedia.viewmodel.IMediaTabViewModelFactory
import vn.linkid.sdk.utils.mainAPI

class IMediaTabFragment : Fragment() {
    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): IMediaTabFragment {
            val fragment = IMediaTabFragment()
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

    private lateinit var binding: FragmentImeadiaTabBinding
    private lateinit var viewModel: IMediaTabViewModel
    private val service = IMediaService(mainAPI)
    private val repository = IMediaRepository(service)
    private val viewModelFactory = IMediaTabViewModelFactory(repository)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentImeadiaTabBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[IMediaTabViewModel::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBrandByVendor().observe(viewLifecycleOwner) {
            Log.d("IMediaTabFragment", "getBrandByVendor: $it")
            val brandList = it.getOrNull() ?: emptyList()
            if (brandList.isNotEmpty()) {
                val firstBrand = brandList.first()
                getIMediaGifts(firstBrand.brandMapping?.brandId ?: 0)
            }
        }
        viewModel.getDiscountedIMedia(tab + 5).observe(viewLifecycleOwner) {
            Log.d("IMediaTabFragment", "getDiscountedIMedia: $it")
        }
    }

    private fun getIMediaGifts(brandId: Int) {
        viewModel.getAllIMedia(brandId).observe(viewLifecycleOwner) {
            Log.d("IMediaTabFragment", "getAllIMedia: $it")
            binding.apply {
                val giftList = it.getOrNull()?.items ?: emptyList()
                if (tab < 3) {
                    val iMediaList = listOf(Pair("Mệnh giá", giftList))
                    val adapter = IMediaGroupAdapter(iMediaList, 0)
                    binding.listIMedia.layoutManager = LinearLayoutManager(context)
                    binding.listIMedia.adapter = adapter
                } else {
                    val iMediaList = giftList
                        .groupBy { gift -> gift.giftInfor?.description?.split(':')?.firstOrNull() }
                        .map { (key, value) -> Pair(key ?: "", value) }
                    val adapter = IMediaGroupAdapter(iMediaList, 1)
                    binding.listIMedia.layoutManager = LinearLayoutManager(context)
                    binding.listIMedia.adapter = adapter
                }
            }
        }
    }
}