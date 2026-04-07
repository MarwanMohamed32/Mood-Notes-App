package com.example.mobile_project_notes_app.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobile_project_notes_app.data.dao.CalendarDayDao
import com.example.mobile_project_notes_app.data.dao.CategoryDao
import com.example.mobile_project_notes_app.data.dao.NoteDao
import com.example.mobile_project_notes_app.data.entity.Category
import com.example.mobile_project_notes_app.data.local.database.entity.Note
import com.example.mobile_project_notes_app.data.local.database.entity.CalendarDayEntry

@Database(
    entities = [Category::class, Note::class, CalendarDayEntry::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao
    abstract fun calendarDayDao(): CalendarDayDao
}