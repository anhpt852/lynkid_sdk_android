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
    private var categories: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    var onItemClick: ((Category) -> Unit)? = null

    var selectedPosition: Int = RecyclerView.NO_POSITION
        set(value) {
            if (field != value) {
                val oldPosition = field
                field = value
                notifyItemChanged(oldPosition)
                notifyItemChanged(value)
            }
        }

    fun updateCategories(newCategories: List<Category>) {
        val diffCallback = CategoryDiffCallback(categories, newCategories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        categories = newCategories
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val isSelected = position == selectedPosition
        Log.d("CategoryAdapter", "onBindViewHolder: $isSelected")
        holder.bind(categories[position], isSelected)
    }


    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, isSelected: Boolean) {
            binding.apply {
                Glide.with(root.context)
                    .load(if (category.code == "all") R.drawable.ic_category_all else category.fullLink)
                    .placeholder(R.drawable.ic_category_placeholder)
                    .into(imgCategory)
                txtCategory.text = category.name
                if (itemView.isSelected != isSelected) {
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
                    if (selectedPosition != layoutPosition) {
                        selectedPosition = layoutPosition
                        onItemClick?.invoke(categories[bindingAdapterPosition])
                    }
                }
            }
        }
    }

    inner class CategoryDiffCallback(
        private val oldCategories: List<Category>,
        private val newCategories: List<Category>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldCategories.size

        override fun getNewListSize(): Int = newCategories.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCategories[oldItemPosition].code == newCategories[newItemPosition].code
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCategories[oldItemPosition] == newCategories[newItemPosition]
        }
    }


}