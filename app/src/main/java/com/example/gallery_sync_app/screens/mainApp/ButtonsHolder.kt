package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.Navigation
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentButtonsHolderBinding
import com.example.gallery_sync_app.screens.utils.ReusableFunctions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ButtonsHolder.newInstance] factory method to
 * create an instance of this fragment.
 */
class ButtonsHolder : Fragment(R.layout.fragment_buttons_holder) {
    private lateinit var binding: FragmentButtonsHolderBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding= FragmentButtonsHolderBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        val bleBut=binding.bleButton
        val galleryBut=binding.GalleryButton
        val webBut=binding.webSocketButton
        bleBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it,R.id.navigateMainToBle)
        }
        galleryBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it,R.id.navigateMainToGallery)
        }
        webBut.setOnClickListener {
            ReusableFunctions.navigateSrcToDest(it,R.id.navigateMainToWebSocket)

        }
    }

}