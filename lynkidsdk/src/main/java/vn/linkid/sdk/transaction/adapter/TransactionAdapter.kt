package vn.linkid.sdk.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemTransactionBinding
import vn.linkid.sdk.models.transaction.TransactionItem

class TransactionAdapter :
    PagingDataAdapter<TransactionItem, TransactionAdapter.TransactionViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((TransactionItem) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TransactionItem>() {
            override fun areItemsTheSame(
                oldItem: TransactionItem,
                newItem: TransactionItem,
            ): Boolean =
                oldItem.giftTransaction?.id == newItem.giftTransaction?.id

            override fun areContentsTheSame(
                oldItem: TransactionItem,
                newItem: TransactionItem,
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
        transactionItem?.let {
            holder.bind(it)
        }
    }


    inner class TransactionViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: TransactionItem) {
            binding.apply {
//                    txtTitle.text = transaction.name
//                    txtTime.text = transaction.date
//                    txtCoin.text = transaction.amount
                itemView.setOnClickListener {
                    onItemClick?.invoke(transaction)
                }
            }
        }

    }

}