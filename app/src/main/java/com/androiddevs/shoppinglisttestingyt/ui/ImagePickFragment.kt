package com.androiddevs.shoppinglisttestingyt.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import androidx.transition.TransitionManager
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.adapter.ImagesAdapter
import com.androiddevs.shoppinglisttestingyt.data.Resource
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_image_pick.*

@AndroidEntryPoint
class ImagePickFragment :Fragment(R.layout.fragment_image_pick) {

    val viewModel:MainViewModel by viewModels()
    lateinit var imagesAdapter: ImagesAdapter

    private val closeRecipientCardOnBackPressed = object : OnBackPressedCallback(false) {
        var expandedChip: View? = null
        override fun handleOnBackPressed() {
            expandedChip?.let { collapseChip(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this,closeRecipientCardOnBackPressed)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.ivShoppingImage)
            endView = imagesContainer
            duration = 500
            scrimColor = Color.TRANSPARENT
            containerColor = Color.TRANSPARENT
            startContainerColor = Color.BLACK
            endContainerColor = Color.BLACK
        }
        returnTransition = Slide().apply {
            addTarget(imagesContainer)
            duration=500
        }

        imagesAdapter = ImagesAdapter({
            findNavController().previousBackStackEntry?.savedStateHandle?.set("imageUrl",it.previewURL)
            findNavController().popBackStack()
        },
            {hit,item ->
                val transform = MaterialContainerTransform().apply {
                    startView = item
                    endView = imagePickerContainer
                    duration = 1000
                    scrimColor = Color.TRANSPARENT
                    addTarget(imagePickerContainer)
                }
                Glide.with(this).load(hit.largeImageURL).placeholder(R.drawable.ic_image).into(imagePreview)
                closeRecipientCardOnBackPressed.expandedChip = item
                closeRecipientCardOnBackPressed.isEnabled = true
                TransitionManager.beginDelayedTransition(imagesContainer, transform)
                imagePickerContainer.visibility = View.VISIBLE
                rvImages.visibility = View.INVISIBLE
                Glide.with(this)
                    .load(hit.previewURL)
                    .apply(bitmapTransform(BlurTransformation(25, 100)))
                    .into(blurBackground)
                blurBackground.visibility = View.VISIBLE
                val imageUrl = hit.previewURL
                fabSelectImage.setOnClickListener {

                    findNavController().previousBackStackEntry?.savedStateHandle?.set("imageUrl",imageUrl)
                    findNavController().popBackStack()
                }
                fabCancel.setOnClickListener {
                    closeRecipientCardOnBackPressed.expandedChip = item
                    closeRecipientCardOnBackPressed.isEnabled = true
                    collapseChip(item)
                }
                return@ImagesAdapter true
            })


        svImages.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchForImages(query!!)
                imagesAdapter.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchForImages(newText!!)
                imagesAdapter.notifyDataSetChanged()
                return true
            }

        })

        viewModel.allImages.observe(viewLifecycleOwner, Observer {
            when(it){
                Resource.loading(null) -> progressBar.visibility = View.VISIBLE
                Resource.success(it.data) ->{
                    progressBar.visibility = View.GONE
                    imagesAdapter.differ.submitList(it.data?.hits)
                    rvImages.adapter = imagesAdapter
                }
                Resource.error("Error",null)-> progressBar.visibility = View.GONE
            }
        })
    }

    private fun searchForImages(keyWord:String){
        viewModel.getImageUrl(keyWord)
    }

    private fun collapseChip(item:View) {
        // Remove the scrim view and on back pressed callbacks
        closeRecipientCardOnBackPressed.expandedChip = null
        closeRecipientCardOnBackPressed.isEnabled = false

        val transform2 = MaterialContainerTransform().apply {
            startView = imagePickerContainer
            endView = item
            duration = 500
            scrimColor = Color.TRANSPARENT
            addTarget(item)
        }
        TransitionManager.beginDelayedTransition(imagesContainer, transform2)
        rvImages.visibility = View.VISIBLE
        imagePickerContainer.visibility = View.INVISIBLE
    }
}