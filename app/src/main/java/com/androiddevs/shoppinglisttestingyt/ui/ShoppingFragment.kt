package com.androiddevs.shoppinglisttestingyt.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapter.ShoppingAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_shopping.*

@AndroidEntryPoint
class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    val viewModel:MainViewModel by viewModels()
    lateinit var shoppingAdapter: ShoppingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingAdapter = ShoppingAdapter()
        rvShoppingItems.adapter = shoppingAdapter
        rvShoppingItems.apply {
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
        subscribeToObservers()

        fabAddShoppingItem.setOnClickListener {
            this.apply {
                exitTransition = MaterialElevationScale(false).apply {
                    duration = 500
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 500
                }
            }

            findNavController().navigate(R.id.action_shoppingFragment_to_addShoppingItemFragment)
        }
    }

    private fun subscribeToObservers(){
        viewModel.allShoppingItems.observe(viewLifecycleOwner, Observer {
            shoppingAdapter.differ.submitList(it)
            shoppingAdapter.notifyDataSetChanged()
        })

        viewModel.totalPrice.observe(viewLifecycleOwner, Observer {
            it?.let {
                tvShoppingItemPrice.text = it.toString()+"$"
            }
        })
    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val shoppingItem = shoppingAdapter.differ.currentList[position]
            viewModel.deleteShoppingItem(shoppingItem)
            Snackbar.make(requireView(), "Successfully deleted run", Snackbar.LENGTH_SHORT).apply {
                setAction("Undo") {
                    viewModel.insertShoppingItem(shoppingItem)
                }
                show()
            }
        }
    }

}