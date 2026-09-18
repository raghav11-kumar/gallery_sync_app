package com.example.gallery_sync_app

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun loginButtonIsDisplayed() {

        // Get instrumentation
        val instrumentation =
            InstrumentationRegistry.getInstrumentation()

        // Get the device
        val device =
            UiDevice.getInstance(instrumentation)

        // Get your application's context
        val context =
            instrumentation.targetContext

        // Get the launch intent
        val intent =
            context.packageManager
                .getLaunchIntentForPackage(context.packageName)

        // Launch your app
        intent?.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        )

        context.startActivity(intent)

        // Wait until the device becomes idle
        device.waitForIdle()

        // Find Login button
        val loginButton = device.findObject(
            By.res(
                context.packageName,
                "loginButton"
            )
        )


    }
}