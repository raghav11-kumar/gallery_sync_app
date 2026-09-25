package com.example.gallery_sync_app

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiScrollable
import androidx.test.uiautomator.UiSelector
import androidx.test.uiautomator.Until
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
    fun appFlow() {
        openApp()
        loginCheck()
        Thread.sleep(2000)
        ReusableTestCases.userProfileCheck(PACKAGE_NAME, uiDevice = device)
        Thread.sleep(2000)
        ReusableTestCases.bleCheck(PACKAGE_NAME,device)
    }

    private fun loginCheck() {
        try {
            val loginButton = ReusableTestCases.getDeviceId(device,PACKAGE_NAME,"loginButton")
            if (loginButton == null) {
                Log.e("AppUiAutomation", "LoginButton isNull")

            }
            Log.e("AppUiAutomation", "Requesting For Email button")

            val gmail =ReusableTestCases.getDeviceId(device,PACKAGE_NAME,"userEmail")
            if (gmail == null) {
                Log.e("AppUiAutomation", "Request Failed Cuz Email button returned Null")


            }
            gmail?.text = DefaultValues.email
            Log.e("AppUiAutomation", "Email ${gmail.text} is Logged In")

            Thread.sleep(1000)
            Log.e("AppUiAutomation", "Requesting For password button")

            val password = ReusableTestCases.getDeviceId(device,PACKAGE_NAME,"passInput")
            if (password == null) {
                Log.e("AppUiAutomation", "Request failed Cuz password  button returned null")
            }
            password?.text = DefaultValues.password
            Thread.sleep(1000)
            Log.e("AppUiAutomation", "clicking the loginButton")

            loginButton.click()
            Log.e("AppUiAutomation", "Successfully IN MainScreen")
        } catch (e: Exception) {
            Log.e("AppUiAutomation", "Failed In Login Check Cuz Of ${e.message}")
        }
    }

    private fun openApp() {
        device.pressHome()
        val displayWidth = device.displayWidth
        val startX = displayWidth.div(2)
        val startY = (device.displayHeight.times(0.5)).toInt()
        val endY = (device.displayHeight.times(0.2)).toInt()
        device.swipe(startX, startY, startX, endY, 10)
        val appIcon = device.findObject(UiSelector().description("sync")) // open app
        try {
            appIcon.click()

        } catch (e: Exception) {
            Log.e("OPENAPPTEST", "Button click failed: ${e.message}")
        }
    }
}