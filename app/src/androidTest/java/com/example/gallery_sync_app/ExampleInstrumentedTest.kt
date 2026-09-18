/*
package com.example.gallery_sync_app

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

*/
/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 *//*

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Before
    fun setUp() {

    }
    @Test
    fun checkCurrentScreen() {
        try {
            val instrumentation =
                InstrumentationRegistry.getInstrumentation()

            val device =
                UiDevice.getInstance(instrumentation)

            device.waitForIdle()
            val displayWidth = device.displayWidth
            val startX = displayWidth?.div(2)!!
            val startY = (device.displayHeight?.times(0.5))?.toInt()!!
            val endY = (device.displayHeight?.times(0.2))?.toInt()!!
            device.swipe(startX, startY, startX, endY, 10)
            val appDrawer = UiScrollable(UiSelector().scrollable(true))
//        UiAutomatorActions.scrollForwardUntil(appDrawer) { TestUtils.isAppAvailable(mDevice) }
            val appIcon =
                device.findObject(UiSelector().description("sync")) // open app
            appIcon.click()
            // Wait 15 seconds so the splash/logo has time to disappear
            Thread.sleep(15_000)

            println("Current package: ${device.currentPackageName}")
            val signInButton = device.wait(
                Until.findObject(
                    By.res("com.example.gallery_sync_app", "SignInButton")
                ),
                20_000
            )
            signInButton.click()

            assertNotNull(signInButton)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}*/

package com.example.gallery_sync_app

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

    private var mDevice: UiDevice? = null
    private var scrollable: UiScrollable? = null

    private val packageName = "com.example.gallery_sync_app"

    @Before
    fun setUp() {

        // Initialize UiDevice
        mDevice = UiDevice.getInstance(
            InstrumentationRegistry.getInstrumentation()
        )

        // Initialize scrollable launcher
        scrollable = UiScrollable(
            UiSelector().scrollable(true)
        )
    }

    @Test
    fun checkCurrentScreen() {

        openApp()

        // Wait until the app is loaded
        mDevice?.waitForIdle()

        // Wait for Sign In button
        val signInButton = mDevice?.wait(
            Until.findObject(
                By.res(
                    packageName,
                    "SignInButton"
                )
            ),
            20_000
        )

        assertNotNull(signInButton)

        signInButton?.click()

        mDevice?.waitForIdle()
    }

    private fun openApp() {

        val device = mDevice ?: return

        // Open launcher
        device.pressHome()

        device.waitForIdle()

        // Same idea as WifiFan:
        // find the app icon in the launcher
        var appIcon = device.findObject(
            UiSelector().description("sync")
        )

        // If the icon isn't currently visible, scroll the launcher
        if (!appIcon.exists()) {

            scrollable?.scrollForward()

            device.waitForIdle()

            appIcon = device.findObject(
                UiSelector().description("sync")
            )
        }

        // Open the application
        if (appIcon.exists()) {
            appIcon.click()
        }
    }
}