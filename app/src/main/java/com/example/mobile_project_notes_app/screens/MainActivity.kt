package com.example.mobile_project_notes_app.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mobile_project_notes_app.navigation.NavigationRoot
import com.example.mobile_project_notes_app.ui.theme.Mobile_Project_Notes_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Mobile_Project_Notes_AppTheme {
                NavigationRoot()
            }
        }
    }
}