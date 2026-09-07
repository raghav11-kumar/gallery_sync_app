package com.example.gallery_sync_app.screens.mainApp

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gallery_sync_app.R
import com.example.gallery_sync_app.databinding.FragmentUserProfileBinding
import com.example.gallery_sync_app.screens.Authentication.AuthenticationViewModel
import com.example.gallery_sync_app.screens.constants.UserStatus
import com.example.gallery_sync_app.screens.utils.ReusableFunctions
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
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "User Profile"
        val textId = bindingEx.userProfileName
        val emailId = bindingEx.userProfileEmail
        val imageId = bindingEx.userProfileImage
        val editId=bindingEx.editImageFab
        var imageUri: Uri
        val launcher = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { Uri ->
            Uri?.let {
                imageUri = it
                authVm.saveImage(imageUri)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            authVm.isIn.collect { state ->

                    when(state){
                        UserStatus.NotLogged -> {
                            ReusableFunctions.navigateSrcToDest(view, R.id.navigateProfileToLogin)
                        }
                        else->{}



                }

            }
        }
        editId.setOnClickListener {
            launcher.launch("image/*")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            authVm.UserInformation.collect { user ->
                user?.let {
                    Log.e("UserProfileFrag", "The Info has Been called${it}")
                    textId.setText(it.name)
                    emailId.text = it.email
                    Glide.with(requireContext()).load(it.imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).centerCrop().into(imageId)
                }

            }
        }

        var isEditing = false
        bindingEx.editNameIcon.setOnClickListener {
            isEditing = !isEditing
            if (isEditing) {
                textId.isEnabled = true
                textId.requestFocus()
                textId.setSelection(textId.text.length)
                bindingEx.editNameIcon.setImageResource(R.drawable.outline_close_24)
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(textId, InputMethodManager.SHOW_IMPLICIT)
            } else {
                val newName = textId.text.toString()
                authVm.updateUserName(newName)
                textId.isEnabled = false
                bindingEx.editNameIcon.setImageResource(R.drawable.outline_edit_24)
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(textId.windowToken, 0)
            }
        }

        textId.setOnEditorActionListener { _, _, _ ->
            if (isEditing) {
                bindingEx.editNameIcon.performClick()
            }
            true
        }

        bindingEx.logoutButton.setOnClickListener {
            ReusableFunctions.DefaultAlertDialog(view.context,"Are You Sure You Want To LogOut","Yes" +
                    "","No") {
                authVm.logOut()
            }
        }
    }

}
