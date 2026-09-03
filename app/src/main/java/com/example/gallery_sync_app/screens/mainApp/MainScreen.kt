package com.example.gallery_sync_app.screens.mainApp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityMainScreenBinding
import com.example.gallery_sync_app.screens.gallery.GalleryViewModel
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                ),
                101
            )
        }

        val userInfo = authVm.UserInformation
        val context = this
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frag_cont) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            showMenu = false
            when (destination.id) {
                R.id.logoScreen -> {
                    binding.appBarLayout.visibility = View.GONE
                }
                R.id.buttonHolderFragScreen -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.buttonProfileImageView.visibility = View.VISIBLE
                    supportActionBar?.title = "Main Screen"

                    lifecycleScope.launch {
                        userInfo.collect {
                            Glide.with(context)
                                .load(it.imageUrl)
                                .centerCrop()
                                .into(binding.userLogo)
                        }
                    }
                    binding.buttonProfileImageView.setOnClickListener {
                        navController.navigate(R.id.navigateMainToUserProfile)
                    }
                }
                R.id.galleryFragScreen -> {
                    supportActionBar?.title = "Gallery"
                    binding.buttonProfileImageView.visibility = View.GONE
                    
                    showMenu = true
                }
                R.id.userProfile -> {
                    binding.buttonProfileImageView.visibility = View.GONE
                }
                R.id.webSocketFragScreen -> {
                    supportActionBar?.title = "WebSockets"
                    binding.buttonProfileImageView.visibility = View.GONE
                }
                R.id.bleFragScreen -> {
                    binding.buttonProfileImageView.visibility = View.GONE
                }
                else -> {
                    binding.appBarLayout.visibility = View.VISIBLE
                }
            }
            invalidateOptionsMenu()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val editItem = menu?.findItem(R.id.editIcon)

        menu?.findItem(R.id.addIcon)?.isVisible = showMenu
        editItem?.isVisible = showMenu
        
        if (isEditMode) {
            editItem?.setIcon(R.drawable.outline_close_24)
        } else {
            editItem?.setIcon(R.drawable.edit_icon)
        }
        
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
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
}
