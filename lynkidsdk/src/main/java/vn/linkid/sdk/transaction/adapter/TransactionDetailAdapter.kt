package vn.linkid.sdk.transaction.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.databinding.ItemTransactionDetailBinding
import vn.linkid.sdk.models.transaction.TransactionDetailItem
import vn.linkid.sdk.utils.copyToClipboard

class TransactionDetailAdapter(private val items: List<TransactionDetailItem>) :
    RecyclerView.Adapter<TransactionDetailAdapter.TransactionDetailViewHolder>() {
    var onClipBoardClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionDetailViewHolder {
        val binding =
            ItemTransactionDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionDetailViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class TransactionDetailViewHolder(private val binding: ItemTransactionDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionDetailItem) {
            binding.apply {
                txtTitle.text = item.title
                txtBody.text =
                    if (item.postIcon != "copy") item.body else if (item.body.length > 12) "${
                        item.body.substring(
                            0,
                            6
                        )
                    }...${item.body.substring(item.body.length - 6)}" else item.body
                if (item.preIcon.isNotEmpty()) {
                    imgMerchant.visibility = View.VISIBLE
                    Glide.with(root.context).load(item.preIcon).into(imgMerchant)
                } else {
                    imgMerchant.visibility = View.GONE
                }
                if (item.postIcon.isNotEmpty()) {
                    if (item.postIcon == "copy") {
                        imgCoin.visibility = View.GONE
                        imgCopy.visibility = View.VISIBLE
                        itemView.setOnClickListener {
                            copyToClipboard(
                                binding.root.context,
                                item.body,
                                "transaction_id",
                                "Đã sao chép ${item.title}"
                            )
                        }
                    } else {
                        imgCoin.visibility = View.VISIBLE
                        imgCopy.visibility = View.GONE
                        Glide.with(root.context).load(item.postIcon).into(imgCoin)
                    }
                } else {
                    imgCoin.visibility = View.GONE
                    imgCopy.visibility = View.GONE
                }
            }
        }
    }

}