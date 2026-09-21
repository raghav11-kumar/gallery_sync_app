package com.example.gallery_sync_app

import android.content.Intent
import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    lateinit var device: UiDevice
    lateinit var scrollable: UiScrollable
    val PACKAGE_NAME = "com.example.gallery_sync_app"

    @Before
    fun setUp() {

        device = UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )

        scrollable =
            UiScrollable(UiSelector().resourceId(PACKAGE_NAME + ":id/" + "scrollViewId"))
    }

    @Test
    fun signInTesting() {
        try {
            openApp()
            device.waitForIdle()
            println("Current package: ${device.currentPackageName}")
            val signInButton = device.wait(
                Until.findObject(
                    By.res(
                        PACKAGE_NAME,
                        "signInButton"
                    )), 20_000
            )
            assertNotNull(
                "Sign In button was not found",
                signInButton
            )
            signInButton.click()
            Thread.sleep(5000)
        } catch (e: Exception) {
            Log.e("SignInTesting", "${e.message}")
        }
    }

    private fun openApp() {
        device.pressHome()
        val displayWidth = device.displayWidth
        val startX = displayWidth.div(2)
        val startY = (device.displayHeight.times(0.5)).toInt()!!
        val endY = (device.displayHeight?.times(0.2))?.toInt()!!
        device?.swipe(startX, startY, startX, endY, 10)
        val appDrawer = UiScrollable(UiSelector().scrollable(true))
        val appIcon = device?.findObject(UiSelector().description("sync")) // open app
        try {
            appIcon?.click()

        } catch (e: Exception) {
            Log.e("OPENAPPTEST", "Button click failed: ${e.message}")
        }
    }
}