package com.example.mobile_project_notes_app.features.feature_note.domain

import com.example.mobile_project_notes_app.data.dao.NoteDao
import com.example.mobile_project_notes_app.data.local.database.entity.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getAllNotes(): Flow<List<Note>>

    fun getNotesFromCategory(categoryName: String): Flow<List<Note>>
    fun getFavouriteNotes(): Flow<List<Note>>

    suspend fun getNoteById(noteId: Int): Note?

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun toggleFavourite(noteId: Int, isFavourite: Boolean)
}