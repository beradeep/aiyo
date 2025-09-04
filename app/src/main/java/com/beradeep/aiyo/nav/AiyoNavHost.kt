package com.beradeep.aiyo.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.beradeep.aiyo.di.ChatViewModelImpl
import com.beradeep.aiyo.ui.screens.chat.ChatScreen

@Composable
fun AiyoNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.ChatScreen.route
    ) {
        composable(Screen.ChatScreen.route) {
            val vm: ChatViewModelImpl = hiltViewModel()
            ChatScreen(vm)
        }
    }
}
