package vn.linkid.sdk.transaction.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.FragmentTransactionDetailBinding
import vn.linkid.sdk.models.transaction.GetTransactionDetail
import vn.linkid.sdk.models.transaction.TransactionDetailItem
import vn.linkid.sdk.transaction.adapter.TransactionDetailAdapter
import vn.linkid.sdk.transaction.repository.TransactionRepository
import vn.linkid.sdk.transaction.service.TransactionService
import vn.linkid.sdk.transaction.viewmodel.TransactionDetailViewModel
import vn.linkid.sdk.transaction.viewmodel.TransactionDetailViewModelFactory
import vn.linkid.sdk.transaction.viewmodel.TransactionViewModel
import vn.linkid.sdk.utils.dpToPx
import vn.linkid.sdk.utils.formatDateTimeToHourMinuteDayMonth
import vn.linkid.sdk.utils.formatDateTimeToHourMinuteDayMonthYear
import vn.linkid.sdk.utils.formatDateToDayMonthYear
import vn.linkid.sdk.utils.formatPrice
import vn.linkid.sdk.utils.getStatusBarHeight
import vn.linkid.sdk.utils.mainAPI

class TransactionDetailFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailBinding
    private lateinit var viewModel: TransactionDetailViewModel
    private val service = TransactionService(mainAPI)
    private val repository = TransactionRepository(service)
    private val viewModelFactory = TransactionDetailViewModelFactory(repository)

    private val args: TransactionDetailFragmentArgs by navArgs()
    private val transactionCode: String by lazy { args.transactionCode }
    private val isTokenTransId: Boolean by lazy { args.isTokenTransId }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[TransactionDetailViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpTransactionDetail()
    }

    private fun setUpView() {
        binding.apply {
            btnBack.setOnClickListener { findNavController().popBackStack() }
            val layoutParams = toolbar.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.topMargin = getStatusBarHeight(root) + (context?.dpToPx(12) ?: 0)
            toolbar.layoutParams = layoutParams
        }
    }

    private fun setUpTransactionDetail() {
        viewModel.getTransactionDetail(transactionCode, isTokenTransId)
            .observe(viewLifecycleOwner) { result ->
                result.onSuccess { transaction ->
                    Log.d("TransactionDetailFrag", "setUpTransactionDetail: $transaction")
                    binding.apply {
                        Glide.with(requireContext())
                            .load(transaction?.result?.contentPhoto ?: "")
                            .error(R.drawable.ic_exchange_done)
                            .into(imgTransactionIcon)
                        txtTransactionTitle.text =
                            transaction?.result?.title ?: "Số dư điểm LynkiD thay đổi"
                        txtTransactionTitle.text =
                            transaction?.result?.title ?: "Số dư điểm LynkiD thay đổi"
                        handleBalance(
                            transaction?.result?.walletAddress ?: "",
                            transaction?.result?.toWalletAddress ?: "",
                            transaction?.result?.amount ?: 0.0
                        )
                        if (transaction?.result != null) {
                            Log.d(
                                "TransactionDetailFrag",
                                "setUpTransactionDetail: ${transaction.result}"
                            )
                            handleDetailList(transaction.result)
                        }
                    }
                }
            }
    }

    private fun FragmentTransactionDetailBinding.handleBalance(
        walletAddress: String,
        toWalletAddress: String,
        amount: Double
    ) {
        if (walletAddress.isNotEmpty() && toWalletAddress.isNotEmpty() && walletAddress == toWalletAddress) {
            txtTransactionBalance.text = "+${amount.formatPrice()}"
            txtTransactionBalance.setTextColor(Color.parseColor("#34C759"))
        } else {
            txtTransactionBalance.text = "-${amount.formatPrice()}"
            txtTransactionBalance.setTextColor(Color.parseColor("#F5574E"))
        }
    }

    private fun FragmentTransactionDetailBinding.handleDetailList(transaction: GetTransactionDetail) {
        val detailList: MutableList<TransactionDetailItem> =
            emptyList<TransactionDetailItem>().toMutableList()
        if (!transaction.orderCode.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = "Mã đơn hàng",
                    body = transaction.orderCode,
                    postIcon = "copy"
                )
            )
        }
        if (!transaction.creationTime.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = if (transaction.partnerPointAmount != null && transaction.partnerPointAmount > 0) "Thời gian đổi" else "Thời gian",
                    body = formatDateTimeToHourMinuteDayMonthYear(transaction.creationTime)
                )
            )
        }
        Log.d("TransactionDetailFrag", "transaction.partnerName: ${transaction.partnerName}")
        if (!transaction.partnerName.isNullOrEmpty()) {
            Log.d(
                "TransactionDetailFrag",
                "transaction.partnerName: ${transaction.partnerName}"
            )
            detailList.add(
                TransactionDetailItem(
                    title = "Đối tác",
                    body = transaction.partnerName,
                    preIcon = transaction.partnerIcon ?: ""
                )
            )
        }
        if (transaction.partnerPointAmount != null && transaction.partnerPointAmount > 0) {
            detailList.add(
                TransactionDetailItem(
                    title = "Số điểm đối tác",
                    body = transaction.partnerPointAmount.formatPrice()
                )
            )
        }
        if (!(transaction.walletAddress.isNullOrEmpty()) && !(transaction.toWalletAddress.isNullOrEmpty()) && (transaction.walletAddress == transaction.toWalletAddress) && !transaction.expiredTime.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = "Sử dụng đến ngày",
                    body = formatDateToDayMonthYear(transaction.expiredTime)
                )
            )
        }
        if (!transaction.serviceName.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = "Dịch vụ",
                    body = transaction.serviceName
                )
            )
        }
        if (!transaction.packageName.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = "Gói sản phẩm",
                    body = transaction.packageName
                )
            )
        }
        if (!transaction.toPhoneNumber.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = "Số điện thoại",
                    body = transaction.toPhoneNumber
                )
            )
        }
        if (!transaction.usageAddress.isNullOrEmpty()) {
            detailList.add(
                TransactionDetailItem(
                    title = "Địa điểm",
                    body = transaction.usageAddress
                )
            )
        }
        Log.d("TransactionDetailFrag", "handleDetailList: $detailList")
        val adapter = TransactionDetailAdapter(detailList)
        listDetails.layoutManager = LinearLayoutManager(binding.root.context)
        val divider = ContextCompat.getDrawable(listDetails.context, R.drawable.list_divider)
        val dividerItemDecoration =
            DividerItemDecoration(listDetails.context, LinearLayoutManager.VERTICAL)
        divider?.let {
            dividerItemDecoration.setDrawable(it)
        }
        listDetails.addItemDecoration(dividerItemDecoration)
        listDetails.adapter = adapter
        handelRelatedTransaction(transaction)
        handleGift(transaction)
    }


    private fun FragmentTransactionDetailBinding.handelRelatedTransaction(transaction: GetTransactionDetail) {
        if (!transaction.relatedTokenTransId.isNullOrEmpty()) {
            viewModel.getTransactionDetail(transaction.relatedTokenTransId, true)
                .observe(viewLifecycleOwner) { result ->
                    result.onSuccess { relatedTransaction ->
                        if (relatedTransaction?.result != null) {
                            relatedTransaction.result.apply {
                                txtRelatedTitle.visibility = View.VISIBLE
                                layoutRelated.visibility = View.VISIBLE
                                Glide.with(requireContext())
                                    .load(brandImage ?: "")
                                    .error(R.drawable.img_lynkid)
                                    .centerCrop()
                                    .into(imgRelatedBrand)
                                txtTitle.text = title
                                txtBrand.text = brandName ?: "Thương hiệu khác"
                                if (!creationTime.isNullOrEmpty()) {
                                    txtTime.visibility = View.VISIBLE
                                    txtTime.text =
                                        "HSD: ${formatDateTimeToHourMinuteDayMonth(creationTime)}"
                                } else {
                                    txtTime.visibility = View.GONE
                                }
                                if ((walletAddress ?: "").isNotEmpty() && (toWalletAddress
                                        ?: "").isNotEmpty() && walletAddress == toWalletAddress
                                ) {
                                    txtCoin.text = "+${(amount ?: 0.0).formatPrice()}"
                                    txtCoin.setTextColor(Color.parseColor("#34C759"))
                                } else {
                                    txtCoin.text = "-${(amount ?: 0.0).formatPrice()}"
                                    txtCoin.setTextColor(Color.parseColor("#F5574E"))
                                }
                            }
                        }
                    }
                }
        } else {
            txtRelatedTitle.visibility = View.GONE
            layoutRelated.visibility = View.GONE
        }
    }


    private fun FragmentTransactionDetailBinding.handleGift(transaction: GetTransactionDetail) {
        if (!transaction.giftName.isNullOrEmpty()) {
            layoutGift.visibility = View.VISIBLE
            Glide.with(requireContext()).load(transaction.brandImage ?: "").into(imgBrand)
            txtBrand.text = transaction.brandName ?: "Thương hiệu khác"
            txtGiftName.text = transaction.giftName
            if (!transaction.expiredTime.isNullOrEmpty()) {
                txtExpireDate.visibility = View.VISIBLE
                txtExpireDate.text = "HSD: ${formatDateToDayMonthYear(transaction.expiredTime)}"
            } else {
                txtExpireDate.visibility = View.GONE
            }
            if (transaction.redeemQuantity != null && transaction.redeemQuantity > 1) {
                tagAmount.visibility = View.VISIBLE
                txtAmount.text = "x${transaction.redeemQuantity}"
            } else {
                tagAmount.visibility = View.GONE
            }
        } else {
            layoutGift.visibility = View.GONE
        }
    }


}