package vn.linkid.sdk.imedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vn.linkid.sdk.databinding.ItemImediaBinding
import vn.linkid.sdk.databinding.ItemImediaDataBinding
import vn.linkid.sdk.models.category.Gift

class IMediaAdapter(private val iMediaList: List<Gift>, private val type: Int = 0) :
    RecyclerView.Adapter<ViewHolder>() {


    var onItemClick: ((Gift) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        if (type == 0) {
            val binding =
                ItemImediaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemIMediaViewHolder(binding)
        }
        val binding =
            ItemImediaDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemIMediaDataViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (type == 0) {
            (holder as ItemIMediaViewHolder).bind(iMediaList[position])
        } else {
            (holder as ItemIMediaDataViewHolder).bind(iMediaList[position])
        }
    }

    override fun getItemCount(): Int = iMediaList.size

    inner class ItemIMediaViewHolder(private val binding: ItemImediaBinding) :
        ViewHolder(binding.root) {
        fun bind(gift: Gift) {
            binding.apply {

            }
        }
    }

    inner class ItemIMediaDataViewHolder(private val binding: ItemImediaDataBinding) :
        ViewHolder(binding.root) {
        fun bind(gift: Gift) {
            binding.apply {

            }
        }
    }
}