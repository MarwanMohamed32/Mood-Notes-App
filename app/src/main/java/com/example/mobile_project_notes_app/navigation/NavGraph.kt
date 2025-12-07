package com.example.mobile_project_notes_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_project_notes_app.screens.MainScreen
import com.example.mobile_project_notes_app.screens.MoodScreen.MoodScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(navController)
        }
        composable("mood") {
            MoodScreen(navController)
        }

    }
}