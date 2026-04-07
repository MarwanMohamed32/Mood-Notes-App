package com.example.mobile_project_notes_app.DI


import android.widget.CalendarView
import com.example.mobile_project_notes_app.viewmodel.CalendarViewModel
import com.example.mobile_project_notes_app.viewmodel.CategoryViewModel
import com.example.mobile_project_notes_app.viewmodel.NoteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NoteViewModel)
    viewModelOf(::CategoryViewModel)
    viewModelOf(::CalendarViewModel)

}
