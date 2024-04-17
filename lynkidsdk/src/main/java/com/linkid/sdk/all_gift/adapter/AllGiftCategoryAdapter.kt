package com.linkid.sdk.all_gift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.linkid.sdk.databinding.ItemAllGiftCategoryBinding
import com.linkid.sdk.models.category.Category

class AllGiftCategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<AllGiftCategoryAdapter.AllGiftCategoryViewHolder>() {

    var onItemClick: ((Category) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllGiftCategoryViewHolder {
        val binding =
            ItemAllGiftCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllGiftCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllGiftCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class AllGiftCategoryViewHolder(private val binding: ItemAllGiftCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {
                Glide.with(root.context)
                    .load(category.fullLink)
                    .into(imgCategory)
                txtCategory.text = category.name
            }
        }
    }
}