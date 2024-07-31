package vn.linkid.sdk.transaction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.databinding.FragmentTransactionBinding
import vn.linkid.sdk.transaction.repository.TransactionRepository
import vn.linkid.sdk.transaction.service.TransactionService
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModel
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModelFactory
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class TransactionFragment : Fragment() {

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private val viewModelFactory = TransactionViewModelFactory()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[TransactionViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView(){
        binding.apply {
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams

            if (LynkiD_SDK.isAnonymous) {
                layoutNeedLogin.root.visibility = View.VISIBLE
                layoutNeedLogin.btnInstall.setOnClickListener {

                }
                layoutNeedLogin.txtLogin.setOnClickListener {

                }
                viewPager.visibility = View.GONE
                tabLayout.visibility = View.GONE
            } else {
                layoutNeedLogin.root.visibility = View.GONE
                viewPager.visibility = View.VISIBLE
                tabLayout.visibility = View.VISIBLE
                setUpPager()
            }
        }
    }

    private fun setUpPager(){
        binding.viewPager.adapter = SectionsPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Tất cả"
                1 -> "Tích điểm"
                2 -> "Đổi điểm"
                3 -> "Dùng điểm"
                else -> null
            }
        }.attach()
    }

    inner class SectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment = TransactionListFragment.newInstance(position)
    }

}