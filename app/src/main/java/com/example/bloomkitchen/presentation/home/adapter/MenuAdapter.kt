package com.example.bloomkitchen.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.databinding.ItemMenuBinding
import com.example.bloomkitchen.databinding.ItemMenuListBinding
import com.example.myculinaryapp.base.ViewHolderBinder

class MenuAdapter(
    private val listener: OnItemClickedListener<Menu>,
    private val listMode: Int = MODE_LIST,
) : RecyclerView.Adapter<ViewHolder>() {
    companion object {
        const val MODE_LIST = 0
        const val MODE_GRID = 1
    }

    private var asyncDataDiffer =
        AsyncListDiffer(
            this,
            object : DiffUtil.ItemCallback<Menu>() {
                override fun areItemsTheSame(
                    oldItem: Menu,
                    newItem: Menu,
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: Menu,
                    newItem: Menu,
                ): Boolean {
                    return oldItem.hashCode() == newItem.hashCode()
                }
            },
        )

    fun submitData(data: List<Menu>) {
        asyncDataDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        return if (listMode == MODE_GRID) {
            MenuGridItemViewHolder(
                ItemMenuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        } else {
            MenuListItemViewHolder(
                ItemMenuListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener,
            )
        }
    }

    override fun getItemCount(): Int = asyncDataDiffer.currentList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        if (holder !is ViewHolderBinder<*>) return
        (holder as ViewHolderBinder<Menu>).bind(asyncDataDiffer.currentList[position])

        holder.itemView.findViewById<ImageView>(R.id.iv_app_menu).setOnClickListener {
            Toast.makeText(holder.itemView.context, "Ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
            val item = asyncDataDiffer.currentList[position]
            listener.onItemAddedToCart(item)
        }
    }
}

interface OnItemClickedListener<T> {
    fun onItemClicked(item: T)

    fun onItemAddedToCart(item: T)
}
