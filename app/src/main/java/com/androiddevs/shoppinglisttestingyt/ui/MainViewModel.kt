package com.androiddevs.shoppinglisttestingyt.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.data.Resource
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.ImageModel
import com.androiddevs.shoppinglisttestingyt.data.remote.ImagesApi
import com.androiddevs.shoppinglisttestingyt.repositories.MainRepositories
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val repo: MainRepositories) :ViewModel() {

    val allShoppingItems:LiveData<List<ShoppingItem>> = repo.getAllShoppingItems()
    val totalPrice:LiveData<Float> = repo.getTotalPrice()
    var allImages = MutableLiveData<Resource<ImageModel>>()

    init {
        getImageUrl("banana")
    }

    fun insertShoppingItem(shoppingItem: ShoppingItem){
        viewModelScope.launch {
            repo.insertShoppingItem(shoppingItem)
        }
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem){
        viewModelScope.launch {
            repo.deleteShoppingItem(shoppingItem)
        }
    }

    fun getImageUrl(keyWord:String)=
        viewModelScope.launch {
            allImages.value = repo.getImage(keyWord)
        }
}