package vn.linkid.sdk.my_reward.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.linkid.sdk.databinding.FragmentMyRewardListBinding

class MyRewardListFragment(private val type: Int): Fragment() {

    private lateinit var binding: FragmentMyRewardListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRewardListBinding.inflate(inflater, container, false)
        return binding.root
    }

}