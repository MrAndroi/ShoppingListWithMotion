package com.androiddevs.shoppinglisttestingyt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_shopping.view.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {


    class ShoppingViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<ShoppingItem>(){
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shopping,parent,false))
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val shoppingItem = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(shoppingItem.imageUrl).placeholder(R.drawable.ic_image).into(ivShoppingImage)
            tvShoppingItemAmount.text = shoppingItem.amount.toString()
            tvName.text = shoppingItem.name
            tvShoppingItemPrice.text = shoppingItem.price.toString()
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}