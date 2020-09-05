package com.androiddevs.shoppinglisttestingyt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.Hit
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_image.view.*

class ImagesAdapter(private val clickListener:(Hit) -> Unit,
                    private val longClickListener:(Hit,item:View) -> Boolean)
    :RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {


    class ImagesViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)

    val diffUtil = object : DiffUtil.ItemCallback<Hit>(){
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false))
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val image = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(image.previewURL).placeholder(R.drawable.ic_image).into(ivShoppingImage)
            setOnClickListener{ clickListener(image) }
            setOnLongClickListener {
                longClickListener(image,it)
            }
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}