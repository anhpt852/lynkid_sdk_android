package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import vn.linkid.sdk.databinding.FragmentImeadiaTabHomeBinding

class IMediaTabHomeFragment : Fragment() {
    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int): IMediaTabHomeFragment {
            val fragment = IMediaTabHomeFragment()
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

    private lateinit var binding: FragmentImeadiaTabHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImeadiaTabHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.apply {
            viewPager.adapter = SectionsPagerAdapter(this@IMediaTabHomeFragment)
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tabView, position ->
                tabView.text = if (tab == 0) when (position) {
                    0 -> "Trả trước"
                    1 -> "Đổi mã thẻ"
                    2 -> "Trả sau"
                    else -> null
                } else when (position) {
                    0 -> "Nạp data"
                    1 -> "Đổi mã thẻ"
                    else -> null
                }
            }.attach()
        }
    }


    inner class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = if (tab == 0) 3 else 2

        override fun createFragment(position: Int): Fragment =
            IMediaTabFragment.newInstance(tab * 3 + position)
    }

}