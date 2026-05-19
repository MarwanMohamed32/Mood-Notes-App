package com.example.mobile_project_notes_app.DI


import android.widget.CalendarView
import com.example.mobile_project_notes_app.features.feature_calendar.presentation.CalendarViewModel
import com.example.mobile_project_notes_app.features.feature_category.presentation.CategoryViewModel
import com.example.mobile_project_notes_app.features.feature_note.presentation.NoteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NoteViewModel)
    viewModelOf(::CategoryViewModel)
    viewModelOf(::CalendarViewModel)
}