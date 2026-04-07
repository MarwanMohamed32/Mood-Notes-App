package com.example.mobile_project_notes_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_project_notes_app.data.local.database.entity.Note
import com.example.mobile_project_notes_app.data.repository.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    val allNotes: StateFlow<List<Note>> =
        repository.getAllNotes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addNote(
        title: String,
        description: String,
        categoryName: String,
        bgColor: Long,
        textHoveredColor: Long,
        isFavourite: Boolean = false
    ) {
        viewModelScope.launch {
            val note = Note(
                title = title,
                description = description,
                categoryName = categoryName,
                bgColor = bgColor,
                textHoveredColor = textHoveredColor,
                favourite = isFavourite
            )
            repository.insertNote(note)
        }
    }

    suspend fun getNoteById(noteId: Int): Note? = repository.getNoteById(noteId)

    fun updateNoteById(
        noteId: Int,
        title: String,
        description: String,
        categoryName: String,
        bgColor: Long,
        textHoveredColor: Long,
        isFavourite: Boolean
    ) {
        viewModelScope.launch {
            val existingNote = repository.getNoteById(noteId)
            existingNote?.let { note ->
                val updatedNote = note.copy(
                    title = title,
                    description = description,
                    categoryName = categoryName,
                    bgColor = bgColor,
                    textHoveredColor = textHoveredColor,
                    favourite = isFavourite
                )
                repository.updateNote(updatedNote)
            }
        }
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            repository.getNoteById(noteId)?.let { repository.deleteNote(it) }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch { repository.updateNote(note) }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch { repository.deleteNote(note) }
    }

    fun toggleFavourite(noteId: Int, isFavourite: Boolean) {
        viewModelScope.launch { repository.toggleFavourite(noteId, isFavourite) }
    }

    fun getNotesFromCategory(categoryName: String): StateFlow<List<Note>> =
        repository.getNotesFromCategory(categoryName).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
