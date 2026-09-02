package com.example.gallery_sync_app.screens.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentGalleryScreenBinding

class GalleryScreen : Fragment(R.layout.fragment_gallery_screen){
    private lateinit var binding: FragmentGalleryScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentGalleryScreenBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
    }
}