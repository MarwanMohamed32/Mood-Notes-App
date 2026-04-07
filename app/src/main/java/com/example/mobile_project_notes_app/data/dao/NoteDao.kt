package com.example.mobile_project_notes_app.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mobile_project_notes_app.data.local.database.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM Note WHERE category_name = :categoryName ORDER BY created_at DESC")
    fun getNotesFromCategory(categoryName: String): Flow<List<Note>>

    @Query("SELECT * FROM Note ORDER BY created_at DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE favourite = 1 ORDER BY created_at DESC")
    fun getFavouriteNotes(): Flow<List<Note>>

    @Query("SELECT * FROM Note WHERE uid = :noteId")
    suspend fun getNoteById(noteId: Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("UPDATE Note SET favourite = :isFavourite WHERE uid = :noteId")
    suspend fun toggleFavourite(noteId: Int, isFavourite: Boolean)

}