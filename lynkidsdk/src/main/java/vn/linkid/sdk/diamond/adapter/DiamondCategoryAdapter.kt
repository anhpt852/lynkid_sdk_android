package vn.linkid.sdk.diamond.adapter

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
import vn.linkid.sdk.databinding.ItemDiamondCategoryBinding
import vn.linkid.sdk.models.category.Category
import vn.linkid.sdk.models.diamond.DiamondCategory

class DiamondCategoryAdapter(
    private var categories: List<DiamondCategory>
) : RecyclerView.Adapter<DiamondCategoryAdapter.DiamondCategoryViewHolder>() {
    var onItemClick: ((DiamondCategory) -> Unit)? = null

    var selectedPosition: Int = RecyclerView.NO_POSITION
        set(value) {
            if (field != value) {
                val oldPosition = field
                field = value
                notifyItemChanged(oldPosition)
                notifyItemChanged(value)
            }
        }

    fun updateCategories(newCategories: List<DiamondCategory>) {
        val diffCallback = DiamondCategoryDiffCallback(categories, newCategories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        categories = newCategories
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiamondCategoryViewHolder {
        val binding =
            ItemDiamondCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiamondCategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: DiamondCategoryViewHolder, position: Int) {
        val isSelected =
            position == (if (selectedPosition == RecyclerView.NO_POSITION) 0 else selectedPosition)
        if (categories[position].giftCategory != null) {
            Log.d("DiamondCategoryAdapter", "onBindViewHolder: $isSelected")
            holder.bind(categories[position].giftCategory!!, isSelected)
        }
    }


    inner class DiamondCategoryViewHolder(
        private val binding: ItemDiamondCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category, isSelected: Boolean) {
            binding.apply {
                Glide.with(root.context)
                    .load(if (category.code == "all") R.drawable.ic_category_all_diamond else category.fullLink)
                    .placeholder(R.drawable.ic_category_all_diamond)
                    .into(imgCategory)
                txtCategory.text = category.name
                if (isSelected) {
                    backgroundCategory.setBackgroundResource(R.drawable.bg_gradient_diamond_category)
                } else {
                    backgroundCategory.setBackgroundResource(R.drawable.bg_gradient_diamond_category_none)
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

    inner class DiamondCategoryDiffCallback(
        private val oldCategories: List<DiamondCategory>,
        private val newCategories: List<DiamondCategory>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldCategories.size

        override fun getNewListSize(): Int = newCategories.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCategories[oldItemPosition].giftCategory?.code == newCategories[newItemPosition].giftCategory?.code
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCategories[oldItemPosition] == newCategories[newItemPosition]
        }
    }


}