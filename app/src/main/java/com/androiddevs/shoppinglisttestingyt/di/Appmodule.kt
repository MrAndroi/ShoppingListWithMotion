package com.androiddevs.shoppinglisttestingyt.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.shoppinglisttestingyt.Constans.BASE_URL
import com.androiddevs.shoppinglisttestingyt.Constans.DATABASE_NAME
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItemDatabase
import com.androiddevs.shoppinglisttestingyt.data.remote.ImagesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            ShoppingItemDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()


    @Provides
    @Singleton
    fun provideShoppingDoa(database:ShoppingItemDatabase) =
        database.shoppingDao()


    @Singleton
    @Provides
    fun provideRetrofitInstance() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Singleton
    @Provides
    fun provideImagesApi(retrofit: Retrofit) = retrofit.create(ImagesApi::class.java)


}