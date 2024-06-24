package vn.linkid.sdk.transaction.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemTransactionBinding
import vn.linkid.sdk.models.merchant.GetMerchant
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.utils.formatDateTimeToHourMinuteDayMonth
import vn.linkid.sdk.utils.formatPrice

class TransactionAdapter(private val merchants: List<GetMerchant>) :
    PagingDataAdapter<GetTransactionItem, TransactionAdapter.TransactionViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((GetTransactionItem) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetTransactionItem>() {
            override fun areItemsTheSame(
                oldItem: GetTransactionItem,
                newItem: GetTransactionItem,
            ): Boolean =
                oldItem.tokenTransID == newItem.tokenTransID

            override fun areContentsTheSame(
                oldItem: GetTransactionItem,
                newItem: GetTransactionItem,
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transactionItem = getItem(position)
        transactionItem?.let { holder.bind(it) }
    }


    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: GetTransactionItem) {
            binding.apply {
                setUpLogo(transaction)
                setUpTitle(transaction)
                txtTime.text = formatDateTimeToHourMinuteDayMonth(transaction.time ?: "")
                val walletAddress = transaction.userAddress ?: ""
                val toWalletAddress = transaction.toWalletAddress ?: ""
                if (walletAddress.isNotEmpty() && toWalletAddress.isNotEmpty() && walletAddress == toWalletAddress) {
                    txtCoin.text = "+${(transaction.tokenAmount ?: 0).formatPrice()}"
                    txtCoin.setTextColor(Color.parseColor("#34C759"))
                } else {
                    txtCoin.text = "-${(transaction.tokenAmount ?: 0).formatPrice()}"
                    txtCoin.setTextColor(Color.parseColor("#F5574E"))
                }
                itemView.setOnClickListener {
                    onItemClick?.invoke(transaction)
                }
            }
        }


        private fun ItemTransactionBinding.setUpLogo(transaction: GetTransactionItem) {
            fun getImageUrl(merchants: List<GetMerchant>, transaction: GetTransactionItem): String {
                val merchant =
                    merchants.firstOrNull { it.id == transaction.merchantId } ?: return ""
                return if (!transaction.storeName.isNullOrEmpty()) {
                    merchant.storeList?.firstOrNull { it.id == transaction.storeId }?.avatar ?: ""
                } else {
                    merchant.logo ?: ""
                }
            }

            val imageUrl = getImageUrl(merchants, transaction)
            Glide.with(root.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_brand_placeholder)
                .into(imgBrand)
        }

        private fun ItemTransactionBinding.setUpTitle(transaction: GetTransactionItem) {
            txtTitle.text = when (transaction.actionType) {
                "Exchange" -> "Đổi điểm với đối tác ${transaction.fromMerchantNameOrMember}"
                "Adjust" -> "Điều chỉnh giao dịch"
                "Action" -> when (transaction.actionCode) {
                    "Adjust" -> "Cấp điểm từ ${transaction.fromMerchantNameOrMember}"
                    "Birthday" -> "${transaction.fromMerchantNameOrMember} chúc mừng sinh nhật bạn"
                    "Custom" -> "Quà tặng từ LynkiD"
                    "ClaimQuest" -> "Nhận điểm từ thử thách"
                    "ClaimMission" -> "Nhận điểm từ nhiệm vụ"
                    "MemoryLand" -> when (transaction.actionCodeDetail) {
                        "ReadNews" -> "Kỷ niệm đọc báo"
                        "FirstLogin" -> "Kỷ niệm đăng nhập lần đầu"
                        "DailyLogin" -> "Kỷ niệm đăng nhập hàng ngày"
                        else -> "Quà tặng từ ${transaction.fromMerchantNameOrMember}"
                    }

                    "ActionRule" -> when (transaction.actionCodeDetail) {
                        "ReadNews" -> "Tặng điểm đọc báo"
                        "FirstLogin" -> "Tặng điểm đăng nhập lần đầu"
                        "DailyLogin" -> "Tặng điểm đăng nhập hàng ngày"
                        "PayByToken" -> "Hoàn điểm cho chi tiêu tại ${transaction.fromMerchantNameOrMember}"
                        "Redeem" -> "Tặng điểm hành động đổi quà"
                        "1stPayByQR" -> "Hoàn điểm cho chi tiêu lần đầu tại ${transaction.fromMerchantNameOrMember}"
                        "Exchange" -> "Tặng điểm hành động đổi điểm"
                        "ConnectMerchant" -> "Tặng điểm kết nối đối tác lần đầu"
                        "MLMAction" -> "Tặng điểm đơn hàng đa cấp"
                        else -> "Tích điểm từ ${transaction.fromMerchantNameOrMember}"
                    }

                    "Referee" -> "Nhận thưởng hoa hồng từ CT Giới thiệu bạn bè"
                    "Referrer" -> "Nhận thưởng hoa hồng từ CT Giới thiệu bạn bè"
                    else -> "Số dư điểm LynkiD thay đổi"
                }

                "BatchManualGrant", "SingleManualGrant" -> "Điểm thưởng từ ${transaction.fromMerchantNameOrMember}"
                "TopUp" -> "Nạp tiền thành công"
                "Expired" -> "Điểm của bạn bị thu hồi do hết hạn sử dụng"
                "Redeem" -> "Đổi quà tại LynkiD"
                "PayByToken" -> "Tiêu điểm tại ${transaction.storeName ?: transaction.toMerchantNameOrMember}"
                "Transfer" -> "Nhận điểm từ ${transaction.fromMerchantNameOrMember}"
                "ReturnFull", "ReturnPartial", "RevertOrder" -> "Giao dịch thu hồi điểm tại ${transaction.toMerchantNameOrMember}"
                "Revert" -> "Hoàn điểm do giao dịch thất bại tại ${transaction.fromMerchantNameOrMember}"
                "CashedOut" -> "CashedOut từ LynkiD"
                "CashedOutFee" -> "CashedOutFee từ LynkiD"
                "AdjustPlus" -> "Cấp điểm từ ${transaction.fromMerchantNameOrMember}"
                "Claim" -> "Claim từ LynkiD"
                "RevertExchange" -> "Điều chỉnh điểm từ ${transaction.toMerchantNameOrMember}"
                "Deposit" -> "Deposit từ LynkiD"
                "RevertCashOut" -> "RevertCashOut từ LynkiD"
                "AdjustMinus" -> "Thu hồi điểm của ${transaction.toMerchantNameOrMember}"
                "RevertCashOutFee" -> "RevertCashOutFee từ LynkiD"
                "Order" -> "Tích điểm từ ${transaction.fromMerchantNameOrMember}"
                else -> "Số dư điểm LynkiD thay đổi"
            }
        }

    }

}