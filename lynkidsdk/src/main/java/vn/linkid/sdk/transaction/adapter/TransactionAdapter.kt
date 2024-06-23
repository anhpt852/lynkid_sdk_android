package vn.linkid.sdk.transaction.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemTransactionBinding
import vn.linkid.sdk.models.transaction.GetTransactionDetailResponseModel

class TransactionAdapter :
    PagingDataAdapter<GetTransactionDetailResponseModel, TransactionAdapter.TransactionViewHolder>(DIFF_CALLBACK) {
    var onItemClick: ((GetTransactionDetailResponseModel) -> Unit)? = null

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GetTransactionDetailResponseModel>() {
            override fun areItemsTheSame(
                oldItem: GetTransactionDetailResponseModel,
                newItem: GetTransactionDetailResponseModel,
            ): Boolean =
                oldItem.tokenTransID == newItem.tokenTransID

            override fun areContentsTheSame(
                oldItem: GetTransactionDetailResponseModel,
                newItem: GetTransactionDetailResponseModel,
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

        fun bind(transaction: GetTransactionDetailResponseModel) {
            binding.apply {
//                    txtTitle.text = transaction.name
//                    txtTime.text = transaction.date
//                    txtCoin.text = transaction.amount
                itemView.setOnClickListener {
                    onItemClick?.invoke(transaction)
                }
            }
        }


        private fun setUpLogo(transaction: GetTransactionDetailResponseModel){
            when(transaction.actionType){

            }
        }

    }

}