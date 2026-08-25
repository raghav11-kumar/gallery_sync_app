package com.example.gallery_sync_app.screens.mainApp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.navigation.Navigation
import com.example.gallery_sync_app.R

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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bleBut=view.findViewById<Button>(R.id.ble_button)
        val galleryBut=view.findViewById<Button>(R.id.Gallery_button)
        val webBut=view.findViewById<Button>(R.id.webSocket_button)
        bleBut.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigateMainToBle)
        }
        galleryBut.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigateMainToGallery)

        }
        webBut.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.navigateMainToWebSocket)

        }
    }

}