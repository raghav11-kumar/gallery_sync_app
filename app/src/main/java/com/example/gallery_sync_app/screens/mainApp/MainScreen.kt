package com.example.gallery_sync_app.screens.mainApp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.ActivityMainScreenBinding
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreen : AppCompatActivity() {
    private lateinit var binding: ActivityMainScreenBinding
    private val authVm: AuthenticationViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userInfo=authVm.UserInformation


        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

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
        val context=this
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frag_cont) as NavHostFragment

        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { controller, destination, bundle ->
            when (destination.id){
                R.id.logoScreen ->{
                    binding.appBarLayout.visibility= View.GONE
                }
                R.id.buttonHolderFragScreen->{
                    binding.appBarLayout.visibility= View.VISIBLE
                    binding.buttonProfileImageView.visibility= View.VISIBLE
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
                R.id.galleryFragScreen->{
                    supportActionBar?.title="Gallery"
                    binding.buttonProfileImageView.visibility= View.GONE
                    binding.toolbar.inflateMenu(R.menu.app_menu)



                }
                R.id.userProfile ->{
                    binding.buttonProfileImageView.visibility= View.GONE

                }
                R.id.webSocketFragScreen->{
                    supportActionBar?.title="WebSockets"
                    binding.buttonProfileImageView.visibility= View.GONE
                }
                R.id.bleFragScreen->{
                    binding.buttonProfileImageView.visibility= View.GONE
                }
                else ->{
                    binding.appBarLayout.visibility= View.VISIBLE
                }

            }

        }




    }
}