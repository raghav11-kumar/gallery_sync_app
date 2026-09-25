package com.example.gallery_sync_app.screens

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityMainScreenBinding
import com.example.gallery_sync_app.screens.authentication.AuthenticationViewModel
import com.example.gallery_sync_app.screens.gallery.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private val authVm: AuthenticationViewModel by viewModels()
    private val galleryVm: GalleryViewModel by viewModels()

    private var showMenu = false
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        lifecycleScope.launch {
            galleryVm.editClickOpen.collect { editMode ->
                isEditMode = editMode
                invalidateOptionsMenu()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.appBarLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && androidx.core.app.ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            androidx.core.app.ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                    android.Manifest.permission.BLUETOOTH_SCAN,
                    android.Manifest.permission.BLUETOOTH
                ), 101
            )
        }

        val userInfo = authVm.UserInformation
        val context = this
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frag_cont) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            showMenu = false
            when (destination.id) {
                R.id.logoScreen, R.id.loginFragScreen, R.id.signInFragScreen, R.id.bleInfo -> {
                    binding.appBarLayout.visibility = View.GONE
                }

                R.id.buttonHolderFragScreen -> {

                    binding.appBarLayout.visibility = android.view.View.VISIBLE
                    binding.buttonProfileImageView.visibility = android.view.View.VISIBLE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.buttonProfileImageView.visibility = View.VISIBLE
                    supportActionBar?.title =
                        getString(R.string.main_screen_header)

                    lifecycleScope.launch {
                        userInfo.collect {
                            com.bumptech.glide.Glide.with(context).load(it?.imageUrl).centerCrop()
                                .into(binding.userLogo)
                        }
                    }
                    binding.buttonProfileImageView.setOnClickListener {
                        navController.navigate(R.id.navigateMainToUserProfile)
                    }
                }

                R.id.galleryFragScreen -> {
                    lifecycleScope.launch {
                        galleryVm.isUploading.collect {
                            binding.appBarLayout.visibility =
                                if (it) View.GONE else View.VISIBLE
                        }
                    }
                    supportActionBar?.title = getString(R.string.gallery_)
                    binding.buttonProfileImageView.visibility = View.GONE

                    showMenu = true
                }

                R.id.userProfile -> {
                    binding.buttonProfileImageView.visibility = View.GONE
                }

                R.id.webSocketFragScreen -> {
                    supportActionBar?.title = getString(R.string.webSockets)
                    binding.buttonProfileImageView.visibility = View.GONE
                }

                R.id.bleFragScreen -> {
                    supportActionBar?.title = getString(R.string.ble)
                    binding.buttonProfileImageView.visibility = View.GONE
                }


                R.id.mqqtFragScreen -> {
                    supportActionBar?.title =
                        getString(com.example.gallery_sync_app.R.string.mqqt)
                    binding.buttonProfileImageView.visibility = View.GONE

                }

                else -> {
                    binding.appBarLayout.visibility = android.view.View.VISIBLE
                }
            }
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: android.view.Menu?): Boolean {
        val editItem = menu?.findItem(R.id.editIcon)

        menu?.findItem(R.id.addIcon)?.isVisible = showMenu
        editItem?.isVisible = showMenu

        if (isEditMode) {
            editItem?.setIcon(R.drawable.outline_close_24)
        } else {
            editItem?.setIcon(R.drawable.outline_edit_24)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.addIcon -> {
                galleryVm.openGallery()
                true
            }

            R.id.editIcon -> {
                galleryVm.openEdit()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        galleryVm.closeGallery()
    }
}