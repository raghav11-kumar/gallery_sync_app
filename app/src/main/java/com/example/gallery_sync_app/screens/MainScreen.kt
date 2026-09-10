package com.example.gallery_sync_app.screens

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.example.gallery_sync_app.databinding.ActivityMainScreenBinding
import com.example.gallery_sync_app.screens.Authentication.AuthenticationViewModel
import com.example.gallery_sync_app.screens.gallery.GalleryViewModel
import com.example.gallery_sync_app.screens.websockets.WebSocketsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private val authVm: AuthenticationViewModel by viewModels()
    private val galleryVm: GalleryViewModel by viewModels()

    private var showMenu = false
    private var isEditMode = false
    private val webSocketsManager = WebSocketsManager()

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

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU && androidx.core.app.ActivityCompat.checkSelfPermission(
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
            supportFragmentManager.findFragmentById(com.example.gallery_sync_app.R.id.frag_cont) as androidx.navigation.fragment.NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            showMenu = false
            when (destination.id) {
                com.example.gallery_sync_app.R.id.logoScreen -> {
                    binding.appBarLayout.visibility = android.view.View.GONE
                }

                com.example.gallery_sync_app.R.id.buttonHolderFragScreen -> {
                    lifecycleScope.launch {
                        authVm.UserInformation.collect {user->
                            user?.let {
                                android.widget.Toast.makeText(context,"Welcome ${it.name}",
                                    android.widget.Toast.LENGTH_SHORT).show()

                            }

                        }
                    }
                    binding.appBarLayout.visibility = android.view.View.VISIBLE
                    binding.buttonProfileImageView.visibility = android.view.View.VISIBLE
                    supportActionBar?.title =getString(com.example.gallery_sync_app.R.string.main_screen_header)

                    lifecycleScope.launch {
                        userInfo.collect {
                            com.bumptech.glide.Glide.with(context).load(it?.imageUrl)
                                .centerCrop()
                                .into(binding.userLogo)
                        }
                    }
                    binding.buttonProfileImageView.setOnClickListener {
                        navController.navigate(com.example.gallery_sync_app.R.id.navigateMainToUserProfile)
                    }
                }

                com.example.gallery_sync_app.R.id.galleryFragScreen -> {
                    supportActionBar?.title = "Gallery"
                    binding.buttonProfileImageView.visibility = android.view.View.GONE

                    showMenu = true
                }

                com.example.gallery_sync_app.R.id.userProfile -> {
                    binding.buttonProfileImageView.visibility = android.view.View.GONE
                }

                com.example.gallery_sync_app.R.id.webSocketFragScreen -> {
                    supportActionBar?.title = "WebSockets"
                    binding.buttonProfileImageView.visibility = android.view.View.GONE
                }

                com.example.gallery_sync_app.R.id.bleFragScreen -> {
                    binding.buttonProfileImageView.visibility = android.view.View.GONE
                }
                com.example.gallery_sync_app.R.id.loginFragScreen->{
                    binding.appBarLayout.visibility= android.view.View.GONE
                }
                com.example.gallery_sync_app.R.id.signInFragScreen->{
                    binding.appBarLayout.visibility= android.view.View.GONE

                }
                com.example.gallery_sync_app.R.id.bleInfo->{
                    binding.appBarLayout.visibility= android.view.View.GONE

                }

                else -> {
                    binding.appBarLayout.visibility = android.view.View.VISIBLE
                }
            }
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(com.example.gallery_sync_app.R.menu.app_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: android.view.Menu?): Boolean {
        val editItem = menu?.findItem(com.example.gallery_sync_app.R.id.editIcon)

        menu?.findItem(com.example.gallery_sync_app.R.id.addIcon)?.isVisible = showMenu
        editItem?.isVisible = showMenu

        if (isEditMode) {
            editItem?.setIcon(com.example.gallery_sync_app.R.drawable.outline_close_24)
        } else {
            editItem?.setIcon(com.example.gallery_sync_app.R.drawable.outline_edit_24)
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            com.example.gallery_sync_app.R.id.addIcon -> {
                galleryVm.openGallery()
                true
            }

            com.example.gallery_sync_app.R.id.editIcon -> {
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