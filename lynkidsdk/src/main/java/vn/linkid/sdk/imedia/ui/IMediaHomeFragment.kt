package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentImeadiaHomeBinding
import vn.linkid.sdk.models.imedia.TopupRedeemInfo
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight

class IMediaHomeFragment : Fragment() {


    private val args: IMediaHomeFragmentArgs by navArgs()
    private val tab: Int by lazy { args.tab }

    private lateinit var binding: FragmentImeadiaHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        if (this::binding.isInitialized) {
            binding
        } else {
            binding = FragmentImeadiaHomeBinding.inflate(inflater, container, false)
            setupView()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
        }
    }

    private fun setupView() {
        binding.apply {

            btnBack.setOnClickListener { findNavController().popBackStack() }
            btnHistory.setOnClickListener {
                val action =
                    IMediaHomeFragmentDirections.actionIMediaHomeFragmentToIMediaHistoryFragment()
                findNavController().navigate(action)
            }

            for (i in 0 until tabLayout.tabCount) {
                val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
                val p = tab.layoutParams as ViewGroup.MarginLayoutParams
                p.setMargins(10, 10, 10, 10)
                tab.requestLayout()
            }

            viewPager.isUserInputEnabled = false
            viewPager.isSaveEnabled = false
            viewPager.adapter = SectionsPagerAdapter(this@IMediaHomeFragment)
            tabLayout.addTab(tabLayout.newTab().setText("Điện thoại"))
            tabLayout.addTab(tabLayout.newTab().setText("Data 3G/4G"))

            minorTabLayout1.addTab(minorTabLayout1.newTab().setText("Trả trước"))
            minorTabLayout1.addTab(minorTabLayout1.newTab().setText("Đổi mã thẻ"))
            minorTabLayout1.addTab(minorTabLayout1.newTab().setText("Trả sau"))

            minorTabLayout2.addTab(minorTabLayout2.newTab().setText("Nạp data"))
            minorTabLayout2.addTab(minorTabLayout2.newTab().setText("Đổi mã thẻ"))



            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    Log.d("IMediaHomeFragment", "onTabSelected: ${tab?.position}")
                    if (tab != null) {
                        if (tab.position == 0) {
                            minorTabLayout1.visibility = View.VISIBLE
                            minorTabLayout2.visibility = View.GONE
                            viewPager.setCurrentItem(minorTabLayout1.selectedTabPosition, true)
                        } else {
                            minorTabLayout1.visibility = View.GONE
                            minorTabLayout2.visibility = View.VISIBLE
                            viewPager.setCurrentItem(minorTabLayout2.selectedTabPosition + 3, true)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            minorTabLayout1.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager.setCurrentItem(tab.position, true)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            minorTabLayout2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab != null) {
                        viewPager.setCurrentItem(tab.position + 3, true)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
            lifecycleScope.launch {
                delay(500)
                tabLayout.selectTab(tabLayout.getTabAt(tab))
            }
        }
    }


    fun navigateFromIMediaToGiftExchangeFragment(giftId: Int, topupRedeemInfo: TopupRedeemInfo) {
        val action = IMediaHomeFragmentDirections.actionIMediaHomeFragmentToGiftExchangeFragment(
            giftId = giftId,
            topupRedeemInfo = topupRedeemInfo
        )
        findNavController().navigate(action)
    }

    val tabFragments = arrayOf(
        IMediaTabFragment.newInstance(0),
        IMediaTabFragment.newInstance(1),
        IMediaTabFragment.newInstance(2),
        IMediaTabFragment.newInstance(3),
        IMediaTabFragment.newInstance(4)
    )

    inner class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 5

        override fun createFragment(position: Int): Fragment = tabFragments[position]
    }

}