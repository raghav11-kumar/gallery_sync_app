package com.example.gallery_sync_app.screens.mainApp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentUserProfileBinding
import com.example.gallery_sync_app.screens.viewModels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfile : Fragment(R.layout.fragment_user_profile) {
    private lateinit var bindingEx: FragmentUserProfileBinding
    private val authVm: AuthenticationViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.e("UserProfileFrag", "Its Called")
        super.onViewCreated(view, savedInstanceState)
        bindingEx = FragmentUserProfileBinding.bind(view)
        (requireActivity() as AppCompatActivity)
            .supportActionBar
            ?.title = "User Profile"
        val textId = bindingEx.userProfileName
        val emailId = bindingEx.userProfileEmail
        val imageId = bindingEx.userProfileImage
        var imageUri: Uri
        val launcher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { Uri ->
            Uri?.let {
                imageUri = it
                authVm.saveImage(imageUri)
            }
        }
        imageId.setOnClickListener {
            launcher.launch("image/*")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            authVm
                .UserInformation.collect { user ->
                    user.let {
                        Log.e("UserProfileFrag", "The Info has Been called${it}")
                        textId.text = it.name
                        emailId.text = it.email
                        Glide.with(requireContext())
                            .load(it.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(imageId)
                    }

                }
        }

        bindingEx.logoutButton.setOnClickListener {
            // ReusableFunctions.navigateSrcToDest(it, R.id.navigateProfileToLogin)
            // You should also call authVm.signOut() here once implemented
        }
    }
}
