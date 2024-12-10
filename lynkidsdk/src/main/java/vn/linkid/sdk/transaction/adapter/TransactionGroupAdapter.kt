package vn.linkid.sdk.transaction.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.LynkiDSDKActivity
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemGroupTransactionBinding
import vn.linkid.sdk.databinding.ItemGroupTransactionInstallAppBinding
import vn.linkid.sdk.databinding.ItemTransactionBinding
import vn.linkid.sdk.models.merchant.GetMerchant
import vn.linkid.sdk.models.transaction.GetTransactionItem
import vn.linkid.sdk.models.transaction.GroupTransactionItem
import vn.linkid.sdk.utils.formatDateTimeToHourMinuteDayMonth
import vn.linkid.sdk.utils.formatPrice
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TransactionGroupAdapter(private val merchants: List<GetMerchant>) :
    PagingDataAdapter<GroupTransactionItem, RecyclerView.ViewHolder>(
        DIFF_CALLBACK
    ) {
    var onItemClick: ((GetTransactionItem) -> Unit)? = null
    var onInstallAppClick: (() -> Unit)? = null

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_FOOTER = 1
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GroupTransactionItem>() {
            override fun areItemsTheSame(
                oldItem: GroupTransactionItem,
                newItem: GroupTransactionItem,
            ): Boolean =
                oldItem.transactions[0].tokenTransID == newItem.transactions[0].tokenTransID

            override fun areContentsTheSame(
                oldItem: GroupTransactionItem,
                newItem: GroupTransactionItem,
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_FOOTER -> {
                val binding =
                    ItemGroupTransactionInstallAppBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return GroupTransactionInstallAppViewHolder(binding)

            }

            else -> {
                val binding =
                    ItemGroupTransactionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return GroupTransactionViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GroupTransactionInstallAppViewHolder -> holder.bind()
            is GroupTransactionViewHolder -> {
                val transactionItem = getItem(position)
                transactionItem?.let { holder.bind(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPE_FOOTER else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 // Add 1 for footer
    }


    inner class GroupTransactionViewHolder(private val binding: ItemGroupTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: GroupTransactionItem) {
            binding.apply {

                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val inputFormatted = inputFormat.parse(transaction.date)
                if (inputFormatted != null) {
                    val exchangeDate = outputFormat.format(inputFormatted)
                    txtTitle.text =
                        exchangeDate + if (isSameDay(inputFormatted)) " - Hôm nay" else ""
                }
                val adapter = TransactionAdapter(merchants, transaction.transactions)
                listTransaction.layoutManager = LinearLayoutManager(binding.root.context)
                val divider =
                    ContextCompat.getDrawable(listTransaction.context, R.drawable.list_divider)
                val dividerItemDecoration =
                    DividerItemDecoration(listTransaction.context, LinearLayoutManager.VERTICAL)
                divider?.let {
                    dividerItemDecoration.setDrawable(it)
                }
                listTransaction.addItemDecoration(dividerItemDecoration)
                listTransaction.adapter = adapter
                adapter.onItemClick = { transactionItem ->
                    Log.d("TransactionListFragment", "Selected transaction: $transactionItem")
                    onItemClick?.invoke(transactionItem)
                }
            }
        }

        private fun isSameDay(date: Date): Boolean {
            val today = Calendar.getInstance()
            val calendar = Calendar.getInstance()
            calendar.time = date

            return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    today.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    today.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
        }

    }


    inner class GroupTransactionInstallAppViewHolder(private val binding: ItemGroupTransactionInstallAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                btnInstallApp.setOnClickListener {
                    onInstallAppClick?.invoke()
                }
            }
        }

    }

}