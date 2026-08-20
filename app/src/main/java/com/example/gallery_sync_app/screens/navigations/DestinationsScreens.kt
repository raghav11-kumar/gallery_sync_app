package com.example.gallery_sync_app.screens.navigations

sealed class DestinationsScreens(val route: String) {
    object chat_Screen: DestinationsScreens("chat_screen")

}
