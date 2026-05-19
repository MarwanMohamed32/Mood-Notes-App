package com.example.mobile_project_notes_app.navigation

import androidx.navigation3.runtime.NavKey
import com.example.mobile_project_notes_app.models.NoteType
import kotlinx.serialization.Serializable


@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object HomeScreen : Route

    @Serializable
    data object CalendarScreen : Route

    @Serializable
    data object MoodScreen : Route

    @Serializable
    data object SplashScreen : Route
    @Serializable
    data class AddNoteScreen(val noteType: NoteType) : Route

    @Serializable
    data class EditNoteScreen(val noteId: Int) : Route
}