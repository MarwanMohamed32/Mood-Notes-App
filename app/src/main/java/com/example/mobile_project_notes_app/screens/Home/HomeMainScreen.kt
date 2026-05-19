package com.example.mobile_project_notes_app.screens.Home

import androidx.compose.runtime.*
import com.example.mobile_project_notes_app.components.Common.MainScaffoldWithSideMenu
import com.example.mobile_project_notes_app.models.NoteType
import com.example.mobile_project_notes_app.navigation.Route

@Composable
fun HomeMainScreen(
    onNavigate: (Route) -> Unit,
    onBackClick: () -> Unit,
    onNavigateToAddNote: (NoteType) -> Unit,
    onNavigateToEditNote: (Int) -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    MainScaffoldWithSideMenu(
        isMenuOpen = isMenuOpen,
        onMenuOpenChange = { isMenuOpen = it },
        onNavigate = onNavigate,
        onBackClick = onBackClick
    ) { onMenuClick ->
        HomeScreen(
            onMenuClick = onMenuClick,
            onNavigateToAddNote = onNavigateToAddNote,
            onNavigateToEditNote = onNavigateToEditNote
        )
    }
}