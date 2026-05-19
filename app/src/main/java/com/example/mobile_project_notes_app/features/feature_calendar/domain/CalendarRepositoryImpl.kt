package com.example.mobile_project_notes_app.features.feature_calendar.domain

import com.example.mobile_project_notes_app.data.dao.CalendarDayDao
import com.example.mobile_project_notes_app.data.local.database.entity.CalendarDayEntry
import com.example.mobile_project_notes_app.features.feature_calendar.data.CalendarRepository
import kotlinx.coroutines.flow.Flow

class CalendarRepositoryImpl(private val calendarDayDao: CalendarDayDao) : CalendarRepository {

    override fun getAllMoodEntries(): Flow<List<CalendarDayEntry>> = calendarDayDao.getAllCalendarDayEntries()

    override suspend fun getMoodByDate(date: String): CalendarDayEntry? = calendarDayDao.getCalendarDayByDate(date)

    override fun getMoodEntriesForMonth(yearMonth: String): Flow<List<CalendarDayEntry>> =
        calendarDayDao.getCalendarDaysForMonth(yearMonth)

    override suspend fun insertMoodEntry(calendarDayEntry: CalendarDayEntry) {
        calendarDayDao.insertCalendarDayEntry(calendarDayEntry)
    }

    override suspend fun deleteMoodByDate(date: String) {
        calendarDayDao.deleteCalendarDayByDate(date)
    }
}