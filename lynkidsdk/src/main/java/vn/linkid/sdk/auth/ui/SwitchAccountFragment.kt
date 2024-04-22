package vn.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import vn.linkid.sdk.auth.repository.SwitchAccountRepository
import vn.linkid.sdk.auth.service.SwitchAccountService
import vn.linkid.sdk.auth.viewmodel.SwitchAccountViewModel
import vn.linkid.sdk.auth.viewmodel.SwitchAccountViewModelFactory
import vn.linkid.sdk.databinding.FragmentSwitchAccountBinding
import vn.linkid.sdk.dpToPx
import vn.linkid.sdk.getStatusBarHeight
import vn.linkid.sdk.mainAPI

class SwitchAccountFragment : Fragment() {

    private lateinit var viewModel: SwitchAccountViewModel
    private val service = SwitchAccountService(mainAPI)
    private val repository = SwitchAccountRepository(service)
    private val viewModelFactory = SwitchAccountViewModelFactory(repository)
    private lateinit var binding: FragmentSwitchAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSwitchAccountBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[SwitchAccountViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val layoutParams = btnExit.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            btnExit.layoutParams = layoutParams
        }
    }
}