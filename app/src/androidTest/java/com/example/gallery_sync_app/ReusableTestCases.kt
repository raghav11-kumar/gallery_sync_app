package com.example.gallery_sync_app

import android.util.Log
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until

object ReusableTestCases {

    var TAG = "AppUiAutomation"
    fun userProfileCheck(packageName: String, uiDevice: UiDevice) {
        try {

            Log.e("AppUiAutomation", "Requesting For UserProfileLogo")
            val userIcon = getDeviceId(uiDevice, packageName, "userLogo")
            if (userIcon == null) {
                Log.e(TAG, "userProfileIcon was Null")
            }
            Log.e("AppUiAutomation", "userProfileIcon Was Clicked")


            userIcon?.click()
            Thread.sleep(1000)
            val editName = getDeviceId(uiDevice, packageName, "userProfileName")
            if (editName == null) {
                Log.e("AppUiAutomation", "editName was Null")

            }
            Log.e("AppUiAutomation", "EditText Info was  cleared")
            editName?.clear()
            editName?.text = DefaultValues.userName
            Log.e("AppUiAutomation", "Requesting For editNameIcon")
            val editIcon = getDeviceId(uiDevice, packageName, "editNameIcon")
            if (editIcon == null) {
                Log.e("AppUiAutomation", "EditIcon Returned Null ${editIcon}")
            }
            Log.e("AppUiAutomation", "Clicked The EditIcon Button")
            editIcon.click()
            Thread.sleep(4000)
            editName.clear()
            editName.text = DefaultValues.userName
            Thread.sleep(1000)
            Log.e("AppUiAutomation", "Saved The Edit Text Info")
            val width = uiDevice.displayWidth
            val height = uiDevice.displayHeight
            uiDevice.click(
                (width * 0.93).toInt(), (height * 0.90).toInt()
            )
            Thread.sleep(2000)
            uiDevice.pressBack()
            Log.e("AppUiAutomation", "Back To MainScreen")

        } catch (e: Exception) {
            Log.e("AppUiAutomation", "Failed In userProfileCheck Cuz ${e.message}")
        }

    }

    fun bleCheck(packageName: String, uiDevice: UiDevice) {
        try {
            Log.e("AppUiAutomation", "Requesting For BleButton")
            val bleButton = getDeviceId(device = uiDevice, packageName, "ble_button")
            if (bleButton == null) {
                Log.e("AppUiAutomation", "BleButton returned Null")
            }
            bleButton.click()
            Thread.sleep(15000)
            val itemMacAddress = getDeviceId(uiDevice, packageName, "macAddress")
            if (itemMacAddress == null) {
                Log.e("AppUiAutomation", "MacAddress Item Is Null")

            }
            if (itemMacAddress?.text.equals("")) {
                itemMacAddress.click()
            }

            Log.e("AppUiAutomation", "Clicked On ItemMacAddress ${itemMacAddress.text}")
            Thread.sleep(5_000)
            val textData = uiDevice.wait(
                Until.findObject(By.text("Data")), 20_000
            )
            if (textData == null)
                Log.e("AppUiAutomation", "TextData View was NUll")
            textData?.click()
            val infoData = uiDevice.wait(
                Until.findObject(
                    By.text("Info")
                ), 20_000
            )
            if (infoData == null)
                Log.e("AppUiAutomation", "InfoData View Was Null")
            infoData?.click()
            Thread.sleep(3000)
            uiDevice.pressBack()
            Thread.sleep(3000)
        } catch (e: Exception) {

        }


    }

    fun getDeviceId(device: UiDevice, packageName: String, id: String) = device.wait(
        Until.findObject(
            By.res(packageName, id)
        ),
        20_000
    )

}