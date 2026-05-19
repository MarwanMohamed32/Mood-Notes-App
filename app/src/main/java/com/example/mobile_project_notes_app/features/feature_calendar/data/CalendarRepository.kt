package com.example.mobile_project_notes_app.features.feature_calendar.data

import com.example.mobile_project_notes_app.data.dao.CalendarDayDao
import com.example.mobile_project_notes_app.data.local.database.entity.CalendarDayEntry
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {

    fun getAllMoodEntries(): Flow<List<CalendarDayEntry>>

    suspend fun getMoodByDate(date: String): CalendarDayEntry?

    fun getMoodEntriesForMonth(yearMonth: String): Flow<List<CalendarDayEntry>>

    suspend fun insertMoodEntry(calendarDayEntry: CalendarDayEntry)

    suspend fun deleteMoodByDate(date: String)
}