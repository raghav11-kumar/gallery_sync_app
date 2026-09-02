package com.example.gallery_sync_app.screens.gallery

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentGalleryScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryScreen : Fragment(R.layout.fragment_gallery_screen) {
    private lateinit var binding: FragmentGalleryScreenBinding
    private val galleryVm: GalleryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGalleryScreenBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecyclerAdapterImp(emptyList()){
            galleryVm.deleteImage(it)
        }
        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            galleryVm.imageList.collect { list ->
                adapter.updateList(list)
            }
        }
        val launcher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                galleryVm.saveImage(it)
            }
        }
        binding.openGall.setOnClickListener {
            launcher.launch("image/*")
        }


    }
}
