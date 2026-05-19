package com.example.mobile_project_notes_app.features.feature_calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_project_notes_app.data.local.database.entity.CalendarDayEntry
import com.example.mobile_project_notes_app.features.feature_calendar.data.CalendarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarViewModel(
    private val repository: CalendarRepository
) : ViewModel() {

    private val _currentYearMonth = MutableStateFlow(getCurrentYearMonth())
    val currentYearMonth: StateFlow<String> = _currentYearMonth

    val moodEntriesForMonth: StateFlow<List<CalendarDayEntry>> =
        repository.getMoodEntriesForMonth(_currentYearMonth.value).stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addMoodEntry(mood: String, note: String = "") {
        viewModelScope.launch {
            val today = getCurrentDate()
            repository.insertMoodEntry(
                CalendarDayEntry(
                    date = today,
                    mood = mood,
                    note = note
                )
            )
        }
    }

    fun changeMonth(yearMonth: String) {
        _currentYearMonth.value = yearMonth
    }

    private fun getCurrentDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun getCurrentYearMonth(): String =
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
}