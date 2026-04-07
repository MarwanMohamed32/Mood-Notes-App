package com.example.mobile_project_notes_app.screens.Calendar

import androidx.compose.runtime.*
import com.example.mobile_project_notes_app.components.Common.MainScaffoldWithSideMenu
import com.example.mobile_project_notes_app.navigation.Route

@Composable
fun CalendarMainScreen(
    onNavigate: (Route) -> Unit,
    onBackClick: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    MainScaffoldWithSideMenu(
        isMenuOpen = isMenuOpen,
        onMenuOpenChange = { isMenuOpen = it },
        onNavigate = onNavigate,
        onBackClick = onBackClick
    ) { onMenuClick ->
        CalendarScreen(
            onMenuClick = onMenuClick,
        )
    }
}
