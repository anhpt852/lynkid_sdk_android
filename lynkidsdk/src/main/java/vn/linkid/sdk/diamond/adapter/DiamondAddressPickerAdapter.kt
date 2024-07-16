package vn.linkid.sdk.diamond.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vn.linkid.sdk.databinding.ItemDiamondAddressPickerBinding
import vn.linkid.sdk.models.address.Address

class DiamondAddressPickerAdapter(private val listAddress: List<Address>) :
    RecyclerView.Adapter<DiamondAddressPickerAdapter.AddressPickerViewHolder>() {
    var onItemClick: ((Address) -> Unit)? = null
    private var filteredAddress: List<Address> = listAddress.toList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressPickerViewHolder {
        val binding =
            ItemDiamondAddressPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressPickerViewHolder, position: Int) =
        holder.bind(filteredAddress[position])

    override fun getItemCount(): Int = filteredAddress.size

    fun filter(query: String) {
        val oldList = filteredAddress
        filteredAddress = if (query.isEmpty()) {
            listAddress.toList()
        } else {
            listAddress.filter {
                it.name?.contains(query, ignoreCase = true) ?: false
            }
        }
        updateList(oldList, filteredAddress)
    }

    private fun updateList(oldList: List<Address>, newList: List<Address>) {
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = oldList.size

            override fun getNewListSize() = newList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition].id == newList[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldList[oldItemPosition] == newList[newItemPosition]
            }
        })

        diffResult.dispatchUpdatesTo(this)
    }


    inner class AddressPickerViewHolder(private val binding: ItemDiamondAddressPickerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address) {
            binding.apply {
                txtAddress.text = address.name
                itemView.setOnClickListener { onItemClick?.invoke(address) }
            }
        }
    }

}