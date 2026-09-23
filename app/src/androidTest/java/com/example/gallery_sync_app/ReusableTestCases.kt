package com.example.gallery_sync_app

import android.util.Log
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2
import androidx.test.uiautomator.Until

object ReusableTestCases {
    private fun getObjects(uiDevice: UiDevice, packageName: String, id: String) =
        uiDevice.findObjects(By.res(packageName, id))

    private var TAG = "AppUiAutomation"
    fun userProfileCheck(packageName: String, uiDevice: UiDevice) {
        try {
            Log.d(TAG, "Requesting For UserProfileLogo")
            val userIcon = getDeviceId(uiDevice, packageName, "userLogo")
            if (userIcon == null) {
                Log.e(TAG, "userProfileIcon was Null")
            } else {
                Log.d(TAG, "userProfileIcon Was Clicked")
                userIcon.click()
            }
            Thread.sleep(1000)
            val editName = getDeviceId(uiDevice, packageName, "userProfileName")
            if (editName == null) {
                Log.e(TAG, "editName was Null")

            } else {
                Log.d(TAG, "EditText Info was  cleared")
                editName.clear()
            }
            editName?.text = DefaultValues.userName
            Log.d(TAG, "Requesting For editNameIcon")
            val editIcon = getDeviceId(uiDevice, packageName, "editNameIcon")
            if (editIcon == null) {
                Log.e(TAG, "EditIcon Returned Null ${editIcon}")
            } else {
                Log.d(TAG, "Clicked The EditIcon Button")
                editIcon.click()
            }
            Thread.sleep(4000)
            editName.clear()
            editName.text = DefaultValues.userName
            Thread.sleep(1000)
            Log.d(TAG, "Saved The Edit Text Info")
            val width = uiDevice.displayWidth
            val height = uiDevice.displayHeight
            uiDevice.click(
                (width * 0.93).toInt(), (height * 0.90).toInt()
            )
            Thread.sleep(2000)
            Log.d(TAG, "Requesting For logout")
            val logOut = getDeviceId(uiDevice, packageName, "logoutButton")
            if (logOut == null) {
                Log.e(TAG, "Logout is Null")
            } else {
                Log.d(TAG, "Logoput is being called")
                logOut.click()
                val posBut = getDeviceId(uiDevice, packageName, "btnPositive")
                if (posBut == null) {
                    Log.e(TAG, "PosBut is null")
                } else {
                    posBut.click()
                }
                Thread.sleep(1000)
            }
            uiDevice.pressBack()
        } catch (e: Exception) {
            Log.d(TAG, "Failed In userProfileCheck Cuz ${e.message}")
        }

    }

    fun bleCheck(
        packageName: String, uiDevice: UiDevice
    ) {
        try {

            // Open BLE screen
            val bleButton = getDeviceId(
                uiDevice, packageName, "ble_button"
            )

            if (bleButton == null) {
                Log.e(TAG, "BLE button not found")
                return
            }

            bleButton.click()
            uiDevice.waitForIdle()

            // Try scanning up to 3 times
            var attempt = 0

            while (attempt < 3) {

                attempt++

                Log.d(
                    TAG, "BLE scan attempt: $attempt"
                )

                // Wait for devices to appear
                Thread.sleep(15_000)

                // Search for the MAC address again
                val macAddress = getObjects(uiDevice = uiDevice, packageName, "macAddress")
                for (mac in macAddress) {
                    if (mac.text.equals(DefaultValues.macAddress)) {

                        Log.d(
                            TAG, "Target MAC found: ${mac.text}"
                        )

                        macAddressClick(
                            uiDevice, packageName, mac
                        )

                        return

                    } else {

                        Log.d(
                            TAG, "Different MAC found: ${mac.text}"
                        )
                    }
                }

                // If we reach here, target wasn't found.
                // Scan again if attempts to remain.

                if (attempt < 3) {

                    val scanButton = getDeviceId(
                        uiDevice, packageName, "bleClick"
                    )

                    if (scanButton == null) {
                        Log.e(
                            TAG, "Scan button not found"
                        )
                        return
                    }
                    scanButton.click()
                    uiDevice.waitForIdle()
                } else if (attempt > 3) {
                    Log.d(
                        TAG, "Target MAC was not found after 3 attempts"
                    )
                    break
                }
            }
            uiDevice.pressBack()
            Thread.sleep(1000)


        } catch (e: Exception) {

            Log.d(
                TAG, "BleCheck failed: ${e.message}"
            )
        }
    }

    private fun macAddressClick(
        uiDevice: UiDevice, packageName: String, itemMacAddress: UiObject2
    ) {

        try {
            Log.d(TAG, "Clicked On MacAddress ${itemMacAddress.text}")
            itemMacAddress.click()
            Thread.sleep(1000)
            Log.d(TAG, "Requesting For PosBut")
            val posBut = getDeviceId(uiDevice, packageName, "btnPositive")
            if (posBut == null) {
                Log.e(TAG, "PosBut is Null")
                Thread.sleep(30_000)
                val retryButton = uiDevice.findObject(By.text("retry"))
                if (retryButton == null) {
                    Log.e(TAG, "Failed To Get Retry Button")
                } else {
                    Log.d(TAG, "Clicked The RetyButton")
                    retryButton.click()
                    Thread.sleep(15_000)
                    macAddressClick(uiDevice, packageName, itemMacAddress)
                    return
                }
            } else {
                Log.d(TAG, "Clicked On PosBut")
                posBut.click()

                Thread.sleep(5_000)
                val textData = getResourceByText("Data", uiDevice)
                if (textData == null) Log.e(TAG, "TextData View was NUll")
                else textData.click()
                val infoData = getResourceByText("Info", uiDevice)
                if (infoData == null) Log.e(TAG, "InfoData View Was Null")
                else infoData.click()
                Thread.sleep(3000)
                uiDevice.pressBack()
                Thread.sleep(2000)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Mac AddressClick Function Failed Cuz Of ${e.message}")
        }
    }

    fun galleryCheck(uiDevice: UiDevice, packageName: String) {
        try {
            Log.d(TAG, "Requesting For GalleryButton")
            val galleryButton = getDeviceId(uiDevice, packageName, "GalleryButton")
            if (galleryButton == null) {
                Log.e(TAG, "Gallery Button is Null")
            } else {
                Log.d(TAG, "Clicked the Gallery Button")
                galleryButton.click()
                Thread.sleep(1000)
                Log.d(TAG, "Requesting For AddIcon")
                val addIcon = getDeviceId(uiDevice, packageName, "addIcon")
                if (addIcon == null) {
                    Log.e(TAG, "addIcon Button is Null")
                } else {
                    Log.d(TAG, "Clicked AddIcon")
                    addAnyImage(packageName, uiDevice)
                    Thread.sleep(1000)
                    val editIcon = getDeviceId(uiDevice, packageName, "editIcon")
                    if (editIcon == null) {
                        Log.e(TAG, "EditIcon Is Null")
                    } else {
                        Thread.sleep(1000)
                        Log.d(TAG, "EditIcon Clicked")
                        editIcon.click()
                        Thread.sleep(1000)

                        val images = getObjects(uiDevice, packageName, "deleteIcon")
                        Log.d(
                            TAG, "Delete icons currently visible: ${images.size}"
                        )

                        if (images.isEmpty()) {
                            Log.d(TAG, "No more images to delete")
                        } else {
                            images[0].click()
                            val positiveButton =
                                getDeviceId(device = uiDevice, packageName, "btnPositive")

                            if (positiveButton == null) {
                                Log.e(TAG, "Positive button not found")
                            } else {

                                positiveButton.click()
                                // Give the RecyclerView a chance to update
                                uiDevice.waitForIdle()
                                Thread.sleep(500)
                            }
                        }

                    }


//                images.click()
//                Thread.sleep(1000)
//                val posButton = getDeviceId(uiDevice, packageName, "btnPositive")
//                Thread.sleep(1000)
//                posButton.click()
//                Thread.sleep(1000)


                }
            }
            uiDevice.pressBack()
            Thread.sleep(2000)
        } catch (e: Exception) {
            Log.e(TAG, "GalleryCheck Failed ${e}")
        }
    }

    fun getDeviceId(device: UiDevice, packageName: String, id: String) = device.wait(
        Until.findObject(
            By.res(packageName, id)
        ), 20_000
    )

    private fun addAnyImage(
        packageName: String, uiDevice: UiDevice
    ) {

        val addButton = getDeviceId(uiDevice, packageName, "addIcon")

        if (addButton == null) {
            Log.e(TAG, "Add button not found")
            return
        }


        val width = uiDevice.displayWidth
        val height = uiDevice.displayHeight

        Log.d(
            TAG, "Gallery screen size: width=$width height=$height"
        )
        addButton.click()
        uiDevice.waitForIdle()
        Thread.sleep(1_000)
        uiDevice.click(245, 1072)
        val doneButton = getResourceByText("Done", uiDevice)
        if (doneButton == null) {
            Log.e(TAG, "Done button not found")
        } else {
            doneButton.click()
            uiDevice.waitForIdle()
            Thread.sleep(1_000)
        }
    }

    private fun getResourceByText(text: String, uiDevice: UiDevice) = uiDevice.wait(
        Until.findObject(
            By.text(text)
        ), 20_000
    )

    fun checkNotification(uiDevice: UiDevice, packageName: String) {
        var c = 0
        while (c < 3) {
            c++
            val notId = getDeviceId(device = uiDevice, packageName, "fcm_button")
            if (notId == null) {
                Log.e(TAG, "NotID is Null")
            } else {
                Log.d(TAG, "NotId Clicking")
                notId.click()
            }
            Thread.sleep(500)
        }
        Thread.sleep(6000)
    }

    fun checkWebsocketScreen(uiDevice: UiDevice, packageName: String) {
        try {
            val webSocketButton = getDeviceId(uiDevice, packageName, "webSocket_button")
            if (webSocketButton == null) {
                Log.e(TAG, "WebSocket Button is Null")
            } else {
                webSocketButton.click()
                Thread.sleep(1000)
                val volData = getDeviceId(device = uiDevice, packageName, "volDet")
                if (volData == null) {
                    Log.e(TAG, "VolDAta Is Null")

                } else {
                    Log.d(TAG, "VolData ${volData.text}")

                }

                Thread.sleep(200)
                val currDet = getDeviceId(uiDevice, packageName, "currDet")
                if (currDet == null) {
                    Log.e(TAG, "CurrDet is Null")
                } else {
                    Log.d(TAG, "currDet is ${currDet.text}")
                }
                Thread.sleep(200)
                val actPValue = getDeviceId(uiDevice, packageName, "actPValue")

                if (actPValue == null) {
                    Log.e(TAG, "actPValue is Null")
                } else {
                    Log.d(TAG, "actPValue is ${actPValue.text}")
                }
                Thread.sleep(200)
                val apparValue = getDeviceId(uiDevice, packageName, "apparValue")
                if (apparValue == null) {
                    Log.e(TAG, "apparValue is Null")
                } else {
                    Log.d(TAG, "apparValue is ${apparValue.text}")
                }
                Thread.sleep(200)
                val rpValue = getDeviceId(uiDevice, packageName, "RpValue")
                if (rpValue == null) {
                    Log.e(TAG, "rpValue is Null")
                } else {
                    Log.d(TAG, "rpValue is ${rpValue.text}")
                }
                Thread.sleep(200)
                val dsValue = getDeviceId(uiDevice, packageName, "dsValue")
                if (dsValue == null) {
                    Log.e(TAG, "dsValue is Null")
                } else {
                    Log.d(TAG, "dsValue is ${dsValue.text}")
                }
                Thread.sleep(200)
                val enValue = getDeviceId(uiDevice, packageName, "enValue")
                if (enValue == null) {
                    Log.e(TAG, "enValue is Null")
                } else {
                    Log.d(TAG, "enValue is ${enValue.text}")
                }
                Thread.sleep(200)
                val lpValue = getDeviceId(uiDevice, packageName, "lpValue")
                if (lpValue == null) {
                    Log.e(TAG, "lpValue is Null")
                } else {
                    Log.d(TAG, "lpValue is ${lpValue.text}")
                }

            }
            Thread.sleep(1000)
            uiDevice.pressBack()
            Thread.sleep(200)
        } catch (e: Exception) {
            Log.e(TAG, "WebSocketsCheck Failed ${e}")
        }
    }


}