package com.example.mobile_project_notes_app.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.mobile_project_notes_app.data.local.database.entity.Note

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM Note Where category_name = :categoryName")
    suspend fun getNotesFromSection(categoryName: String): Note?

    @Transaction
    @Query("SELECT * FROM Note")
    suspend fun getAllNotes(): Note?

    @Transaction
    @Query("SELECT * FROM Note Where favourite = :isFavourite")
    suspend fun getNotesFromFavourite(isFavourite: Boolean): Note?
}