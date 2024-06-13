package vn.linkid.sdk.transaction.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.linkid.sdk.databinding.FragmentTransactionDetailBinding

class TransactionDetailFragment: Fragment() {

    private lateinit var binding: FragmentTransactionDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}