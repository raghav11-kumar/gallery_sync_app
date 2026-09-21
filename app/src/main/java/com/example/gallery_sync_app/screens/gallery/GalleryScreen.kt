package com.example.gallery_sync_app.screens.gallery

import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager.WIFI_STATE_CHANGED_ACTION
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentGalleryScreenBinding
import com.example.gallery_sync_app.screens.gallery.broadcast.WifiListener
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

@AndroidEntryPoint
class GalleryScreen : Fragment(R.layout.fragment_gallery_screen) {
    private lateinit var binding: FragmentGalleryScreenBinding
    private val galleryVm: GalleryViewModel by activityViewModels()
    private lateinit var wifiListener: WifiListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentGalleryScreenBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        val adapter = RecyclerAdapterImp(emptyList()) {
            galleryVm.deleteImage(it)
        }
        wifiListener= WifiListener{
            ReusableFunctions.DefaultAlertDialog(requireContext(),"Internet Disconnected","Retry","Cancel"){
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                startActivity(intent)
            }
        }

        binding.recView.layoutManager = LinearLayoutManager(requireContext())
        binding.recView.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            galleryVm.imageList.collect { list ->
                adapter.updateList(list)
                if (list.isEmpty()) {
                    binding.noImagesIcon.visibility = View.VISIBLE
                    binding.noImagesText.visibility = View.VISIBLE
                    binding.recView.visibility = View.GONE
                } else {
                    binding.noImagesIcon.visibility = View.GONE
                    binding.noImagesText.visibility = View.GONE
                    binding.recView.visibility = View.VISIBLE
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryVm.isUploading.collect { isUploading ->
                if (isUploading) {
                    binding.screenProgressBar.visibility = View.VISIBLE
                    binding.loadingOverlay.visibility = View.VISIBLE

                } else {
                    binding.loadingOverlay.visibility = View.GONE

                    binding.screenProgressBar.visibility = View.GONE
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryVm.errorFlowing.collect { message ->
                ReusableFunctions.DefaultAlertDialog(
                    view.context, message, "OK", "Cancel"
                ) {

                }
            }
        }
        val launcher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                galleryVm.saveImage(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryVm.editClickOpen.collect { isEditMode ->
                adapter.setEditMode(isEditMode)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            galleryVm.galleryOpen.collect { isOpen ->
                if (isOpen) {
                    galleryVm.closeGallery()
                    launcher.launch("image/*")
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        requireActivity().registerReceiver(wifiListener, IntentFilter(WIFI_STATE_CHANGED_ACTION))
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(wifiListener)

    }


}

