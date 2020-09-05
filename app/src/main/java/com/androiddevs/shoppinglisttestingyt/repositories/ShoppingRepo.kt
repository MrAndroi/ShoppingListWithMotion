package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.Resource
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.ImageModel

interface ShoppingRepo {

    fun getAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun getTotalPrice():LiveData<Float>

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    suspend fun getImage(keyWord:String): Resource<ImageModel>
}