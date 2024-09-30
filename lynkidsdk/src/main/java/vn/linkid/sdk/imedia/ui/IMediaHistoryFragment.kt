package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import vn.linkid.sdk.databinding.FragmentImeadiaHistoryBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight

class IMediaHistoryFragment : Fragment() {

    private lateinit var binding: FragmentImeadiaHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImeadiaHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams

            btnBack.setOnClickListener { findNavController().popBackStack() }


            viewPager.adapter = SectionsPagerAdapter(this@IMediaHistoryFragment)
            viewPager.isUserInputEnabled = false
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tabView, position ->
                tabView.text = when (position) {
                    0 -> "Điện thoại"
                    1 -> "Data 3G/4G"
                    else -> null
                }
            }.attach()
        }
    }


    inner class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment =
            IMediaHistoryTabFragment.newInstance(position)
    }


}