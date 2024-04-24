package vn.linkid.sdk.category.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.linkid.sdk.R
import vn.linkid.sdk.databinding.ItemCategoryBinding
import vn.linkid.sdk.models.category.Category


class CategoryAdapter(
    private var categories: List<Category>,
    private var selectedCategoryCode: String
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var onItemClick: ((Category) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding, selectedCategoryCode)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    fun updateSelectedCategoryCode(newSelectedCategoryCode: String) {
        val oldSelectedCategoryCode = selectedCategoryCode
        selectedCategoryCode = newSelectedCategoryCode

        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = categories.size
            override fun getNewListSize(): Int = categories.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return !(categories[oldItemPosition].code == oldSelectedCategoryCode ||
                        categories[oldItemPosition].code == newSelectedCategoryCode)
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateCategories(newCategories: List<Category>) {
        val diffCallback = CategoryDiffCallback(categories, newCategories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        categories = newCategories
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val selectedCategoryCode: String
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.apply {
                Glide.with(root.context)
                    .load(if (category.code == "all") R.drawable.ic_category_all else category.fullLink)
                    .into(imgCategory)
                txtCategory.text = category.name
                if (category.code == selectedCategoryCode) {
                    val backgroundColor = ColorStateList.valueOf(Color.parseColor("#F0F0F4"))
                    backgroundCategory.setCardBackgroundColor(backgroundColor)
                    val imageBackgroundColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    cardCategory.setCardBackgroundColor(imageBackgroundColor)
                } else {
                    val backgroundColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                    backgroundCategory.setCardBackgroundColor(backgroundColor)
                    val imageBackgroundColor = ColorStateList.valueOf(Color.parseColor("#F3F3F3"))
                    cardCategory.setCardBackgroundColor(imageBackgroundColor)
                }
                itemView.setOnClickListener {
                    onItemClick?.invoke(categories[bindingAdapterPosition])
                }
            }
        }
    }

    inner class CategoryDiffCallback(
        private val oldList: List<Category>,
        private val newList: List<Category>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].code == newList[newItemPosition].code
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }


}