package com.example.mobile_project_notes_app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobile_project_notes_app.data.local.database.entity.CalendarDayEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface CalendarDayDao {

    @Query("SELECT * FROM calendar_day_entries ORDER BY date DESC")
    fun getAllCalendarDayEntries(): Flow<List<CalendarDayEntry>>

    @Query("SELECT * FROM calendar_day_entries WHERE date = :date")
    suspend fun getCalendarDayByDate(date: String): CalendarDayEntry?

    @Query("SELECT * FROM calendar_day_entries WHERE date LIKE :yearMonth || '%' ORDER BY date ASC")
    fun getCalendarDaysForMonth(yearMonth: String): Flow<List<CalendarDayEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalendarDayEntry(calendarDayEntry: CalendarDayEntry)

    @Query("DELETE FROM calendar_day_entries WHERE date = :date")
    suspend fun deleteCalendarDayByDate(date: String)
}