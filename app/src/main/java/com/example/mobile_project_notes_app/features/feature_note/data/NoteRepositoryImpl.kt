package com.example.mobile_project_notes_app.features.feature_note.data

import com.example.mobile_project_notes_app.data.dao.NoteDao
import com.example.mobile_project_notes_app.data.local.database.entity.Note
import com.example.mobile_project_notes_app.features.feature_note.domain.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()

    override fun getNotesFromCategory(categoryName: String): Flow<List<Note>> =
        noteDao.getNotesFromCategory(categoryName)

    override fun getFavouriteNotes(): Flow<List<Note>> = noteDao.getFavouriteNotes()

    override suspend fun getNoteById(noteId: Int): Note? = noteDao.getNoteById(noteId)

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override suspend fun toggleFavourite(noteId: Int, isFavourite: Boolean) {
        noteDao.toggleFavourite(noteId, isFavourite)
    }
}