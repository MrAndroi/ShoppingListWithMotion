package com.androiddevs.shoppinglisttestingyt.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.Slide
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_shopping_item.*

@AndroidEntryPoint
class AddShoppingItemFragment:Fragment(R.layout.fragment_add_shopping_item) {

    val viewModel:MainViewModel by viewModels()
    var imageUrl = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fabAddShoppingItem)
            endView = container
            duration = 500
            scrimColor = Color.TRANSPARENT
            containerColor = Color.TRANSPARENT
            startContainerColor = Color.BLACK
            endContainerColor = Color.BLACK
        }
        returnTransition = Slide().apply {
            duration = 500
            addTarget(container)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("imageUrl")?.observe(
            viewLifecycleOwner, Observer {
                Glide.with(this).load(it).placeholder(R.drawable.ic_image).into(ivShoppingImage)
                imageUrl = it
            }
        )

        ivShoppingImage.setOnClickListener {
            this.apply {
                enterTransition = MaterialElevationScale(true).apply {
                    duration = 500
                }
                exitTransition = MaterialElevationScale(true).apply {
                    duration = 500
                }
            }

            findNavController().navigate(R.id.action_addShoppingItemFragment_to_imagePickFragment)
        }

        btnAddShoppingItem.setOnClickListener {
            insertShoppingItemToDatabase()
        }

    }

    private fun insertShoppingItemToDatabase(){
        val name = etShoppingItemName.text.toString()
        val amount = etShoppingItemAmount.text.toString()
        val price = etShoppingItemPrice.text.toString()
        val imageUrl = imageUrl
        if(price.equals("")){
            Snackbar.make(requireView(),"Enter Valid Price",Snackbar.LENGTH_SHORT).show()
        }
        else if(name.equals("")){
            Snackbar.make(requireView(),"Enter Valid Name",Snackbar.LENGTH_SHORT).show()
        }
        else if(amount.equals("")){
            Snackbar.make(requireView(),"Enter Valid Amount",Snackbar.LENGTH_SHORT).show()
        }
        else{
            val shoppingItem = ShoppingItem(name,amount.toInt(),price.toFloat(),imageUrl)
            viewModel.insertShoppingItem(shoppingItem)
            Snackbar.make(requireView(),"Item Saved Successfully",Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

    }
}