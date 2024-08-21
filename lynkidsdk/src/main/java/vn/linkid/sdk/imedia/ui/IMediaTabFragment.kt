package vn.linkid.sdk.imedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.linkid.sdk.databinding.FragmentImeadiaTabBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImeadiaTabBinding.inflate(inflater, container, false)
        return binding.root
    }
}