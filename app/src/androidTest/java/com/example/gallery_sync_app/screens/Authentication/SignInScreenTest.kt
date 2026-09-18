package com.example.gallery_sync_app.screens.Authentication

import android.content.Intent
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.simpleViewResourceName
import androidx.test.uiautomator.uiAutomator
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInScreenTest {

    @Test
    fun testSignInScreen() {
        uiAutomator {
            // 0. Clear App Data to ensure we aren't already logged in
            clearAppData()

            // 1. Launch the app using Intent
            val context = instrumentation.targetContext
            val intent = context.packageManager.getLaunchIntentForPackage("com.example.gallery_sync_app")
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            // 2. Wait for the app to come to foreground
            device.waitForIdle()

            // 3. Find and type the Name
            // Increased timeout to 15s to account for the 3s LogoScreen delay + device slowness
            val nameField = onElement(timeoutMs = 15000) {
                simpleViewResourceName() == "nameInputLayout"
            }
            nameField.setText("Test User")

            // 4. Find and type the Email
            onElement { simpleViewResourceName() == "emailInputLayout" }.setText("test@example.com")

            // 5. Find and type the Password
            onElement { simpleViewResourceName() == "passInputLayout" }.setText("password123")

            // 6. Click the Sign In Button
            onElement { simpleViewResourceName() == "SignInButton" }.click()
            
            device.waitForIdle()
        }
    }
}