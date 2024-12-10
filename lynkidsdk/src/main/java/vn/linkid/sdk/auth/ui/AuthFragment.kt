package vn.linkid.sdk.auth.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import vn.linkid.sdk.auth.repository.AuthRepository
import vn.linkid.sdk.auth.service.AuthService
import vn.linkid.sdk.auth.viewmodel.AuthViewModel
import vn.linkid.sdk.auth.viewmodel.AuthViewModelFactory
import vn.linkid.sdk.databinding.FragmentAuthBinding
import vn.linkid.sdk.utils.mainAPI
import vn.linkid.sdk.models.auth.ConnectedMember
import androidx.navigation.fragment.navArgs
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.LynkiD_SDK
import vn.linkid.sdk.R
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.getNavigationBarHeight
import vn.linkid.sdk.utils.getStatusBarHeight


class AuthFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private val service = AuthService(mainAPI)
    private val repository = AuthRepository(service)
    private val viewModelFactory = AuthViewModelFactory(repository)
    private lateinit var binding: FragmentAuthBinding

    private val args: AuthFragmentArgs by navArgs()
    private val connectedMember: ConnectedMember by lazy { args.connectedMember }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]
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
            bottomLayoutParam.bottomMargin =
                getNavigationBarHeight(root) + (context?.dpToPx(24) ?: 0)
            layoutAuth.layoutParams = bottomLayoutParam

            btnExit.setOnClickListener { (activity as LynkiDSDKActivity).exitSDK() }

            txtName.text = connectedMember.basicInfo?.name
            txtPhone.text = connectedMember.phoneNumber

            btnDecline.setOnClickListener {
                LynkiD_SDK.isAnonymous = true
                val action = AuthFragmentDirections.actionAuthFragmentToHomeFragment()
                findNavController().navigate(action)
            }

            btnAllow.setOnClickListener {
                LynkiD_SDK.isAnonymous = false
                val isAccountExist = connectedMember.isExisting ?: false
                val isAccountConnected = connectedMember.connectionInfo?.isExisting ?: false
                val isDifferentPhone =
                    connectedMember.phoneNumber != connectedMember.connectionInfo?.connectedToPhone
                Log.d(
                    "AuthFragment",
                    "isAccountExist: $isAccountExist, isAccountConnected: $isAccountConnected, isDifferentPhone: $isDifferentPhone"
                )
                val action =
                    if (!isAccountExist && !isAccountConnected) AuthFragmentDirections.actionAuthFragmentToRegisterFragment(
                        connectedMember
                    )
                    else if (!isAccountExist) {
                        if (isDifferentPhone) AuthFragmentDirections.actionAuthFragmentToSwitchAccountFragment(
                            connectedMember
                        )
                        else AuthFragmentDirections.actionAuthFragmentToLoginWithoutConnectFragment(
                            connectedMember
                        )
                    } else if (!isAccountConnected) AuthFragmentDirections.actionAuthFragmentToLoginWithoutConnectFragment(
                        connectedMember
                    ) else AuthFragmentDirections.actionAuthFragmentToLoginFragment(connectedMember)
                findNavController().navigate(action)
            }
            txtPolicy.apply {
                setCustomText(this)
            }
        }
    }

    private fun setCustomText(textView: TextView) {
        val fullText = "Bằng việc nhấn \"Cho phép\", tôi đã đọc và đồng ý với Điều khoản Điều kiện và Chính sách bảo vệ dữ liệu cá nhân của LynkiD."
        val allowText = "Cho phép"
        val termsText = "Điều khoản Điều kiện"
        val privacyText = "Chính sách bảo vệ dữ liệu cá nhân"

        val spannableString = SpannableString(fullText)

        // Find indices
        val allowStart = fullText.indexOf(allowText)
        val allowEnd = allowStart + allowText.length

        val termsStart = fullText.indexOf(termsText)
        val termsEnd = termsStart + termsText.length

        val privacyStart = fullText.indexOf(privacyText)
        val privacyEnd = privacyStart + privacyText.length

        // Get custom typefaces
        val semiBoldTypeface = ResourcesCompat.getFont(textView.context, R.font.bevietnampro_semibold)
        val regularTypeface = ResourcesCompat.getFont(textView.context, R.font.bevietnampro_regular)

        // Create clickable spans with custom styling
        val termsClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://linkid.vn/terms"))
                widget.context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.typeface = semiBoldTypeface
                ds.color = Color.parseColor("#663692")
            }
        }

        val privacyClickSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://linkid.vn/policy"))
                widget.context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.typeface = semiBoldTypeface
                ds.color = Color.parseColor("#663692")
            }
        }

        // Apply typeface spans
        spannableString.setSpan(
            CustomTypefaceSpan(semiBoldTypeface!!),
            allowStart,
            allowEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Apply clickable spans (these already include the semibold typeface)
        spannableString.setSpan(
            termsClickSpan,
            termsStart,
            termsEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            privacyClickSpan,
            privacyStart,
            privacyEnd,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set regular typeface for the rest of the text
        spannableString.setSpan(
            CustomTypefaceSpan(regularTypeface!!),
            0,
            fullText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set up the TextView
        textView.apply {
            text = spannableString
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }
    }


    inner class CustomTypefaceSpan(private val typeface: Typeface) : TypefaceSpan("") {
        override fun updateDrawState(ds: TextPaint) {
            applyCustomTypeface(ds)
        }

        override fun updateMeasureState(paint: TextPaint) {
            applyCustomTypeface(paint)
        }

        private fun applyCustomTypeface(paint: Paint) {
            paint.typeface = typeface
        }
    }
}