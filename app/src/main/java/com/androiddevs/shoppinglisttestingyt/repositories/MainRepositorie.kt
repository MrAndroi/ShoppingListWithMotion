package com.androiddevs.shoppinglisttestingyt.repositories

import androidx.lifecycle.LiveData
import com.androiddevs.shoppinglisttestingyt.data.Resource
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingDao
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.data.remote.ImageModel
import com.androiddevs.shoppinglisttestingyt.data.remote.ImagesApi
import retrofit2.Response
import javax.inject.Inject

class MainRepositories @Inject constructor(private val dao: ShoppingDao,private val api: ImagesApi):ShoppingRepo{

    override fun getAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return dao.observeAllShoppingItems()
    }

    override fun getTotalPrice(): LiveData<Float> {
        return dao.observeTotalPrice()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        dao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        dao.deleteShoppingItem(shoppingItem)
    }

    override suspend fun getImage(keyWord: String): Resource<ImageModel> {
        try {
            Resource.loading(null)
            val response = api.searchForImages(keyWord)
            if(response.isSuccessful){
                return Resource.success(response.body())
            }
            else{
                return Resource.error(response.message(),null)
            }

        }catch (e:Exception){
            return Resource.error("Error",null)
        }

    }


}