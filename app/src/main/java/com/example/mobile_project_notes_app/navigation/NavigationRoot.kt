package com.example.mobile_project_notes_app.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.mobile_project_notes_app.screens.AddNoteScreen
import com.example.mobile_project_notes_app.features.feature_voice_note.presentation.AddVoiceNoteScreen
import com.example.mobile_project_notes_app.screens.Calendar.CalendarMainScreen
import com.example.mobile_project_notes_app.screens.EditNoteScreen
import com.example.mobile_project_notes_app.screens.Home.HomeMainScreen
import com.example.mobile_project_notes_app.screens.MoodScreen
import com.example.mobile_project_notes_app.screens.SplashScreen


@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Route.AddVoiceNoteScreen)

    NavDisplay(
        backStack = backStack,
        entryProvider = { key ->
            when (key) {
                is Route.HomeScreen -> NavEntry(key) {
                    HomeMainScreen(
                        onNavigate = { route -> backStack.add(route) },
                        onBackClick = { backStack.removeLastOrNull() },

                        onNavigateToAddNote = { type ->
                            backStack.add(Route.AddNoteScreen(type))
                        },
                        onNavigateToEditNote = { noteId ->
                            backStack.add(Route.EditNoteScreen(noteId))
                        }
                    )
                }

                is Route.SplashScreen -> NavEntry(key) {
                    SplashScreen({})
                }

                is Route.MoodScreen -> NavEntry(key) {
                    MoodScreen(
                        onNavigate = {
                            if (backStack.lastOrNull() !is Route.HomeScreen) {
                                backStack.add(Route.HomeScreen)
                            }
                        }
                    )
                }

                is Route.CalendarScreen -> NavEntry(key) {
                    CalendarMainScreen(
                        onNavigate = { route -> backStack.add(route) },
                        onBackClick = { backStack.removeLastOrNull() }
                    )
                }

                is Route.AddVoiceNoteScreen -> NavEntry(key) {
                    AddVoiceNoteScreen()
                }

                is Route.AddNoteScreen -> NavEntry(key) {
                    AddNoteScreen(
                        noteType = key.noteType,
                        onBackClick = { backStack.removeLastOrNull() }
                    )
                }

                is Route.EditNoteScreen -> NavEntry(key) {
                    EditNoteScreen(
                        noteId = key.noteId,
                        onBackClick = { backStack.removeLastOrNull() }
                    )
                }

                else -> error("UnKnow NavKey: $key")
            }
        }
    )
}
