package com.example.mobile_project_notes_app.data.repository

import com.example.mobile_project_notes_app.data.dao.NoteDao
import com.example.mobile_project_notes_app.data.local.database.entity.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    fun getNotesFromCategory(categoryName: String): Flow<List<Note>> =
        noteDao.getNotesFromCategory(categoryName)

    fun getFavouriteNotes(): Flow<List<Note>> = noteDao.getFavouriteNotes()

    suspend fun getNoteById(noteId: Int): Note? = noteDao.getNoteById(noteId)

    suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    suspend fun toggleFavourite(noteId: Int, isFavourite: Boolean) {
        noteDao.toggleFavourite(noteId, isFavourite)
    }
}