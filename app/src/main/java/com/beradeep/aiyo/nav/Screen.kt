package com.beradeep.aiyo.nav

sealed class Screen(val route: String) {
    data object ChatScreen : Screen("chat_screen")
    data object SettingsScreen : Screen("settings_screen")
}
