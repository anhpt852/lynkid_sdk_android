package vn.linkid.sdk.imedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import vn.linkid.sdk.databinding.ItemImediaBinding
import vn.linkid.sdk.databinding.ItemImediaDataBinding
import vn.linkid.sdk.databinding.ItemImediaGroupBinding
import vn.linkid.sdk.models.category.Gift
import vn.linkid.sdk.models.gift.GiftDetail

class IMediaGroupAdapter(private val iMediaList: List<Pair<String, List<GiftDetail>>>, private val type: Int = 0) :
    RecyclerView.Adapter<IMediaGroupAdapter.ItemIMediaGroupViewHolder>() {


    var onItemClick: ((GiftDetail) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): IMediaGroupAdapter.ItemIMediaGroupViewHolder {
        val binding =
            ItemImediaGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemIMediaGroupViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ItemIMediaGroupViewHolder, position: Int) =
        holder.bind(iMediaList[position])


    override fun getItemCount(): Int = iMediaList.size

    inner class ItemIMediaGroupViewHolder(private val binding: ItemImediaGroupBinding) :
        ViewHolder(binding.root) {
        fun bind(iMedia: Pair<String, List<GiftDetail>>) {
            binding.apply {
                txtTitle.text = iMedia.first
                val adapter = IMediaAdapter(iMedia.second, type)
                adapter.onItemClick = onItemClick
                listIMedia.layoutManager = GridLayoutManager(listIMedia.context, 2)
                listIMedia.adapter = adapter
            }
        }
    }
}