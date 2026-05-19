package com.example.mobile_project_notes_app.DI

import androidx.room.Room
import com.example.mobile_project_notes_app.data.AppDatabase
import com.example.mobile_project_notes_app.features.feature_calendar.data.CalendarRepository
import com.example.mobile_project_notes_app.features.feature_calendar.domain.CalendarRepositoryImpl
import com.example.mobile_project_notes_app.features.feature_category.data.CategoryRepositoryImpl
import com.example.mobile_project_notes_app.features.feature_note.domain.NoteRepository
import com.example.mobile_project_notes_app.features.feature_category.domain.CategoryRepository
import com.example.mobile_project_notes_app.features.feature_note.data.NoteRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "notes_app_database"

        )
            .fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().noteDao() }
    single { get<AppDatabase>().calendarDayDao() }

    single<CalendarRepository> { CalendarRepositoryImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }

}