package com.example.mobile_project_notes_app.data.repository

import com.example.mobile_project_notes_app.data.dao.CalendarDayDao
import com.example.mobile_project_notes_app.data.local.database.entity.CalendarDayEntry
import kotlinx.coroutines.flow.Flow

class CalendarRepository(private val calendarDayDao: CalendarDayDao) {

    fun getAllMoodEntries(): Flow<List<CalendarDayEntry>> = calendarDayDao.getAllCalendarDayEntries()

    suspend fun getMoodByDate(date: String): CalendarDayEntry? = calendarDayDao.getCalendarDayByDate(date)

    fun getMoodEntriesForMonth(yearMonth: String): Flow<List<CalendarDayEntry>> =
        calendarDayDao.getCalendarDaysForMonth(yearMonth)

    suspend fun insertMoodEntry(calendarDayEntry: CalendarDayEntry) {
        calendarDayDao.insertCalendarDayEntry(calendarDayEntry)
    }

    suspend fun deleteMoodByDate(date: String) {
        calendarDayDao.deleteCalendarDayByDate(date)
    }
}