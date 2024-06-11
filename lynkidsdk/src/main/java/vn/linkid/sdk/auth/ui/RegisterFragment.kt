package vn.linkid.sdk.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import vn.linkid.sdk.auth.repository.RegisterRepository
import vn.linkid.sdk.auth.service.RegisterService
import vn.linkid.sdk.auth.viewmodel.RegisterViewModel
import vn.linkid.sdk.auth.viewmodel.RegisterViewModelFactory
import vn.linkid.sdk.databinding.FragmentRegisterBinding
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class RegisterFragment: Fragment() {

    private lateinit var viewModel: RegisterViewModel
    private val service = RegisterService(mainAPI)
    private val repository = RegisterRepository(service)
    private val viewModelFactory = RegisterViewModelFactory(repository)
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]
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

            val bottomLayoutParam = layoutAuth.layoutParams as ViewGroup.MarginLayoutParams
            bottomLayoutParam.bottomMargin = getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            layoutAuth.layoutParams = bottomLayoutParam

            btnRegister.setOnClickListener {
                viewModel.createMember().observe(viewLifecycleOwner) {
                    val action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }

}