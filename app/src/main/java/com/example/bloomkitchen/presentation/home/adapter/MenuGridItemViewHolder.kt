package com.example.bloomkitchen.presentation.home.adapter

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.databinding.ItemMenuBinding
import com.example.bloomkitchen.utils.toIndonesianFormat
import com.example.myculinaryapp.base.ViewHolderBinder

class MenuGridItemViewHolder(
    private val binding: ItemMenuBinding,
    private val listener: OnItemClickedListener<Menu>
) : ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        item.let {
            binding.ivMenuImage.load(item.imgUrl)
            binding.tvMenuName.text = item.name
            binding.tvMenuPrice.text = item.price.toIndonesianFormat()
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}