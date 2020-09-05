package com.androiddevs.shoppinglisttestingyt.data.remote

import com.androiddevs.shoppinglisttestingyt.Constans.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("?key=$API_KEY")
    suspend fun searchForImages(
        @Query("q")keyWord:String,
        @Query("page") page:Int = 3,
        @Query("per_page") pageNumbers:Int = 50
    ):Response<ImageModel>

}