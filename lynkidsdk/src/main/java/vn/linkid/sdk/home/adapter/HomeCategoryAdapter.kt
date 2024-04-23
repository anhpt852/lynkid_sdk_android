package vn.linkid.sdk.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemHomeCategoryBinding
import vn.linkid.sdk.models.category.Category

class HomeCategoryAdapter(private val categories: List<Category>) :
    RecyclerView.Adapter<HomeCategoryAdapter.HomeCategoryViewHolder>() {

    var onItemClick: ((Category) -> Unit)? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeCategoryViewHolder {
        val binding =
            ItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size


    inner class HomeCategoryViewHolder(private val binding: ItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.apply {
                Glide.with(imgCategory)
                    .load(category.fullLink ?: "")
                    .into(imgCategory)
                txtCategory.text = category.name ?: ""
                itemView.setOnClickListener { onItemClick?.invoke(category) }
            }
        }

    }
}