package com.androiddevs.shoppinglisttestingyt.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@SmallTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class ShoppingDaoTest {

    lateinit var database : ShoppingItemDatabase
    lateinit var shoppingDoa :ShoppingDao
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Before
    fun setUpDatabase(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),ShoppingItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        shoppingDoa = database.shoppingDao()
    }

    @Test
    fun insertShoppingItemTest(){
        val item = ShoppingItem("banana",4,5.4f,"https://sami",2)
        runBlockingTest {
            shoppingDoa.insertShoppingItem(item)
            val allShoppingItems = shoppingDoa.observeAllShoppingItems().getOrAwaitValue()
            assertThat(allShoppingItems).contains(item)
        }
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val item = ShoppingItem("banana",4,5.4f,"https://sami",2)
        shoppingDoa.insertShoppingItem(item)
        shoppingDoa.deleteShoppingItem(item)
        val shoppingItems = shoppingDoa.observeAllShoppingItems().getOrAwaitValue()
        assertThat(shoppingItems).doesNotContain(item)
    }

    @Test
    fun observeTotalPriceTest(){
        val item = ShoppingItem("banana",4,5f,"https://sami",2)
        val item2 = ShoppingItem("banana",2,4f,"https://sami",4)
        runBlockingTest {
            shoppingDoa.insertShoppingItem(item)
            shoppingDoa.insertShoppingItem(item2)
        }
        val totalPrice = shoppingDoa.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(28)
    }


    @After
    fun closeDatabase(){
        database.close()
    }

}













